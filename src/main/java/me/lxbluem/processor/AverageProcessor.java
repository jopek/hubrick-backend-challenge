package me.lxbluem.processor;

import java.util.List;
import java.util.Map;

public class AverageProcessor<T> extends AbstractProcessor<T> {

  @Override
  public Double aggregate(Map.Entry<String, List<T>> entries) {
    return entries.getValue()
        .stream()
        .mapToDouble(value -> reportValueFunction.apply(value))
        .average()
        .orElse(0);
  }
}