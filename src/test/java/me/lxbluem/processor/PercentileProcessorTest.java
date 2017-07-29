package me.lxbluem.processor;

import me.lxbluem.model.Report;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PercentileProcessorTest {

  private PercentileProcessor<Integer> integerPercentileProcessor;

  @Before
  public void setUp() throws Exception {
    integerPercentileProcessor = new PercentileProcessor<>();
  }


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
    String KEY = "median";

    List<Integer> list = Arrays.asList(-2, 0, 56, 68, 93, 82);
    integerPercentileProcessor.setReportKeyFunction(integer -> KEY);
    integerPercentileProcessor.setReportValueFunction(integer -> (double) integer);
    integerPercentileProcessor.setPercentile(50);

    Report report = integerPercentileProcessor.process(list);

    assertEquals(62, report.getValues().get(KEY), 0);
  }

  @Test
  public void process() throws Exception {
    String KEY = "percentile";
    Report report;

    List<Integer> list = Arrays.asList(15, 20, 35, 40, 50);
    integerPercentileProcessor.setReportKeyFunction(integer -> KEY);
    integerPercentileProcessor.setReportValueFunction(integer -> (double) integer);

    integerPercentileProcessor.setPercentile(0.1);
    report = integerPercentileProcessor.process(list);
    assertEquals(15.02, report.getValues().get(KEY), 0.01);

    integerPercentileProcessor.setPercentile(25);
    report = integerPercentileProcessor.process(list);
    assertEquals(20, report.getValues().get(KEY), 0.01);

    integerPercentileProcessor.setPercentile(50);
    report = integerPercentileProcessor.process(list);
    assertEquals(35, report.getValues().get(KEY), 0.01);

    integerPercentileProcessor.setPercentile(75);
    report = integerPercentileProcessor.process(list);
    assertEquals(40, report.getValues().get(KEY), 0.01);

    integerPercentileProcessor.setPercentile(99.99);
    report = integerPercentileProcessor.process(list);
    assertEquals(49.99, report.getValues().get(KEY), 0.01);
  }
}