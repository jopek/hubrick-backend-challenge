package me.lxbluem.processor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;

public class MappingListToListLearningTest {
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
        new X("a", 1, 2),
        new X("a", 2, 1),
        new X("a", 2, 3),
        new X("a", 2, 4),
        new X("a", 20, 4),
        new X("d", 3, 6),
        new X("d", 0, 7),
        new X("e", 4, 6),
        new X("e", 5, 3)
    );

    List<Function<X, Object>> keyFunctions = asList(
        x -> x.name,
        x -> x.val1 / 3
    );

    List<Function<List<X>, Object>> valueFunctions = asList(
        xs -> (long) xs.size(),
        xs -> xs.stream().mapToInt(x -> x.val1).max().orElse(0),
        xs -> xs.stream().mapToInt(x -> x.val1).min().orElse(0),
        xs -> xs.stream().mapToDouble(x -> x.val2 * 10.).average().orElse(0)
    );

    Map<List<Object>, List<X>> collect = xes.stream().collect(groupingBy(
        x -> getKeyList(keyFunctions, x),
        toList()
    ));

    collect.forEach((k, v) -> {
      System.out.printf("%s: %s\n", k, v);
    });

    Map<List<Object>, List<Object>> objectMap = collect.entrySet()
        .stream()
        .collect(toMap(
            Map.Entry::getKey,
            entry -> runValueFunctions(entry, valueFunctions)
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

  private List<Object> getKeyList(List<Function<X, Object>> functions, X x) {
    return functions.stream()
        .map(f -> f.apply(x))
        .collect(toList());
  }

  private List<Object> runValueFunctions(Map.Entry<List<Object>, List<X>> entry, List<Function<List<X>, Object>> valueFunctions) {
    return valueFunctions.stream()
        .map(f -> f.apply(entry.getValue()))
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