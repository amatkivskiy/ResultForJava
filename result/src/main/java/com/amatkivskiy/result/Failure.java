package com.amatkivskiy.result;

public class Failure<V, E> extends Result<V, E> {
  private E error;

  public Failure(E error) {
    this.error = error;
  }

  @Override
  public V value() {
    return null;
  }

  @Override
  public E error() {
    return error;
  }

  @Override
  public boolean isSuccess() {
    return false;
  }

  @Override
  public boolean isEmpty() {
    return error() == null;
  }

  @Override
  public String toString() {
    return "Failure[failure=" + error + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Failure failure = (Failure) o;

    return error != null ? error.equals(failure.error) : failure.error == null;
  }

  @Override
  public int hashCode() {
    return error != null ? error.hashCode() : 0;
  }
}