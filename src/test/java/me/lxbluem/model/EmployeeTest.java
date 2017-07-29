package me.lxbluem.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EmployeeTest {
  @Test
  public void equals() throws Exception {
    Employee ref = Employee.of(Person.of("a", 30, Person.Gender.M), 6300f, Department.of(1, "dev"));

    assertEquals(
        ref,
        Employee.of(Person.of("a", 30, Person.Gender.M), 6300f, Department.of(1, "dev"))
    );

    assertNotEquals(
        ref,
        Employee.of(Person.of("a", 30, Person.Gender.M), 6301f, Department.of(1, "dev"))
    );

    assertNotEquals(
        ref,
        Employee.of(Person.of("a", 30, Person.Gender.M), 6300f, Department.of(2, "dev"))
    );

    assertNotEquals(
        ref,
        Employee.of(Person.of("a", 30, Person.Gender.M), 6300f, Department.of(1, "ops"))
    );

    assertNotEquals(
        ref,
        Employee.of(Person.of("x", 63, Person.Gender.F), 6300f, Department.of(1, "dev"))
    );
  }

}