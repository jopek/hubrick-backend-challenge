package me.lxbluem;

import me.lxbluem.filereader.AgesReader;
import me.lxbluem.filereader.DepartmentsReader;
import me.lxbluem.filereader.EmployeesReader;
import me.lxbluem.filereader.model.AgeData;
import me.lxbluem.filereader.model.DepartmentData;
import me.lxbluem.filereader.model.EmployeeData;
import me.lxbluem.model.Department;
import me.lxbluem.model.Employee;
import me.lxbluem.model.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class EmployeeBuilderTest {
  private EmployeeBuilder employeeBuilder;

  @Mock
  private DepartmentsReader departmentsReader;
  @Mock
  private AgesReader agesReader;
  @Mock
  private EmployeesReader employeesReader;

  @Before
  public void setUp() throws Exception {
    employeeBuilder = new EmployeeBuilder(agesReader, employeesReader, departmentsReader);
  }

  @Test
  public void dataPath_supplied() throws Exception {
    List<AgeData> ageData = Arrays.asList(
        new AgeData(23, "a"),
        new AgeData(32, "b"),
        new AgeData(42, "universe")
    );
    when(agesReader.read()).thenReturn(ageData);

    List<EmployeeData> employeeData = Arrays.asList(
        new EmployeeData(1, "a", "m", 1700f),
        new EmployeeData(2, "b", "f", 2700f),
        new EmployeeData(1, "universe", "t", Float.MAX_VALUE)
    );
    when(employeesReader.read()).thenReturn(employeeData);

    List<DepartmentData> departmentData = Arrays.asList(
        new DepartmentData(1, "office"),
        new DepartmentData(2, "sales"),
        new DepartmentData(3, "bar")
    );
    when(departmentsReader.read()).thenReturn(departmentData);


    List<Employee> employees = employeeBuilder.readAll();
    verify(agesReader).read();
    verify(departmentsReader).read();
    verify(employeesReader).read();

    assertEquals(3, employees.size());

    employees.forEach(System.out::println);

    assertTrue(employees.contains(
        Employee.of(Person.of("a", 23, Person.Gender.M), 1700f, Department.of(1, "office"))
    ));

    assertTrue(employees.contains(
        Employee.of(Person.of("b", 32, Person.Gender.F), 2700f, Department.of(2, "sales"))
    ));

    assertTrue(employees.contains(
        Employee.of(Person.of("universe", 42, Person.Gender.U), Float.MAX_VALUE, Department.of(1, "office"))
    ));
  }
}