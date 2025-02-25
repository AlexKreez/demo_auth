package com.example.demo.domain.template;

import com.fasterxml.jackson.databind.JsonNode;
import com.example.demo.services.JsonNodeConverter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "templates")
@Getter
@Setter
@NoArgsConstructor
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Название шаблона (например, "Смартфон")
    @Column(nullable = false)
    private String name;

    // JSON-структура с описанием шаблона, хранится в формате JSONB
    @Convert(converter = JsonNodeConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private JsonNode template;

    // Дата создания шаблона
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
