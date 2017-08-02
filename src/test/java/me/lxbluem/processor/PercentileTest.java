package me.lxbluem.processor;

import me.lxbluem.processor.methods.Percentile;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PercentileTest {

  private Percentile<Integer> percentile;

  @Test
  public void median_pure() throws Exception {
    List<Integer> integers = Arrays.asList(-2, 0, 56, 68, 93, 82);
    integers.sort(Integer::compareTo);

    boolean isOdd = integers.size() % 2 == 1;
    int index = integers.size() / 2;

    if (isOdd) {
      System.out.println(integers.get(index));
    } else {
      Integer upper = integers.get(index);
      Integer lower = integers.get(index - 1);

      System.out.println((upper + lower) / 2);
    }

  }

  @Test
  public void median() throws Exception {
    List<Integer> list = Arrays.asList(-2, 0, 56, 68, 93, 82);

    percentile = new Percentile<>(50, integer -> (double) integer);
    Double percentileValue = percentile.get(list);

    assertEquals(62, percentileValue, 0);
  }

  @Test
  public void process() throws Exception {

    List<Integer> list = Arrays.asList(15, 20, 35, 40, 50);

    percentile = new Percentile<>(0.1, integer -> (double) integer);
    assertEquals(15.02, percentile.get(list), 0.01);

    percentile.setPercentile(25);
    assertEquals(20, percentile.get(list), 0.01);

    percentile.setPercentile(50);
    assertEquals(35, percentile.get(list), 0.01);

    percentile.setPercentile(75);
    assertEquals(40, percentile.get(list), 0.01);

    percentile.setPercentile(99.99);
    assertEquals(49.99, percentile.get(list), 0.01);
  }
}