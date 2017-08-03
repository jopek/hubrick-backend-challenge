package me.lxbluem.processor;

import me.lxbluem.model.Report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Processor<T> {

  private List<Selector<T>> selectors = new ArrayList<>();
  private List<Aggregator<T>> aggregators = new ArrayList<>();

  public Report process(List<T> entries) {
    Map<List<Object>, List<T>> groupedEntries = entries.stream()
        .collect(Collectors.groupingBy(
            this::getKeyList,
            toList()
        ));

    Map<List<Object>, List<Serializable>> aggregated = groupedEntries.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            this::runAggregationFunctions
        ));

    List<List<String>> reportContent = aggregated.entrySet()
        .stream()
        .map(x -> {
          List<String> keys = x.getKey().stream().map(Object::toString).collect(toList());
          List<String> aggregations = x.getValue().stream().map(Object::toString).collect(toList());
          ArrayList<String> strings = new ArrayList<>();
          strings.addAll(keys);
          strings.addAll(aggregations);
          return strings;
        })
        .collect(toList());

    List<String> columnNames = new ArrayList<>();
    columnNames.addAll(getSelectorNames());
    columnNames.addAll(getAggregatorNames());

    return Report.of(reportContent, columnNames);
  }

  public void addSelector(Selector<T> selector) {
    validateSelector(selector);
    selectors.add(selector);
  }

  public void addAggregator(Aggregator<T> aggregator) {
    validateAggregator(aggregator);
    aggregators.add(aggregator);
  }

  private List<Object> getKeyList(T key) {
    return selectors.stream()
        .map(f -> f.function.apply(key))
        .collect(toList());
  }

  private List<Serializable> runAggregationFunctions(Map.Entry<List<Object>, List<T>> entries) {
    return aggregators.stream()
        .map(aggregator -> aggregator.function.apply(entries.getValue()))
        .collect(toList());
  }

  private List<String> getSelectorNames() {
    return selectors.stream().map(s -> s.name).collect(toList());
  }

  private List<String> getAggregatorNames() {
    return aggregators.stream().map(s -> s.name).collect(toList());
  }

  private void validateSelector(Selector selector) {
    if (Objects.isNull(selector))
      throw new IllegalArgumentException("Selector must not be null");
    if (Objects.isNull(selector.function))
      throw new IllegalArgumentException("Selector's function must not be null");
  }

  private void validateAggregator(Aggregator aggregator) {
    if (Objects.isNull(aggregator))
      throw new IllegalArgumentException("Aggregator must not be null");
    if (Objects.isNull(aggregator.function))
      throw new IllegalArgumentException("Aggregator's function must not be null");
  }

}
