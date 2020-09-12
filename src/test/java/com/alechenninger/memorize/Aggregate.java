package com.alechenninger.memorize;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

public class Aggregate {
  private Value value;

  private Aggregate() {}

  public Value value() {
    return value;
  }

  public Aggregate(Value value) {
    this.value = value;
  }

  @EqualsAndHashCode
  @ToString
  public static class Value {
    @JsonValue
    private String raw;

    @JsonCreator
    private Value(String raw) {
      this.raw = raw;
    }

    public static Value of(String raw) { return new Value(raw); }
  }
}
