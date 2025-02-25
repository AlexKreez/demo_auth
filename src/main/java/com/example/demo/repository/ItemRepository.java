package com.example.demo.repository;

import com.example.demo.domain.inventory.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "SELECT * FROM items WHERE CAST(data AS TEXT) LIKE %:value%", nativeQuery = true)
    List<Item> findItemByJsonValue(@Param("value") String value);
}
