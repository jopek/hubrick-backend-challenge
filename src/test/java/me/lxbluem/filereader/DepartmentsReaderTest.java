package me.lxbluem.filereader;

import me.lxbluem.filereader.model.DepartmentData;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DepartmentsReaderTest {
  private DepartmentsReader departmentsReader;

  @Before
  public void setUp() throws Exception {
    Path path = getDataPath();
    departmentsReader = new DepartmentsReader(path);
  }

  private Path getDataPath() throws URISyntaxException {
    URL resource = getClass().getClassLoader().getResource("data/");
    assert resource != null;
    return Paths.get(resource.toURI());
  }

  @Test
  public void read() throws Exception {
    List<DepartmentData> read = departmentsReader.read();

    String departmentNames = "ABCDE";

    for (int i = 0; i < read.size(); i++) {
      DepartmentData departmentData = read.get(i);
      assertEquals(i + 1, departmentData.id);
      assertEquals(String.valueOf(departmentNames.charAt(i)), departmentData.name);
    }
  }

}