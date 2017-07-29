package me.lxbluem.processor;

import me.lxbluem.model.Report;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AverageProcessor<T> extends AbstractProcessor<T> {

  @Override
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
            this::getAverage
        ));

    return Report.of(groupedEntriesProcessed, columnNames);
  }

  private Double getAverage(Map.Entry<String, List<T>> entries) {
    return entries.getValue()
        .stream()
        .mapToDouble(value -> reportValueFunction.apply(value))
        .average()
        .orElse(0);
  }
}