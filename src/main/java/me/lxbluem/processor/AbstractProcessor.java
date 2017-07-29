package me.lxbluem.processor;

import me.lxbluem.model.Report;

import java.util.List;
import java.util.function.Function;

import static java.util.Collections.emptyList;

public abstract class AbstractProcessor<T> {
  protected List<String> columnNames = emptyList();
  protected Function<T, String> reportKeyFunction;
  protected Function<T, Double> reportValueFunction;

  public void setColumnNames(List<String> columnNames) {
    this.columnNames = columnNames;
  }

  public void setReportValueFunction(Function<T, Double> reportValueFunction) {
    this.reportValueFunction = reportValueFunction;
  }

  public void setReportKeyFunction(Function<T, String> reportKeyFunction) {
    this.reportKeyFunction = reportKeyFunction;
  }

  public abstract Report process(List<T> entries);
}
