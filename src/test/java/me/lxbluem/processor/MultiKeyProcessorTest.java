package me.lxbluem.processor;

import me.lxbluem.model.Report;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MultiKeyProcessorTest {

  private class Thing {
    public String name;
    public int value;

    public Thing(String name, int value) {
      this.name = name;
      this.value = value;
    }
  }

  private MultiKeyProcessor<Thing> thingMultiKeyProcessor = new MultiKeyProcessor<>();

  @Test
  public void name() throws Exception {
    List<Thing> things = asList(
        new Thing("a", 1),
        new Thing("a", 2),
        new Thing("a", 3),
        new Thing("a", 4),
        new Thing("a", 5),
        new Thing("a", 6),
        new Thing("b", 1),
        new Thing("b", 2),
        new Thing("b", 3),
        new Thing("b", 5),
        new Thing("b", 6)
    );

    thingMultiKeyProcessor.addSelector(new MultiKeyProcessor.Selector<>("name", thing -> thing.name));
    thingMultiKeyProcessor.addAggregator(new MultiKeyProcessor.Aggregator<>("count", List::size));
    thingMultiKeyProcessor.addAggregator(
        new MultiKeyProcessor.Aggregator<>(
            "avg",
            thingList -> thingList.stream()
                .mapToDouble(t -> t.value)
                .average()
                .orElse(0))
    );

    Report report = thingMultiKeyProcessor.process(things);

//    AllOf.allOf(eq("name"), eq("count"))

    assertThat(report.getColumnNames(), CoreMatchers.hasItems("name", "count", "avg"));
    assertEquals(2, report.getLlValues().size());
    assertEquals(3, report.getLlValues().get(0).size());
    assertEquals(3, report.getLlValues().get(1).size());
  }
}