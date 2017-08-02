package me.lxbluem;

import me.lxbluem.model.Report;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class ReportWriterTest {
  private ReportWriter writer;

  @Before
  public void setUp() throws Exception {
    writer = new ReportWriter();
  }

  @Test
  public void name() throws Exception {
    List<List<String>> values = new ArrayList<>();
    values.add(asList("line1", "33."));
    values.add(asList("line2", "21."));
    Report report = Report.of(values, asList("col a", "col b"));

    writer.write(report, "report.csv");
  }
}