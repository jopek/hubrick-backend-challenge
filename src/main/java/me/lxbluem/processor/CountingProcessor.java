package me.lxbluem.processor;

import java.util.List;
import java.util.Map;

public class CountingProcessor<T> extends AbstractProcessor<T> {

  @Override
  public Double aggregate(Map.Entry<String, List<T>> entries) {
    return (double) entries.getValue().size();
  }
}