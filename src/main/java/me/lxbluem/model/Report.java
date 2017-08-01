package me.lxbluem.model;

import java.util.List;
import java.util.Map;

public class Report {
  private Map<String, Double> values;
  private List<List<String>> llvalues;
  private List<String> columnNames;

  private Report(Map<String, Double> values, List<String> columnNames) {
    this.values = values;
    this.columnNames = columnNames;
  }

  private Report(List<List<String>> values, List<String> columnNames) {
    this.llvalues = values;
    this.columnNames = columnNames;
  }

  public static Report of(Map<String, Double> values, List<String> columnNames) {
    return new Report(values, columnNames);
  }

  public static Report of(List<List<String>> values, List<String> columnNames) {
    return new Report(values, columnNames);
  }

  public Map<String, Double> getValues() {
    return values;
  }

  public List<List<String>> getLlValues() {
    return llvalues;
  }

  public List<String> getColumnNames() {
    return columnNames;
  }
}
