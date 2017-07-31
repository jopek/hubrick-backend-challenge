package me.lxbluem.processor;

import me.lxbluem.model.Report;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;

public abstract class AbstractMultiKeyProcessor<T> {
  protected List<String> columnNames = emptyList();

  protected List<Selector> selectors;
  protected List<Aggregator> aggregators;

  public void addSelector(Selector selector) {
    validateSelector(selector);
    selectors.add(selector);
  }

  public void addAggregator(Aggregator aggregator) {
    validateAggregator(aggregator);
    aggregators.add(aggregator);
  }

  public Report process(List<T> entries) {
    Map<String, List<T>> groupedEntries = entries.stream()
        .collect(Collectors.groupingBy(
            this::getReportKey,
            Collectors.toList()
        ));

    Map<String, List<Number>> groupedEntriesProcessed = groupedEntries.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            this::aggregate
        ));

//    return Report.of(groupedEntriesProcessed, columnNames);
    return null;
  }

  private String getReportKey(T t) {
    return selectors.stream()
        .map(selector -> selector.function.apply(t))
        .collect(joining());
  }

  public abstract List<Number> aggregate(Map.Entry<String, List<T>> entries);

  private void validateSelector(Selector selector) {
    if (Objects.isNull(selector))
      throw new IllegalArgumentException("Selector must not be null");
    if (Objects.isNull(selector.function))
      throw new IllegalArgumentException("Selector's function must not be null");
  }

  private void validateAggregator(Aggregator aggregator) {
    if (Objects.isNull(aggregator))
      throw new IllegalArgumentException("Selector must not be null");
    if (Objects.isNull(aggregator.function))
      throw new IllegalArgumentException("Selector's function must not be null");
  }

  public class Selector {
    public Function<T, String> function;
    public String name;
  }

  public class Aggregator {
    public Function<T, Number> function;
    public String name;
  }
}
