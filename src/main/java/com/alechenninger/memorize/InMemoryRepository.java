/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.alechenninger.memorize;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class InMemoryRepository<E, I> {
  private final ConcurrentHashMap<I, ObjectNode> db = new ConcurrentHashMap<>();

  private final ObjectMapper mapper;
  private final Function<E, I> idOfE;
  private final Class<E> type;

  public InMemoryRepository(ObjectMapper mapper, Function<E, I> idOfE, Class<E> type) {
    this.mapper = Objects.requireNonNull(mapper, "mapper");
    this.idOfE = Objects.requireNonNull(idOfE, "idOfE");
    this.type = Objects.requireNonNull(type, "type");
  }

  public static Stream<JsonNode> stream(JsonNode node) {
    return StreamSupport.stream(
        Spliterators.spliterator(
            node.iterator(),
            node.size(),
            Spliterator.NONNULL | Spliterator.ORDERED),
        false);
  }

  public InMemoryRepository(Function<E, I> idOfE, Class<E> type) {
    this(defaultObjectMapper(), idOfE, type);
  }

  private static ObjectMapper defaultObjectMapper() {
    return new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .setDefaultVisibility(JsonAutoDetect.Value.construct(
            /*    field*/ JsonAutoDetect.Visibility.ANY,
            /*  getters*/ JsonAutoDetect.Visibility.NONE,
            /*isGetters*/ JsonAutoDetect.Visibility.NONE,
            /*  setters*/ JsonAutoDetect.Visibility.NONE,
            /* creators*/ JsonAutoDetect.Visibility.NONE));
  }

  public <T extends E> InMemoryRepository<T, I> withSubtype(Class<T> type) {
    return new InMemoryRepository<T, I>(mapper, idOfE::apply, type);
  }

  public void save(E aggregate) {
    Objects.requireNonNull(aggregate, "aggregate");
    final I id = idOfE.apply(aggregate);
    final ObjectNode object = mapper.convertValue(aggregate, ObjectNode.class);
    db.put(id, object);
  }

  public E byId(I id) {
    Objects.requireNonNull(id, "id");
    final ObjectNode object = db.get(id);
    if (object == null) throw new NoSuchElementException("No object with id: " + id);
    return mapper.convertValue(object, type);
  }

  public Stream<E> byJsonPredicate(Predicate<ObjectNode> filter) {
    Objects.requireNonNull(filter, "filter");

    return db.values().stream()
        .filter(filter)
        .map(o -> mapper.convertValue(o, type));
  }

  public Stream<E> byPredicate(Predicate<E> filter) {
    return all().filter(filter);
  }

  public Stream<E> all() {
    return db.values().stream().map(o -> mapper.convertValue(o, type));
  }

  public long size() {
    return db.values().size();
  }

  public boolean isEmpty() {
    return db.isEmpty();
  }

}
