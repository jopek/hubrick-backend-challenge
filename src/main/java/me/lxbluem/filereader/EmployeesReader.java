package me.lxbluem.filereader;

import me.lxbluem.filereader.model.EmployeeData;

import java.nio.file.Path;

public class EmployeesReader extends AbstractReader<EmployeeData> {

  public EmployeesReader(Path dataPath) {
    super(dataPath);
  }

  @Override
  protected Path dataFilePath() {
    return dataPath.resolve("employees.csv");
  }

  @Override
  protected EmployeeData mapToLocalStructure(String[] line) {
    try {
      EmployeeData employeesData = new EmployeeData();
      employeesData.departmentId = Integer.parseInt(line[0]);
      employeesData.name = line[1];
      employeesData.gender = line[2];
      employeesData.salary = Float.parseFloat(line[3]);
      return employeesData;
    } catch (IndexOutOfBoundsException | NumberFormatException e) {
      return null;
    }

  }

}
