package me.lxbluem.model;

import java.util.List;

public class Report {
  private List<List<String>> values;
  private List<String> columnNames;

  private Report(List<List<String>> values, List<String> columnNames) {
    this.values = values;
    this.columnNames = columnNames;
  }

  public static Report of(List<List<String>> values, List<String> columnNames) {
    return new Report(values, columnNames);
  }

  public List<List<String>> getValues() {
    return values;
  }

  public List<String> getColumnNames() {
    return columnNames;
  }
}
