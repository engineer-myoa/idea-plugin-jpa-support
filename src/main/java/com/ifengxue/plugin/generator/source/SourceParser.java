package com.ifengxue.plugin.generator.source;

import com.ifengxue.plugin.entity.Table;
import com.ifengxue.plugin.generator.config.GeneratorConfig;

/**
 * Source parser
 */
public interface SourceParser {

  String parse(GeneratorConfig config, Table table);
}
