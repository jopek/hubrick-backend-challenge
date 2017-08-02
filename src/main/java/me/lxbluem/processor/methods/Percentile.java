package me.lxbluem.processor.methods;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Percentile<T> implements Method<T, Double> {

  private double percentile;

  private Function<T, Double> valueFunction;
  /**
   * @param percentile the percentile must be between 0 .. 100: 0 < percentile < 100. Otherwise an IllegalArgumentException is thrown.
   * @param valueFunction the function to be used for the passed in list of object's value. The value is then used for calculating the percentile and sorting.
   */
  public Percentile(double percentile, Function<T, Double> valueFunction) {
    validateValue(percentile);
    validateFunction(valueFunction);
    this.valueFunction = valueFunction;
    this.percentile = percentile;
  }

  public void setPercentile(double percentile) {
    validateValue(percentile);
    this.percentile = percentile;
  }

  // linear interpolation between closest ranks: https://en.wikipedia.org/wiki/Percentile#The_linear_interpolation_between_closest_ranks_method
  public Double get(List<T> entries) {
    List<T> sortedList = entries
        .stream()
        .sorted(Comparator.comparingDouble(e -> valueFunction.apply(e)))
        .collect(Collectors.toList());

    if (sortedList.size() == 1) {
      return valueFunction.apply(sortedList.get(0));
    }

    // second variant, C = 1
    double rank = percentile / 100 * (sortedList.size() - 1) + 1;
    double rank_fraction = rank % 1;
    int rank_floor = (int) Math.floor(rank);
    int index = rank_floor - 1;

    Double lowerValue = valueFunction.apply(sortedList.get(index));
    Double upperValue = valueFunction.apply(sortedList.get(index + 1));
    return lowerValue + rank_fraction * (upperValue - lowerValue);
  }

  private void validateValue(double percentile) {
    if (percentile > 0 && percentile < 100)
      return;

    String message = String.format("the percentile %.3f must be specified inside the range 0 < P < 100", percentile);
    throw new IllegalArgumentException(message);
  }

  private void validateFunction(Function<T, Double> valueFunction) {
    if (Objects.nonNull(valueFunction))
      return;

    throw new IllegalArgumentException("the function must not be null");
  }

}