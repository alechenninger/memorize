package com.alechenninger.memorize;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode
@ToString
public class Aggregate {
  private Value value;
  private final List<Value> others = new ArrayList<>();
  private final List<Nested> nesteds = new ArrayList<>();

  private Aggregate() {}

  public Value value() {
    return value;
  }

  public void addMore(Value... values) {
    Collections.addAll(others, values);
  }

  public void addNested(Nested nested) {
    nesteds.add(nested);
  }

  public Aggregate(Value value) {
    this.value = value;
  }

  @EqualsAndHashCode
  @ToString
  static class Nested {
    private Value value;

    private Nested() {}

    public Nested(Value value) {
      this.value = value;
    }
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
