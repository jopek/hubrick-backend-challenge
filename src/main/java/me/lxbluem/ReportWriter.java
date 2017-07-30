package me.lxbluem;

import me.lxbluem.model.Report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.stream.Collectors.toList;

public class ReportWriter {
  public void write(Report report, String filename) throws IOException {
    System.err.println(String.format("writing %s", filename));

    List<String> content = new ArrayList<>();
    content.add(getHeader(report));
    content.addAll(getContent(report));

    Files.write(Paths.get(filename), content, CREATE, WRITE);
  }

  private List<String> getContent(Report report) {
    return report.getValues()
        .entrySet()
        .stream()
        .map(entry -> String.format("%s,%.2f", entry.getKey(), entry.getValue()))
        .sorted()
        .collect(toList());
  }

  private String getHeader(Report report) {
    return report.getColumnNames()
        .stream()
        .collect(Collectors.joining(","));
  }
}
