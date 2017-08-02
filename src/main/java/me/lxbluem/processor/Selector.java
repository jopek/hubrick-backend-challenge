package me.lxbluem.processor;

import java.util.function.Function;

public class Selector<T> {
  public String name;
  public Function<T, String> function;

  public Selector(String name, Function<T, String> function) {
    this.name = name;
    this.function = function;
  }
}
