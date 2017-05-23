package com.amatkivskiy.result.sample;

import com.amatkivskiy.result.Result;

public class Main {
  public static void mainSimple(String[] args) {
    System.out.println("Simplest way to create Result.");

    Result.success("success");
    Result.failure("failure");
    Result.of("success");
    Result.of(() -> "success");
    Result.orDefault(() -> Integer.parseInt("invalid"), -1);
    Result.orFailWith(() -> Integer.parseInt("invalid"), new IllegalArgumentException());

    System.out.println("Transforming `Result`");
    Result.of("success")
        .map(String::length);
    Result.of("success")
        .flatMap(value -> {
          if (value.isSuccess()) {
            return Result.of(value.value()
                                 .length());
          } else {
            return Result.failure(new IllegalArgumentException());
          }
        });

    System.out.println("Consuming `Result`");
    Result.success("success")
        .value();
    Result.failure("failure")
        .error();
    Result.of("success")
        .onSuccess(System.out::println)
        .onFailure(System.err::println);
    String success = Result.<String, String>failure("failure").or("success");
  }

  public static void main(String[] args) {
    System.out.println("Real life login example");

    final String login = "username";
    final String password = "password";

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
  }

  private static Result<String, Exception> performLogin(Credentials creds) {
    if ("username".equalsIgnoreCase(creds.username) && "password".equalsIgnoreCase(creds.password)) {
      return Result.success("hurrah");
    } else {
      return Result.failure(new Exception("Wrong login or password."));
    }
  }

  private static Result<Object, String> validateInputs(String login, String password) {
    if (login != null && password != null && !login.isEmpty() && !password.isEmpty()) {
      return Result.success(null);
    } else {
      return Result.failure("Login or password is not valid.");
    }
  }

  static class Credentials {
    public final String username;
    public final String password;

    public Credentials(String username, String password) {
      this.username = username;
      this.password = password;
    }
  }
}
