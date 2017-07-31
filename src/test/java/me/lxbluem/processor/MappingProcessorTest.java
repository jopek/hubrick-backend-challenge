package me.lxbluem.processor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;

public class MappingProcessorTest {
  @Test
  public void llo() throws Exception {
    List<Integer> integers = asList(
        1, 2, 3, 4, 5, 6, 7, 8, 9
    );

    List<Function<Integer, Object>> ts = asList(
        i -> i,
        i -> (i % 2 == 0) ? "odd" : "even",
        i -> (i % 2 == 0) ? i * 3 : i * 2
    );

    integers.stream()
        .map(integer -> {
          List<Object> objects = new ArrayList<>();
          ts.forEach(a -> objects.add(a.apply(integer)));
          return objects;
        })
        .collect(Collectors.toList())
        .forEach(System.out::println);
  }

  @Test
  public void name() throws Exception {
    List<X> xes = asList(
        new X("a", 1, 1),
        new X("b", 1, 2),
        new X("c", 2, 1),
        new X("c", 2, 3),
        new X("c", 2, 4),
        new X("d", 3, 6),
        new X("d", 4, 7),
        new X("e", 4, 6),
        new X("e", 5, 3)
    );

    List<Function<X, Object>> functions = asList(
        x -> x.name,
        x -> x.val1 / 3
    );

    Map<List<Object>, List<X>> collect = xes.stream().collect(groupingBy(
        x -> getKey(functions, x),
        toList()
    ));

    collect.forEach((k, v) -> {
      System.out.printf("%s: %s\n", k, v);
    });

    Map<List<Object>, Double> objectMap = collect.entrySet()
        .stream()
        .collect(toMap(
            Map.Entry::getKey,
            entry -> entry.getValue()
                .stream()
                .mapToDouble(x -> x.val2 * 10.)
                .average()
                .orElse(0)
        ));

    objectMap
        .entrySet()
        .stream()
        .sorted((o1, o2) -> {
          String collect1 = o1.getKey().stream().map(Object::toString).collect(joining());
          String collect2 = o2.getKey().stream().map(Object::toString).collect(joining());
          return String.CASE_INSENSITIVE_ORDER.compare(collect1, collect2);
        })
        .forEach(e -> System.out.printf("%s: %s\n", e.getKey(), e.getValue()));

  }

  private List<Object> getKey(List<Function<X, Object>> functions, X x) {
    return functions.stream()
        .map(f -> f.apply(x))
        .collect(toList());
  }

  private class X {
    String name;
    int val1;
    int val2;

    X(String name, int val1, int val2) {
      this.name = name;
      this.val1 = val1;
      this.val2 = val2;
    }

    @Override
    public String toString() {
      return "X{" +
          "name='" + name + '\'' +
          ", val1=" + val1 +
          ", val2=" + val2 +
          '}';
    }
  }
}