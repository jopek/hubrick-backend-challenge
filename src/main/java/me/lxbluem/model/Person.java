package me.lxbluem.model;

public class Person {
  private String name;
  private int age;

  private Gender gender;

  private Person(String name, int age, Gender gender) {
    this.name = name;
    this.age = age;
    this.gender = gender;
  }

  public static Person of(String name, int age, Gender gender) {
    return new Person(name, age, gender);
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public Gender getGender() {
    return gender;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Person person = (Person) o;

    if (age != person.age) return false;
    if (!name.equals(person.name)) return false;
    return gender == person.gender;
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + age;
    result = 31 * result + gender.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Person{" +
        "name='" + name + '\'' +
        ", age=" + age +
        ", gender=" + gender +
        '}';
  }

  public enum Gender {
    F, M, U
  }
}
