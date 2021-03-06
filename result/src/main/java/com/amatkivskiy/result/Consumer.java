package com.amatkivskiy.result;

/**
 * A functional interface (callback) that accepts a single value.
 *
 * @param <T> the value type
 */
public interface Consumer<T> {
  /**
   * Consume the given value.
   */
  void accept(T value);
}
