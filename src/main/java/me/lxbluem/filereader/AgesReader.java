package me.lxbluem.filereader;

import me.lxbluem.filereader.model.AgeData;

import java.nio.file.Path;

public class AgesReader extends AbstractReader<AgeData> {

  public AgesReader(Path dataPath) {
    super(dataPath);
  }

  @Override
  protected Path dataFilePath() {
    return dataPath.resolve("ages.csv");
  }

  @Override
  protected AgeData mapToLocalStructure(String[] line) {
    AgeData ageData = new AgeData();
    ageData.age = Integer.parseInt(line[1]);
    ageData.name = line[0];
    return ageData;
  }

}
