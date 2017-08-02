package me.lxbluem.processor;

import me.lxbluem.processor.methods.Average;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class AverageProcessorTest {

  private Average<Integer> average;

  @Test
  public void grouped_average() throws Exception {
    average = new Average<>(integer -> (double) integer);
    assertEquals(51.111, average.get(asList(70, 10, 20, 20, 20, 20, 100, 100, 100)), 0.001);
    assertEquals(2, average.get(asList(1, 2, 3)), 0.001);
    assertEquals(1.5, average.get(asList(1, 2)), 0.001);
    assertEquals(0, average.get(asList(-100, 100)), 0.001);
  }

}