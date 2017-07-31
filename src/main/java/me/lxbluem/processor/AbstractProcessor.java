package me.lxbluem.processor;

import me.lxbluem.model.Report;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

  public abstract Double aggregate(Map.Entry<String, List<T>> entries);

  public Report process(List<T> entries) {
    Map<String, List<T>> groupedEntries = entries.stream()
        .collect(Collectors.groupingBy(
            reportKeyFunction,
            Collectors.toList()
        ));

    Map<String, Double> groupedEntriesProcessed = groupedEntries.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            this::aggregate
        ));

    return Report.of(groupedEntriesProcessed, columnNames);
  }
}
