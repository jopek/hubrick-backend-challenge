package me.lxbluem.processor;

import java.util.List;
import java.util.function.Function;

public class Aggregator<T> {
  public String name;
  public Function<List<T>, Number> function;

  public Aggregator(String name, Function<List<T>, Number> function) {
    this.name = name;
    this.function = function;
  }
}
