package me.lxbluem;

import me.lxbluem.filereader.AgesReader;
import me.lxbluem.filereader.DepartmentsReader;
import me.lxbluem.filereader.EmployeesReader;
import me.lxbluem.filereader.model.DepartmentData;
import me.lxbluem.filereader.model.EmployeeData;
import me.lxbluem.model.Department;
import me.lxbluem.model.Employee;
import me.lxbluem.model.Person;
import me.lxbluem.model.Person.Gender;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static me.lxbluem.model.Person.Gender.U;

public class EmployeeBuilder {

  private AgesReader agesReader;
  private EmployeesReader employeesReader;
  private DepartmentsReader departmentsReader;

  public EmployeeBuilder(AgesReader agesReader, EmployeesReader employeesReader, DepartmentsReader departmentsReader) {
    this.agesReader = agesReader;
    this.employeesReader = employeesReader;
    this.departmentsReader = departmentsReader;
  }

  public List<Employee> readAll() throws IOException {
    Map<Integer, String> departmentDataMap = getDepartmentMap();
    Map<String, Integer> ageDataMap = getAgeMap();
    return getEmployeeList(departmentDataMap, ageDataMap);
  }

  private Map<Integer, String> getDepartmentMap() throws IOException {
    List<DepartmentData> departmentData = departmentsReader.read();
    return departmentData
        .stream()
        .collect(toMap(d -> d.id, d -> d.name));
  }

  private Map<String, Integer> getAgeMap() throws IOException {
    return agesReader.read()
        .stream()
        .collect(toMap(a -> a.name, a -> a.age));
  }

  private List<Employee> getEmployeeList(Map<Integer, String> departmentDataMap, Map<String, Integer> ageDataMap) throws IOException {
    return employeesReader.read()
        .stream()
        .map(employeeData -> mapToEmployee(employeeData, ageDataMap, departmentDataMap))
        .collect(Collectors.toList());
  }

  private Employee mapToEmployee(EmployeeData employeeData, Map<String, Integer> ageDataMap, Map<Integer, String> departmentDataMap) {
    String name = employeeData.name;
    int departmentId = employeeData.departmentId;
    Gender gender = matchGender(employeeData.gender);
    String departmentName = Optional.ofNullable(departmentDataMap.get(departmentId)).orElse("unknown department");

    Person person = Person.of(name, ageDataMap.get(name), gender);
    Department department = Department.of(departmentId, departmentName);
    return Employee.of(person, employeeData.salary, department);
  }

  private Gender matchGender(String employeeDataGender) {
    try {
      return Gender.valueOf(employeeDataGender.toUpperCase());
    } catch (IllegalArgumentException e) {
      return U;
    }
  }


}
