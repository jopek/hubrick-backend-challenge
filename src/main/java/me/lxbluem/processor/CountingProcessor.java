package me.lxbluem.processor;

import me.lxbluem.model.Report;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CountingProcessor<T> extends AbstractProcessor<T> {

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
            listEntry -> (double) listEntry.getValue().size()
        ));

    return Report.of(groupedEntriesProcessed, columnNames);
  }

}