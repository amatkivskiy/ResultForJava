package com.amatkivskiy.result;

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ResultTests {
  private static final String OOOH_NOOO = "Oooh! Nooo!";
  private static final String OOOH_YEAH = "Oooh! Yeah!";

  @Test public void testResultErrorFromExceptionFunctionCorrect() throws Exception {
    Result<?, NullPointerException> error = Result.failure(new NullPointerException(OOOH_NOOO));

    assertThat(error.isSuccess(), is(false));
    assertThat(error.value(), is(nullValue()));
    assertThat(error.error(), is(notNullValue()));
    //noinspection ThrowableResultOfMethodCallIgnored
    assertThat(error.error()
                   .getMessage(), is(OOOH_NOOO));
    assertThat(error.error(), isA(NullPointerException.class));
  }

  @Test public void testResultErrorFromCustomTypeFunctionCorrect() throws Exception {
    Result<Object, String> error = Result.failure(OOOH_NOOO);

    assertThat(error.isSuccess(), is(false));
    assertThat(error.value(), is(nullValue()));
    assertThat(error.error(), is(notNullValue()));
    assertThat(error.error(), is(OOOH_NOOO));
  }

  @Test public void testResultOfFunctionCorrect() throws Exception {
    //noinspection unchecked
    DefaultThrowerImpl<IllegalStateException> thrower =
        (DefaultThrowerImpl<IllegalStateException>) Mockito.mock(DefaultThrowerImpl.class);

    Result<String, IllegalStateException> result = Result.success("one");

    assertThat(result.isSuccess(), is(true));
    assertThat(result.value(), is(notNullValue()));
    assertThat(result.value(), is("one"));
    assertThat(result.error(), is(nullValue()));

    verifyZeroInteractions(thrower);
  }

  @Test public void testResultOfCorrect() throws Exception {
    Result<String, Exception> result = Result.of(new Function<String>() {
      @Override public String call() throws Exception {
        return OOOH_YEAH;
      }
    });

    assertThat(result.isSuccess(), is(true));
    assertThat(result.value(), is(notNullValue()));
    assertThat(result.isEmpty(), is(false));
    assertThat(result.value(), is(OOOH_YEAH));
    assertThat(result.error(), is(nullValue()));
  }

  @Test public void testResultOfFailureCorrect() throws Exception {
    Result<String, Exception> result = Result.of(new Function<String>() {
      @Override public String call() throws Exception {
        // Just throw IllegalStateException
        throw new IllegalStateException(OOOH_NOOO);
      }
    });

    assertThat(result.isSuccess(), is(false));
    assertThat(result.value(), is(nullValue()));
    assertThat(result.isEmpty(), is(false));
    assertThat(result.error(), is(notNullValue()));
    assertThat(result.error(), instanceOf(IllegalStateException.class));
    assertThat(result.error()
                   .getMessage(), is(OOOH_NOOO));
  }

  @Test public void testResultOrFailCorrect() throws Exception {
    Result<String, String> result = Result.orFailWith(new Function<String>() {
      @Override public String call() throws Exception {
        // Just throw IllegalStateException
        throw new IllegalStateException(OOOH_NOOO);
      }
    }, OOOH_NOOO);

    assertThat(result.isSuccess(), is(false));
    assertThat(result.value(), is(nullValue()));
    assertThat(result.isEmpty(), is(false));
    assertThat(result.error(), is(notNullValue()));
    assertThat(result.error(), is(OOOH_NOOO));
  }

  @Test public void testResultOrFailNullErrorCorrect() throws Exception {
    Result<String, String> result = Result.orFailWith(new Function<String>() {
      @Override public String call() throws Exception {
        return OOOH_YEAH;
      }
    }, null);

    assertThat(result.isSuccess(), is(true));
    assertThat(result.isEmpty(), is(false));
    assertThat(result.value(), is(OOOH_YEAH));
    assertThat(result.error(), is(nullValue()));
  }

  @Test public void testResultOrDefaultCorrect() throws Exception {
    Result<String, String> result = Result.orDefault(new Function<String>() {
      @Override public String call() throws Exception {
        return OOOH_YEAH;
      }
    }, OOOH_NOOO);

    assertThat(result.isSuccess(), is(true));
    assertThat(result.isEmpty(), is(false));
    assertThat(result.error(), is(nullValue()));
    assertThat(result.value(), is(OOOH_YEAH));
  }

  @Test public void testResultOrDefaultReturnDefault() throws Exception {
    Result<String, String> result = Result.orDefault(new Function<String>() {
      @Override public String call() throws Exception {
        throw new NullPointerException(OOOH_NOOO);
      }
    }, OOOH_YEAH);

    assertThat(result.isSuccess(), is(true));
    assertThat(result.isEmpty(), is(false));
    assertThat(result.error(), is(nullValue()));
    assertThat(result.value(), is(OOOH_YEAH));
  }

  @Test public void testResultOrDefaultIsNullCorrect() throws Exception {
    Result<String, String> result = Result.orDefault(new Function<String>() {
      @Override public String call() throws Exception {
        throw new NullPointerException(OOOH_NOOO);
      }
    }, null);

    assertThat(result.isSuccess(), is(true));
    assertThat(result.isEmpty(), is(true));
    assertThat(result.error(), is(nullValue()));
    assertThat(result.value(), is(nullValue()));
  }

  @Test public void testResultOnSuccessCorrect() throws Exception {
    Result.of(OOOH_YEAH)
        .onSuccess(new Consumer<String>() {
          @Override public void accept(String value) {
            assertThat(value, is(OOOH_YEAH));
          }
        })
        .onFailure(new Consumer<Object>() {
          @Override public void accept(Object value) {
            fail("onFailure() should not be called.");
          }
        });
  }

  @Test public void testResultOnFailureCorrect() throws Exception {
    Result.failure(OOOH_NOOO)
        .onSuccess(new Consumer<Object>() {
          @Override public void accept(Object value) {
            fail("onSuccess() should not be called.");
          }
        })
        .onFailure(new Consumer<String>() {
          @Override public void accept(String value) {
            assertThat(value, is(OOOH_NOOO));
          }
        });
  }

  @Test public void testResultMapCorrect() throws Exception {
    Result<String, Object> result = Result.of(OOOH_YEAH)
        .map(new Transformer<String, String>() {
          @Override public String apply(String value) {
            return OOOH_NOOO;
          }
        });

    assertThat(result.value(), is(OOOH_NOOO));
  }

  @Test public void testFailureSkipsMapCorrect() throws Exception {
    Result<String, String> result = Result.failure(OOOH_NOOO)
        .map(new Transformer<Object, String>() {
          @Override public String apply(Object value) {
            fail("transformer should not be called.");
            return OOOH_YEAH;
          }
        });

    assertThat(result.isSuccess(), is(false));
    assertThat(result.error(), is(OOOH_NOOO));
  }

  @Test public void testSuccessFlatMapCorrect() throws Exception {
    Result<Integer, Exception> result = Result.of(OOOH_YEAH)
        .flatMap(new Transformer<Result<String, Object>, Result<Integer, Exception>>() {
          @Override public Result<Integer, Exception> apply(Result<String, Object> value) {
            return Result.success(1);
          }
        });

    assertThat(result.isSuccess(), is(true));
    assertThat(result.value(), is(1));
  }

  @Test public void testFailureFlatMapCorrect() throws Exception {
    Result<Integer, Exception> result = Result.failure(OOOH_NOOO)
        .flatMap(new Transformer<Result<Object, String>, Result<Integer, Exception>>() {
          @Override public Result<Integer, Exception> apply(Result<Object, String> value) {
            return Result.success(1);
          }
        });

    assertThat(result.isSuccess(), is(true));
    assertThat(result.value(), is(1));
  }

  @Test public void testResultOrCorrect() throws Exception {
    assertThat(Result.of(OOOH_YEAH)
                   .or(OOOH_NOOO), is(OOOH_YEAH));
  }

  @Test public void testFailureOrCorrect() throws Exception {
    assertThat(Result.<String, String>failure(OOOH_NOOO).or(OOOH_YEAH), is(OOOH_YEAH));
  }

  @Test public void testIsSuccessfulAndEmptyCorrect() throws Exception {
    assertThat(Result.success(null).isSuccessfulNonEmpty(), is(false));

    assertThat(Result.success(1).isSuccessfulNonEmpty(), is(true));

    assertThat(Result.failure(1).isSuccessfulNonEmpty(), is(false));

    assertThat(Result.failure(null).isSuccessfulNonEmpty(), is(false));
  }

  static class DefaultThrowerImpl<T> implements Function<T> {
    @Override public T call() {
      return null;
    }
  }
}
