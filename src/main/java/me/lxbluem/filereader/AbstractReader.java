package me.lxbluem.filereader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public abstract class AbstractReader<T> {
  protected final Path dataPath;

  public AbstractReader(Path dataPath) {
    this.dataPath = dataPath;
    System.err.println(String.format("reading %s", dataFilePath()));
  }

  public List<T> read() throws IOException {
    return Files.readAllLines(dataFilePath()).stream()
        .filter(line -> !line.isEmpty())
        .map(line -> line.split(","))
        .map(this::mapToLocalStructure)
        .filter(Objects::nonNull)
        .collect(toList());
  }

  protected abstract Path dataFilePath();

  protected abstract T mapToLocalStructure(String[] line);
}
