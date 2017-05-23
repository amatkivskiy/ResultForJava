# Result for Java

[![CircleCI](https://circleci.com/gh/amatkivskiy/ResultForJava/tree/master.svg?style=svg)](https://circleci.com/gh/amatkivskiy/ResultForJava/tree/master) [![Coverage Status](https://coveralls.io/repos/github/amatkivskiy/ResultForJava/badge.svg?branch=master)](https://coveralls.io/github/amatkivskiy/ResultForJava?branch=master) ![Android Supported](https://img.shields.io/badge/android%20support-YES-green.svg)

Simple and lightwight implementation of success/failure pattern with **_KISS_** in mind. Indeed it is just a `Result<V, E>` type.

## Why?
`Result<V, E>` is a simple monadic container for the data that represents a result of any operation or action that need to be executed. 

This means that any operation that you need can finish with either `Success` with the data in it (`Result.value()`) or `Failure` with error data in it (`Result.error()`).

## Examples
### Simple

- Simplest way to create Result:
```
Result.success("success");
-----------------------
Result.failure("failure");
-----------------------
Result.of("success");
-----------------------
Result.of(new Function<String>() {
  @Override public String call() throws Exception {
    return "success";
  }
});
-----------------------
Result.orDefault(new Function<Integer>() {
  @Override public Integer call() throws Exception {
    return Integer.parseInt("invalid");
  }
}, -1);
-----------------------
Result.orFailWith(new Function<Integer>() {
  @Override public Integer call() throws Exception {
    return Integer.parseInt("invalid");
  }
}, new IllegalArgumentException());
```

- Transforming `Result`:
```
Result.of("success").map(new Transformer<String, Integer>() {
  @Override public Integer apply(String value) {
    return value.length();
  }
});
-----------------------
Result.of("success")
  .flatMap(new Transformer<Result<String, Object>, Result<Integer, IllegalArgumentException>>() {
    @Override public Result<Integer, IllegalArgumentException> apply(Result<String, Object> value) {
      if (value.isSuccess()) {
        return Result.of(value.value()
                              .length());
      } else {
        return Result.failure(new IllegalArgumentException());
      }
    }
});
```

- Consuming `Result`:
```
Result.success("success").value();
-----------------------
Result.failure("failure").error();
-----------------------
Result.of("success")
      .onSuccess(new Consumer<String>() {
        @Override public void accept(String value) {
          System.out.println(value);
        }
      })
      .onFailure(new Consumer<Object>() {
        @Override public void accept(Object value) {
          System.err.println(value);
        }
      });
-----------------------
String success = Result.<String, String>failure("failure").or("success");
```

### Real life
 - Real life login example:
```
final String login = "username";
final String password = "password";

validateInputs(login, password).map(new Transformer<Object, Credentials>() {
    @Override public Credentials apply(Object value) {
      return new Credentials(login, password);
    }
  })
      .flatMap(new Transformer<Result<Credentials, String>, Result<String, Exception>>() {
        @Override public Result<String, Exception> apply(Result<Credentials, String> it) {
          if (it.isSuccess()) {
            return performLogin(it.value());
          } else {
            return Result.failure(new Exception(it.error()));
          }
        }
      })
      .onSuccess(new Consumer<String>() {
        @Override public void accept(String value) {
          System.out.println(value);
        }
      })
      .onFailure(new Consumer<Exception>() {
        @Override public void accept(Exception value) {
          System.err.println(value);
        }
      });
```
- Same flow but with [Retrolambda](https://github.com/orfjackal/retrolambda):
```
validateInputs(login, password).map(value -> new Credentials(login, password))
        .flatMap(it -> {
          if (it.isSuccess()) {
            return performLogin(it.value());
          } else {
            return Result.failure(new Exception(it.error()));
          }
        })
        .onSuccess(System.out::println)
        .onFailure(System.err::println);
```

## Download
TODO

## Inspired by
This project was heavily inspired by:

- Railway Oriented Programming ([Part 1](http://fsharpforfunandprofit.com/posts/recipe-part1/) and [Part 2](http://fsharpforfunandprofit.com/posts/recipe-part2/))
- [kittinunf/Result](https://github.com/kittinunf/Result)
- [javaslang](http://www.javaslang.io/)

## License
```
Copyright 2017 Andriy Matkivskiy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
