package com.example.demo.repository;

import com.example.demo.domain.template.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    // Дополнительные методы поиска по имени и т.п. можно добавить здесь.
}
