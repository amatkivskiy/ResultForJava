package com.amatkivskiy.result;

/**
 * A functional interface that transforms one value into another.
 *
 * @param <T> the input value type
 * @param <V> the output value type
 */
public interface Transformer<T, V> {
  /**
   * Apply some transformation to the input value and return other value.
   *
   * @param value the input value
   * @return the output value
   */
  V apply(T value);
}
