package me.lxbluem.processor;

import me.lxbluem.model.Report;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AverageProcessorTest {

  private AverageProcessor<Integer> integerAverageProcessor;

  @Before
  public void setUp() throws Exception {
    integerAverageProcessor = new AverageProcessor<>();
  }

  @Test
  public void grouped_average() throws Exception {
    List<Integer> integers = asList(1, 2, 3, 20, 20, 20, 100, 100, 100);
    integerAverageProcessor.setReportKeyFunction((Integer integer) -> {
      int stepSize = 5;
      double quot = (double) integer / stepSize;
      int floor = Math.floorDiv(integer, stepSize) * stepSize;
      int ceil = (int) Math.ceil(quot) * stepSize;
      if (floor == ceil)
        ceil += stepSize;

      return String.format("%d - %d", floor, ceil);
    });
    integerAverageProcessor.setReportValueFunction(integer -> (double) integer);

    Report process = integerAverageProcessor.process(integers);

    assertTrue(process.getValues().containsKey("0 - 5"));
    assertEquals(2.0, process.getValues().get("0 - 5"), 0.001);
    assertTrue(process.getValues().containsKey("20 - 25"));
    assertEquals(20.0, process.getValues().get("20 - 25"), 0.001);
    assertTrue(process.getValues().containsKey("100 - 105"));
    assertEquals(100.0, process.getValues().get("100 - 105"), 0.001);
  }

}