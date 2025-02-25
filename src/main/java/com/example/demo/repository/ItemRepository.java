package com.example.demo.repository;

import com.example.demo.domain.inventory.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    // Поиск по значению внутри json
    @Query(value = "SELECT * FROM items WHERE CAST(data AS TEXT) LIKE %:value%", nativeQuery = true)
    List<Item> findItemByJsonValue(@Param("value") String value);

    // Поиск по точному значению поля
    @Query(value = "SELECT * FROM items WHERE data->>:field = :value", nativeQuery = true)
    List<Item> findByJsonField(@Param("field") String field, @Param("value") String value);

    // Поиск по числовым значениям с оператором (>, <, >=, <=, =)
    @Query(value = "SELECT * FROM items WHERE CAST(data->>:field AS NUMERIC) :operator :value", nativeQuery = true)
    List<Item> findByJsonNumber(@Param("field") String field, @Param("operator") String operator, @Param("value") Double value);
}
