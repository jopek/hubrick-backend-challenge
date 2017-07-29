package me.lxbluem.processor;

import me.lxbluem.model.Report;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PercentileProcessor<T> extends AbstractProcessor<T> {

  private double percentile;

  @Override
  public Report process(List<T> entries) {
    Map<String, List<T>> groupedEntries = entries.stream()
        .collect(Collectors.groupingBy(
            reportKeyFunction,
            Collectors.toList()
        ));

    Map<String, Double> groupedEntriesProcessed = groupedEntries.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            this::getPercentile
        ));

    return Report.of(groupedEntriesProcessed, columnNames);
  }

  // linear interpolation between closest ranks: https://en.wikipedia.org/wiki/Percentile#The_linear_interpolation_between_closest_ranks_method
  private Double getPercentile(Map.Entry<String, List<T>> entries) {
    List<T> sortedList = entries.getValue()
        .stream()
        .sorted(Comparator.comparingDouble(e -> reportValueFunction.apply(e)))
        .collect(Collectors.toList());

    if (sortedList.size() == 1) {
      return reportValueFunction.apply(sortedList.get(0));
    }


    // second variant, C = 1
    double rank = percentile / 100 * (sortedList.size() - 1) + 1;
    double rank_fraction = rank % 1;
    int rank_floor = (int) Math.floor(rank);
    int index = rank_floor - 1;

    Double lowerValue = reportValueFunction.apply(sortedList.get(index));
    Double upperValue = reportValueFunction.apply(sortedList.get(index + 1));
    return lowerValue + rank_fraction * (upperValue - lowerValue);
  }

  /**
   * @param percentile the percentile must be between 0 .. 100: 0 < percentile < 100. Otherwise an IllegalArgumentException is thrown.
   */
  public void setPercentile(double percentile) {
    validateValue(percentile);
    this.percentile = percentile;
  }

  private void validateValue(double percentile) {
    if (percentile > 0 && percentile < 100)
      return;

    String message = String.format("the percentile %.3f must be specified inside the range 0 < P < 100", percentile);
    throw new IllegalArgumentException(message);
  }

}