package com.amatkivskiy.result;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SuccessTests {
  private static final String OOOH_YESSS = "Oooh! Yesss!";

  @Test
  public void testIsEmptyCorrect() throws Exception {
    Success<String, Object> success = new Success<>(OOOH_YESSS);
    assertThat(success.isEmpty(), is(false));

    success = new Success<>(null);
    assertThat(success.isEmpty(), is(true));
  }

  @Test
  public void testIsSuccessCorrect() throws Exception {
    Success<String, Object> success = new Success<>(OOOH_YESSS);
    assertThat(success.isSuccess(), is(true));
  }

  @Test
  public void testToStringCorrect() throws Exception {
    Success<String, Object> success = new Success<>(OOOH_YESSS);
    assertThat(success.toString(), is("Success[value=Oooh! Yesss!]"));

    success = new Success<>(null);
    assertThat(success.toString(), is("Success[value=null]"));
  }

  @Test
  public void testHashCodeCorrect() throws Exception {
    Success<String, Object> success = new Success<>(OOOH_YESSS);
    assertThat(success.hashCode(), is(OOOH_YESSS.hashCode()));

    success = new Success<>(null);
    assertThat(success.hashCode(), is(0));
  }

  @Test
  public void testEqualsCorrect() throws Exception {
    Success<String, Object> one = new Success<>(OOOH_YESSS);
    Success<String, Object> another = new Success<>(OOOH_YESSS);

    assertThat(one.equals(another), is(true));
    assertThat(another.equals(one), is(true));

    //noinspection EqualsWithItself
    assertThat(one.equals(one), is(true));

    //noinspection EqualsBetweenInconvertibleTypes
    assertThat(one.equals(OOOH_YESSS), is(false));
  }
}
