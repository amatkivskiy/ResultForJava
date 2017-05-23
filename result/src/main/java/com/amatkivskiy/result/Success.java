package com.amatkivskiy.result;

public class Success<V, E> extends Result<V, E> {
  private V value;

  public Success(V value) {
    this.value = value;
  }

  @Override
  public V value() {
    return value;
  }

  @Override
  public E error() {
    return null;
  }

  @Override
  public boolean isSuccess() {
    return true;
  }

  @Override
  public boolean isEmpty() {
    return value() == null;
  }

  @Override
  public String toString() {
    return "Success[value=" + value + "]";
  }

  @Override
  public int hashCode() {
    return isEmpty() ? 0 : value().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    //noinspection unchecked
    Success<V, E> success = (Success<V, E>) o;

    return value != null ? value.equals(success.value) : success.value == null;
  }
}