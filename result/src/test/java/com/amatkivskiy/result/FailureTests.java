package com.amatkivskiy.result;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FailureTests {
  private static final String OOOH_YESSS = "Oooh! Yesss!";

  @Test
  public void testIsEmptyCorrect() throws Exception {
    Failure<Object, String> error = new Failure<>(OOOH_YESSS);
    assertThat(error.isEmpty(), is(false));

    error = new Failure<>(null);
    assertThat(error.isEmpty(), is(true));
  }

  @Test
  public void testIsSuccessCorrect() throws Exception {
    Failure<Object, String> error = new Failure<>(OOOH_YESSS);
    assertThat(error.isSuccess(), is(false));
  }

  @Test
  public void testToStringCorrect() throws Exception {
    Failure<Object, String> error = new Failure<>(OOOH_YESSS);
    assertThat(error.toString(), is("Failure[failure=Oooh! Yesss!]"));

    error = new Failure<>(null);
    assertThat(error.toString(), is("Failure[failure=null]"));
  }

  @Test
  public void testHashCodeCorrect() throws Exception {
    Failure<Object, String> error = new Failure<>(OOOH_YESSS);
    assertThat(error.hashCode(), is(OOOH_YESSS.hashCode()));

    error = new Failure<>(null);
    assertThat(error.hashCode(), is(0));
  }

  @Test
  public void testEqualsCorrect() throws Exception {
    Failure<Object, String> one = new Failure<>(OOOH_YESSS);
    Failure<Object, String> another = new Failure<>(OOOH_YESSS);

    assertThat(one.equals(another), is(true));
    assertThat(another.equals(one), is(true));

    //noinspection EqualsWithItself
    assertThat(one.equals(one), is(true));

    //noinspection EqualsBetweenInconvertibleTypes
    assertThat(one.equals(OOOH_YESSS), is(false));
  }
}
