package me.lxbluem.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PersonTest {
  @Test
  public void equals() throws Exception {
    Person ref = Person.of("a", 30, Person.Gender.F);

    assertEquals(ref, Person.of("a", 30, Person.Gender.F));

    assertNotEquals(ref, Person.of("A", 30, Person.Gender.F));
    assertNotEquals(ref, Person.of("a", 30, Person.Gender.M));
    assertNotEquals(ref, Person.of("a", 31, Person.Gender.F));
  }

}