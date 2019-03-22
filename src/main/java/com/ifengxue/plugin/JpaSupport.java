package com.ifengxue.plugin;

import static com.ifengxue.plugin.util.Key.createKey;

import com.ifengxue.fastjdbc.FastJdbc;
import com.ifengxue.fastjdbc.FastJdbcConfig;
import com.ifengxue.fastjdbc.SimpleFastJdbc;
import com.ifengxue.fastjdbc.Sql;
import com.ifengxue.fastjdbc.SqlBuilder;
import com.ifengxue.plugin.component.DatabaseSettings;
import com.ifengxue.plugin.entity.TableSchema;
import com.ifengxue.plugin.gui.AutoGeneratorSettingsFrame;
import com.ifengxue.plugin.util.WindowUtil;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications.Bus;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.util.IJSwingUtilities;
import com.mysql.jdbc.Driver;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

/**
 * JPA support entrance
 */
public class JpaSupport extends AnAction {
  private Logger log = Logger.getInstance(JpaSupport.class);
  static {
    try {
      Class.forName(Driver.class.getName());
    } catch (ClassNotFoundException e) {
      // ignore
    }
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    if (e.getProject() == null) {
      Messages.showWarningDialog("project not activated!", "Jps Support");
      return;
    }
    Holder.registerEvent(e);// Registration issue
    Holder.registerApplicationProperties(PropertiesComponent.getInstance());
    Holder.registerProjectProperties(PropertiesComponent.getInstance(e.getProject()));

    JFrame databaseSettingsFrame = new JFrame("Set database properties");
    DatabaseSettings databaseSettings = new DatabaseSettings();
    databaseSettingsFrame.setContentPane(databaseSettings.getRootComponent());
    databaseSettingsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    databaseSettingsFrame.setLocationRelativeTo(WindowUtil.getParentWindow(e.getProject()));
    databaseSettingsFrame.pack();
    // init text field
    initTextField(databaseSettings);
    databaseSettingsFrame.setVisible(true);

    // Registration cancel event
    databaseSettings.getBtnCancel().addActionListener(event -> databaseSettingsFrame.dispose());
    // Register for the next event
    databaseSettings.getBtnNext().addActionListener(event -> {
      String host = databaseSettings.getTextHost().getText().trim();
      if (host.isEmpty()) {
        databaseSettings.getTextHost().requestFocus();
        return;
      }
      String port = databaseSettings.getTextPort().getText().trim();
      if (port.isEmpty()) {
        databaseSettings.getTextPort().requestFocus();
        return;
      }
      String username = databaseSettings.getTextUsername().getText().trim();
      if (username.isEmpty()) {
        databaseSettings.getTextUsername().requestFocus();
        return;
      }
      databaseSettings.getTextPassword().getPassword();
      String password = new String(databaseSettings.getTextPassword().getPassword()).trim();
      if (password.isEmpty()) {
        databaseSettings.getTextPassword().requestFocus();
        return;
      }
      String database = databaseSettings.getTextDatabase().getText().trim();
      if (database.isEmpty()) {
        databaseSettings.getTextDatabase().requestFocus();
        return;
      }
      saveTextField(host, port, username, password, database);
      String url =
          "jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&charset=utf8";
      new Thread(() -> {
        // Try to get a connection
        try {
          DriverManager.getConnection(url, username, password);
        } catch (SQLException se) {
          ApplicationManager.getApplication().invokeLater(() -> Bus
              .notify(new Notification("JpaSupport", "Error",
                  "Unable to connect to the database(" + se.getErrorCode() + "," + se.getSQLState() + "," + se
                      .getLocalizedMessage() + ")", NotificationType.ERROR)));
          log.error("Unable to connect to the database", se);
          return;
        }
        Properties properties = new Properties();
        properties.setProperty("driverClass", Driver.class.getName());
        properties.setProperty("writableUrl", url);
        properties.setProperty("writableUsername", username);
        properties.setProperty("writablePassword", password);
        FastJdbcConfig.load(properties);

        FastJdbc fastJdbc = new SimpleFastJdbc();
        Sql sql = SqlBuilder.newSelectBuilder(TableSchema.class)
            .select()
            .from()
            .where()
            .equal("tableSchema", database)
            .build();
        List<TableSchema> tableSchemaList;
        try {
          tableSchemaList = fastJdbc
              .find(sql.getSql(), TableSchema.class, sql.getArgs().toArray());
        } catch (SQLException se) {
          ApplicationManager.getApplication()
              .invokeLater(() -> Bus.notify(new Notification("JpaSupport", "Error",
                  se.getErrorCode() + "," + se.getSQLState() + "," + se.getLocalizedMessage(),
                  NotificationType.ERROR)));
          se.printStackTrace();
          return;
        }

        // Show automatic generator configuration window
        AutoGeneratorSettingsFrame.show(tableSchemaList);

        databaseSettingsFrame.dispose();// Release the database settings window
      }).start();
    });
  }

  private void saveTextField(String host, String port, String username, String password, String database) {
    PropertiesComponent applicationProperties = Holder.getApplicationProperties();
    applicationProperties.setValue(createKey("host"), host);
    applicationProperties.setValue(createKey("port"), port);
    applicationProperties.setValue(createKey("username"), username);
    applicationProperties
        .setValue(createKey("password"), Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8)));
    applicationProperties.setValue(createKey("database"), database);
  }

  private void initTextField(DatabaseSettings databaseSettings) {
    PropertiesComponent applicationProperties = Holder.getApplicationProperties();
    databaseSettings.getTextHost().setText(applicationProperties.getValue(createKey("host"), "localhost"));
    databaseSettings.getTextPort().setText(applicationProperties.getValue(createKey("port"), "3306"));
    databaseSettings.getTextUsername().setText(applicationProperties.getValue(createKey("username"), "root"));
    databaseSettings.getTextPassword()
        .setText(new String(Base64.getDecoder().decode(applicationProperties.getValue(createKey("password"), "")),
            StandardCharsets.UTF_8));
    databaseSettings.getTextDatabase().setText(applicationProperties.getValue(createKey("database"), ""));
  }
}
