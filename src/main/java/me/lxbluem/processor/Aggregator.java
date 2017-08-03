package me.lxbluem.processor;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public class Aggregator<T> {
  public final String name;
  public final Function<List<T>, Serializable> function;

  public Aggregator(String name, Function<List<T>, Serializable> function) {
    this.name = name;
    this.function = function;
  }
}
