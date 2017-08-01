package me.lxbluem.processor;

import me.lxbluem.model.Report;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MultiKeyProcessor<T> {

  private List<Selector<T>> selectors = new ArrayList<>();
  private List<Aggregator<T>> aggregators = new ArrayList<>();

  public void addSelector(Selector<T> selector) {
    validateSelector(selector);
    selectors.add(selector);
  }

  public void addAggregator(Aggregator<T> aggregator) {
    validateAggregator(aggregator);
    aggregators.add(aggregator);
  }

  public Report process(List<T> entries) {
    Map<List<Object>, List<T>> groupedEntries = entries.stream()
        .collect(Collectors.groupingBy(
            this::getKeyList,
            toList()
        ));

    Map<List<Object>, List<Number>> report = groupedEntries.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            this::aggregate
        ));

    List<String> columnNames = new ArrayList<>();
    columnNames.addAll(getSelectorNames());
    columnNames.addAll(getAggregatorNames());

    List<List<String>> values = report.entrySet()
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

    return Report.of(values, columnNames);
  }

  private List<String> getAggregatorNames() {
    return aggregators.stream().map(s->s.name).collect(toList());
  }

  private List<String> getSelectorNames() {
    return selectors.stream().map(s->s.name).collect(toList());
  }

  private List<Object> getKeyList(T key) {
    return selectors.stream()
        .map(f -> f.function.apply(key))
        .collect(toList());
  }

  private List<Number> aggregate(Map.Entry<List<Object>, List<T>> entries) {
    return aggregators.stream()
        .map(aggregator -> aggregator.function.apply(entries.getValue()))
        .collect(toList());
  }

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

  public static class Selector<T> {
    public String name;
    public Function<T, String> function;

    public Selector(String name, Function<T, String> function) {
      this.name = name;
      this.function = function;
    }
  }

  public static class Aggregator<T> {
    public String name;
    public Function<List<T>, Number> function;

    public Aggregator(String name, Function<List<T>, Number> function) {
      this.name = name;
      this.function = function;
    }
  }
}
