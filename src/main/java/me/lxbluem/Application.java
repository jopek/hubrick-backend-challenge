package me.lxbluem;

import me.lxbluem.filereader.AgesReader;
import me.lxbluem.filereader.DepartmentsReader;
import me.lxbluem.filereader.EmployeesReader;
import me.lxbluem.model.Employee;
import me.lxbluem.model.Report;
import me.lxbluem.processor.AverageProcessor;
import me.lxbluem.processor.PercentileProcessor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {
  private EmployeeBuilder employeeBuilder;
  private List<Employee> employees;
  private Map<String, Report> reports = new HashMap<>();
  private ReportWriter reportWriter;
  private final AverageProcessor<Employee> employeeAverageProcessor;
  private final PercentileProcessor<Employee> employeePercentileProcessor;

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("please supply only one argument, the data path");
      return;
    }

    ReportWriter reportWriter = new ReportWriter();
    AverageProcessor<Employee> employeeAverageProcessor = new AverageProcessor<>();
    PercentileProcessor<Employee> employeePercentileProcessor = new PercentileProcessor<>();
    EmployeeBuilder employeeBuilder = getEmployeeBuilder(args[0]);

    Application app = new Application(employeeBuilder, reportWriter, employeeAverageProcessor, employeePercentileProcessor);
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
      ReportWriter reportWriter,
      AverageProcessor<Employee> employeeAverageProcessor,
      PercentileProcessor<Employee> employeePercentileProcessor) {
    this.employeeBuilder = employeeBuilder;
    this.reportWriter = reportWriter;
    this.employeeAverageProcessor = employeeAverageProcessor;
    this.employeePercentileProcessor = employeePercentileProcessor;
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

  private PercentileProcessor<Employee> getEmployeeMedianIncome() {
    PercentileProcessor<Employee> percentileProcessor = employeePercentileProcessor;
    percentileProcessor.setColumnNames(Arrays.asList("Department", "Median Income"));
    percentileProcessor.setReportKeyFunction(employee -> employee.getDepartment().getName());
    percentileProcessor.setReportValueFunction(Employee::getSalary);
    percentileProcessor.setPercentile(50);
    return percentileProcessor;
  }

  private PercentileProcessor<Employee> getEmployee95PercentileIncome() {
    PercentileProcessor<Employee> percentileProcessor = employeePercentileProcessor;
    percentileProcessor.setColumnNames(Arrays.asList("Department", "95 Percentile Income"));
    percentileProcessor.setReportKeyFunction(employee -> employee.getDepartment().getName());
    percentileProcessor.setReportValueFunction(Employee::getSalary);
    percentileProcessor.setPercentile(95);
    return percentileProcessor;
  }

  private AverageProcessor<Employee> getEmployeeAverageIncomeByAge() {
    AverageProcessor<Employee> employeeAverageAgeProcessor = employeeAverageProcessor;
    employeeAverageAgeProcessor.setColumnNames(Arrays.asList("Age Range", "Average Income"));
    employeeAverageAgeProcessor.setReportKeyFunction(employee -> getAgeSteps(employee.getPerson().getAge()));
    employeeAverageAgeProcessor.setReportValueFunction(Employee::getSalary);
    return employeeAverageAgeProcessor;
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

  private PercentileProcessor<Employee> getEmployeeMedianAge() {
    PercentileProcessor<Employee> percentileProcessor = employeePercentileProcessor;
    percentileProcessor.setColumnNames(Arrays.asList("Department", "Median Age"));
    percentileProcessor.setReportKeyFunction(employee -> employee.getDepartment().getName());
    percentileProcessor.setReportValueFunction(employee -> (double) employee.getPerson().getAge());
    percentileProcessor.setPercentile(50);
    return percentileProcessor;
  }

}
