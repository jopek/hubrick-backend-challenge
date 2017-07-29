package me.lxbluem.filereader;

import me.lxbluem.filereader.model.DepartmentData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DepartmentsReader extends AbstractReader<DepartmentData> {

  public DepartmentsReader(Path dataPath) {
    super(dataPath);
  }

  @Override
  public List<DepartmentData> read() throws IOException {
    List<DepartmentData> departments = new ArrayList<>();

    AtomicInteger depId = new AtomicInteger(1);
    Files.readAllLines(dataFilePath())
        .stream()
        .sorted()
        .filter(line -> !line.isEmpty())
        .forEach(name -> {
          DepartmentData data = new DepartmentData();
          data.id = depId.getAndIncrement();
          data.name = name;
          departments.add(data);
        });

    return departments;
  }

  @Override
  protected Path dataFilePath() {
    return dataPath.resolve("departments.csv");
  }

  @Override
  protected DepartmentData mapToLocalStructure(String[] strings) {
    return null;
  }


}
