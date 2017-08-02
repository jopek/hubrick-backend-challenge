package me.lxbluem.processor;

import me.lxbluem.model.Report;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ProcessorTest {

  private class Thing {
    public String name;
    public int value;

    public Thing(String name, int value) {
      this.name = name;
      this.value = value;
    }
  }

  private Processor<Thing> thingProcessor = new Processor<>();

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

    thingProcessor.addSelector(new Selector<>("name", thing -> thing.name));
    thingProcessor.addAggregator(new Aggregator<>("count", List::size));
    thingProcessor.addAggregator(
        new Aggregator<>(
            "avg",
            thingList -> thingList.stream()
                .mapToDouble(t -> t.value)
                .average()
                .orElse(0))
    );

    Report report = thingProcessor.process(things);

    assertThat(report.getColumnNames(), CoreMatchers.hasItems("name", "count", "avg"));
    assertEquals(2, report.getValues().size());
    assertEquals(3, report.getValues().get(0).size());
    assertEquals(3, report.getValues().get(1).size());
  }
}