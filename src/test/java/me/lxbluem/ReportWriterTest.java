package me.lxbluem;

import me.lxbluem.model.Report;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;

public class ReportWriterTest {
  private ReportWriter writer;

  @Before
  public void setUp() throws Exception {
    writer = new ReportWriter();
  }

  @Test
  public void name() throws Exception {
    Map<String, Double> values = new HashMap<>();
    values.put("line1", 33.);
    values.put("line2", 21.);
    Report report = Report.of(values, asList("col a", "col b"));

    writer.write(report, "report.csv");
  }
}