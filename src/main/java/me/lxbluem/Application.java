package me.lxbluem;

import me.lxbluem.filereader.AgesReader;
import me.lxbluem.filereader.DepartmentsReader;
import me.lxbluem.filereader.EmployeesReader;
import me.lxbluem.model.Employee;
import me.lxbluem.model.Report;
import me.lxbluem.processor.*;
import me.lxbluem.processor.methods.Average;
import me.lxbluem.processor.methods.Percentile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {
  private EmployeeBuilder employeeBuilder;
  private List<Employee> employees;
  private Map<String, Report> reports = new HashMap<>();
  private ReportWriter reportWriter;

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("please supply only one argument, the data path");
      return;
    }

    ReportWriter reportWriter = new ReportWriter();
    EmployeeBuilder employeeBuilder = getEmployeeBuilder(args[0]);

    Application app = new Application(employeeBuilder, reportWriter);
    app.read();
    app.process();
    app.write();
  }

  private static EmployeeBuilder getEmployeeBuilder(String arg) {
    Path dataPath = Paths.get(arg);
    AgesReader agesReader = new AgesReader(dataPath);
    EmployeesReader employeesReader = new EmployeesReader(dataPath);
    DepartmentsReader departmentsReader = new DepartmentsReader(dataPath);

    return new EmployeeBuilder(agesReader, employeesReader, departmentsReader);
  }

  private Application(
      EmployeeBuilder employeeBuilder,
      ReportWriter reportWriter) {
    this.employeeBuilder = employeeBuilder;
    this.reportWriter = reportWriter;
  }

  private void read() {
    try {
      employees = employeeBuilder.readAll();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void process() {
    reports.put("income-by-department.csv", getEmployeeMedianIncome().process(employees));
    reports.put("income-95-by-department.csv", getEmployee95PercentileIncome().process(employees));
    reports.put("income-average-by-age-range.csv", getEmployeeAverageIncomeByAge().process(employees));
    reports.put("employee-age-by-department.csv", getEmployeeMedianAge().process(employees));
    reports.put("income-average-by-gender-and-department.csv", getEmployeeAverageIncomeByGenderAndDepartment().process(employees));
    reports.put("employee-count-by-gender-and-department.csv", getEmployeeCountByGenderAndDepartment().process(employees));
  }

  private void write() {
    reports.forEach((filename, report) -> {
      try {
        reportWriter.write(report, filename);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private Processor<Employee> getEmployeeMedianIncome() {
    Percentile<Employee> percentile = new Percentile<>(50, Employee::getSalary);
    Processor<Employee> processor = new Processor<>();
    processor.addSelector(new Selector<>("Department", employee -> employee.getDepartment().getName()));
    processor.addAggregator(new Aggregator<>("Median Income", percentile::get));
    return processor;
  }

  private Processor<Employee> getEmployee95PercentileIncome() {
    Percentile<Employee> percentile = new Percentile<>(95, Employee::getSalary);
    Processor<Employee> processor = new Processor<>();
    processor.addSelector(new Selector<>("Department", employee -> employee.getDepartment().getName()));
    processor.addAggregator(new Aggregator<>("Median Income", percentile::get));
    return processor;
  }

  private Processor<Employee> getEmployeeAverageIncomeByAge() {
    Average<Employee> average = new Average<>(Employee::getSalary);
    Processor<Employee> processor = new Processor<>();
    processor.addSelector(new Selector<>("Age Range", employee -> getAgeSteps(employee.getPerson().getAge())));
    processor.addAggregator(new Aggregator<>("Average Income", average::get));
    return processor;
  }

  private String getAgeSteps(int age) {
    int stepSize = 10;
    double quot = (double) age / stepSize;
    int floor = (int) Math.floor(quot) * stepSize;
    int ceil = (int) Math.ceil(quot) * stepSize;
    if (floor == ceil)
      ceil += stepSize;

    return String.format("%d - %d", floor, ceil);
  }

  private Processor<Employee> getEmployeeMedianAge() {
    Percentile<Employee> percentile = new Percentile<>(50, employee -> (double) employee.getPerson().getAge());
    Processor<Employee> processor = new Processor<>();
    processor.addSelector(new Selector<>("Department", employee -> employee.getDepartment().getName()));
    processor.addAggregator(new Aggregator<>("Median Age", percentile::get));
    return processor;
  }

  private Processor<Employee> getEmployeeAverageIncomeByGenderAndDepartment() {
    Average<Employee> average = new Average<>(Employee::getSalary);
    Processor<Employee> processor = new Processor<>();
    processor.addSelector(new Selector<>("Department", employee -> employee.getDepartment().getName()));
    processor.addSelector(new Selector<>("Gender", employee -> employee.getPerson().getGender().name()));
    processor.addAggregator(new Aggregator<>("Average Income", average::get));
    return processor;
  }

  private Processor<Employee> getEmployeeCountByGenderAndDepartment() {
    Processor<Employee> processor = new Processor<>();
    processor.addSelector(new Selector<>("Department", employee -> employee.getDepartment().getName()));
    processor.addSelector(new Selector<>("Gender", employee -> employee.getPerson().getGender().name()));
    processor.addAggregator(new Aggregator<>("Employee Count", List::size));
    return processor;
  }


}
