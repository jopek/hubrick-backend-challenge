package me.lxbluem.filereader.model;

public class EmployeeData {
  public int departmentId;
  public String name;
  public String gender;
  public float salary;

  public EmployeeData() {
  }

  public EmployeeData(int departmentId, String name, String gender, float salary) {
    this.departmentId = departmentId;
    this.name = name;
    this.gender = gender;
    this.salary = salary;
  }
}
