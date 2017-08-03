package me.lxbluem.processor;

import java.util.function.Function;

public class Selector<T> {
  public final String name;
  public final Function<T, String> function;

  public Selector(String name, Function<T, String> function) {
    this.name = name;
    this.function = function;
  }
}
