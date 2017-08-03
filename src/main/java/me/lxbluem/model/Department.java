package me.lxbluem.model;

public class Department {
  private final int id;
  private final String name;

  public final static String DEFAULT_NAME = "Unknown Department";

  private Department(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public static Department of(int id, String name) {
    return new Department(id, name);
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Department that = (Department) o;

    if (id != that.id) return false;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + name.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Department{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
