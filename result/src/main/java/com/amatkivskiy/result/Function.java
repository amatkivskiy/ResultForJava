package com.amatkivskiy.result;

/**
 * A functional interface that returns another value and allows throwing a checked exception.
 *
 * @param <T> the output value type
 */
public interface Function<T> {
  /**
   * Runs the action and optionally throws a checked exception.
   */
  T call() throws Exception;
}
