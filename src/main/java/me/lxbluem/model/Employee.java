package me.lxbluem.model;

public class Employee {
  private final Person person;
  private final double salary;
  private final Department department;

  private Employee(Person person, float salary, Department department) {
    this.person = person;
    this.salary = salary;
    this.department = department;
  }

  public static Employee of(Person person, float salary, Department department) {
    return new Employee(person, salary, department);
  }

  public Person getPerson() {
    return person;
  }

  public double getSalary() {
    return salary;
  }

  public Department getDepartment() {
    return department;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Employee employee = (Employee) o;

    if (Double.compare(employee.salary, salary) != 0) return false;
    if (!person.equals(employee.person)) return false;
    return department.equals(employee.department);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = person.hashCode();
    temp = Double.doubleToLongBits(salary);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + department.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Employee{" +
        "person=" + person +
        ", salary=" + salary +
        ", department=" + department +
        '}';
  }
}
