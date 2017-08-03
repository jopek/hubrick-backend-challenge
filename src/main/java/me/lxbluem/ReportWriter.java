package me.lxbluem;

import me.lxbluem.model.Report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ReportWriter {
  public void write(Report report, String filename) throws IOException {
    System.err.println(String.format("writing %s", filename));

    List<String> content = new ArrayList<>();
    content.add(getHeader(report));
    content.addAll(getContent(report));

    Files.write(Paths.get(filename), content, CREATE, TRUNCATE_EXISTING, WRITE);
  }

  private String getHeader(Report report) {
    return report.getColumnNames()
        .stream()
        .collect(joining(","));
  }

  private List<String> getContent(Report report) {
    return report.getValues()
        .stream()
        .map(entry -> entry.stream().collect(joining(",")))
        .sorted()
        .collect(toList());
  }
}
