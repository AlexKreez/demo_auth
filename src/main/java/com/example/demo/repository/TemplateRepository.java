package com.example.demo.repository;

import com.example.demo.domain.template.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByName(String name);
    @Query(value = "SELECT * FROM templates WHERE CAST(template AS TEXT) LIKE %:value%", nativeQuery = true)
    List<Template> findTemplateByJsonValue(@Param("value") String value);
}
