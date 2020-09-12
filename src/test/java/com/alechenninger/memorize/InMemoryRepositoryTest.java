/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.alechenninger.memorize;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.alechenninger.memorize.Aggregate.Value;
import org.junit.jupiter.api.Test;

class InMemoryRepositoryTest {
  InMemoryRepository<Aggregate, Value> repository =
      new InMemoryRepository<>(Aggregate::value, Aggregate.class);

  @Test
  void saves() {
    repository.save(new Aggregate(Value.of("test")));
    assertDoesNotThrow(() -> repository.byId(Value.of("test")));
  }

}