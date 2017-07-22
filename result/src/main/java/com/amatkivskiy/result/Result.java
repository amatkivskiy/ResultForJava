package com.amatkivskiy.result;

public abstract class Result<V, E> {
  /**
   * @return value if {@link #isSuccess()} returns true, null otherwise.
   */
  public abstract V value();

  /**
   * @return error if {@link #isSuccess()} returns false, null otherwise.
   */
  public abstract E error();

  /**
   * @return true if successful, false otherwise.
   */
  public abstract boolean isSuccess();

  /**
   * {@link Result} can be also empty.
   *
   * @return true if {@link #value()} or {@link #error()} returns null;
   */
  public abstract boolean isEmpty();

  /**
   * @return true if successful non empty @{@link Result}, false otherwise.
   */
  public boolean isSuccessfulNonEmpty() {
    return isSuccess() && !isEmpty();
  }

  // Syntactic sugar

  /**
   * Consumes successful result, skips call otherwise.
   *
   * @return current {@link Result}.
   */
  public Result<V, E> onSuccess(Consumer<V> consumer) {
    if (isSuccess()) consumer.accept(value());

    return this;
  }

  /**
   * Consumes failure result, skips call otherwise.
   *
   * @return current {@link Result}.
   */
  public Result<V, E> onFailure(Consumer<E> consumer) {
    if (!isSuccess()) consumer.accept(error());

    return this;
  }

  /**
   * Transforms successful value of this result, skips call otherwise.
   *
   * @param transformer function that transforms {@link #value()}.
   * @param <P> new successful type.
   * @return new {@link Result} whit P value.
   */
  public <P> Result<P, E> map(Transformer<V, P> transformer) {
    if (isSuccess()) {
      return Result.success(transformer.apply(value()));
    } else {
      return Result.failure(error());
    }
  }

  /**
   * Transforms current {@link Result} into completely new {@link Result}.
   *
   * @param transformer function that transforms {@link Result}.
   * @param <P> new successful type.
   * @param <T> new failure type.
   * @return new {@link Result}.
   */
  public <P, T> Result<P, T> flatMap(Transformer<Result<V, E>, Result<P, T>> transformer) {
    return transformer.apply(this);
  }

  /**
   * Returns successful value or fallback value otherwise.
   *
   * @param fallback value.
   * @return value.
   */
  public V or(V fallback) {
    if (isSuccess()) {
      return value();
    } else {
      return fallback;
    }
  }

  /**
   * Constructs failure result.
   *
   * @param error error value.
   * @param <V> type of successful value.
   * @param <E> type of failure value.
   * @return new {@link Result}.
   */
  public static <V, E> Result<V, E> failure(E error) {
    return new Failure<>(error);
  }

  /**
   * Constructs successful result.
   *
   * @param value successful value.
   * @param <V> type of successful value.
   * @param <E> type of failure value.
   * @return new {@link Result}.
   */
  public static <V, E> Result<V, E> success(V value) {
    return new Success<>(value);
  }

  /**
   * Creates {@link Result} with {@link Function} return value or with error value if {@link Function} fails.
   *
   * @param suspect function to be called.
   * @param error custom error to fail.
   * @param <V> type of successful value.
   * @param <E> type of failure value.
   * @return new {@link Result}.
   */
  public static <V, E> Result<V, E> orFailWith(Function<V> suspect, E error) {
    try {
      return success(suspect.call());
    } catch (Exception exception) {
      return new Failure<>(error);
    }
  }

  /**
   * Creates {@link Result} with {@link Function} return value or with fallbackValue value if {@link Function} fails.
   *
   * @param suspect function to be called.
   * @param fallbackValue value to be used of {@link Function} fails.
   * @param <V> type of successful value.
   * @param <E> type of failure value.
   * @return new {@link Result}.
   */
  public static <V, E> Result<V, E> orDefault(Function<V> suspect, V fallbackValue) {
    try {
      return success(suspect.call());
    } catch (Exception exception) {
      return success(fallbackValue);
    }
  }

  /**
   * Constructs successful {@link Result} with provided value.
   *
   * @param value successful value.
   * @param <V> type of successful value.
   * @param <E> type of failure value.
   * @return new {@link Result}.
   */
  public static <V, E> Result<V, E> of(V value) {
    return Success.success(value);
  }

  /**
   * Creates {@link Result} with {@link Function} return value or with {@link Exception} thrown during {@link Function}
   * call.
   *
   * @param suspect function to be called.
   * @param <V> type of successful value.
   * @param <E> type of failure value.
   * @return new {@link Result}.
   */
  public static <V, E extends Exception> Result<V, E> of(Function<V> suspect) {
    try {
      return success(suspect.call());
    } catch (Exception exception) {
      //      noinspection unchecked
      return new Failure<>((E) exception);
    }
  }
}
