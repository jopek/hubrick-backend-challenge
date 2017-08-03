package me.lxbluem.model;

import me.lxbluem.model.Person.Gender;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PersonTest {
  @Test
  public void equals() throws Exception {
    Person ref = Person.of("a", 30, Gender.F);

    assertEquals(ref, Person.of("a", 30, Gender.F));

    assertNotEquals(ref, Person.of("A", 30, Gender.F));
    assertNotEquals(ref, Person.of("a", 30, Gender.M));
    assertNotEquals(ref, Person.of("a", 31, Gender.F));
  }

  @Test
  public void genderNames() throws Exception {
    Person ref = Person.of("a", 30, Gender.F);

    assertEquals("a", ref.getName());
    assertEquals(30, ref.getAge());
    assertEquals(Gender.F, ref.getGender());
    assertEquals("Female", ref.getGender().toString());
    assertEquals("F", ref.getGender().name());
  }

}