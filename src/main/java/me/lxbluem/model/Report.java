package me.lxbluem.model;

import java.util.List;
import java.util.Map;

public class Report {
  private final Map<String, Double> values;
  private final List<String> columnNames;

  private Report(Map<String, Double> values, List<String> columnNames) {
    this.values = values;
    this.columnNames = columnNames;
  }

  public static Report of(Map<String, Double> values, List<String> columnNames) {
    return new Report(values, columnNames);
  }

  public Map<String, Double> getValues() {
    return values;
  }

  public List<String> getColumnNames() {
    return columnNames;
  }
}
