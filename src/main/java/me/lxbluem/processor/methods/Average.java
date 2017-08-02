package me.lxbluem.processor.methods;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Average<T> implements Method<T, Double> {

  private Function<T, Double> valueFunction;

  public Average(Function<T, Double> valueFunction) {
    this.valueFunction = valueFunction;
  }

  @Override
  public Double get(List<T> elements) {
    return elements
        .stream()
        .mapToDouble(value -> valueFunction.apply(value))
        .average()
        .orElse(0);
  }

  private void validateFunction(Function<T, Double> valueFunction) {
    if (Objects.nonNull(valueFunction))
      return;

    throw new IllegalArgumentException("the function must not be null");
  }

}