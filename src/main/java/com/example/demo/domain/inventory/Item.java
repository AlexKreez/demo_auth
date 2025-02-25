package com.example.demo.domain.inventory;

import com.example.demo.domain.template.Template;
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
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Если предмет создан по шаблону, сохраняется ссылка на Template
    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    // JSON с данными предмета, например:
    // {"Имя": "iPhone 12", "IMEI": "123456789012345", "Бренд": "Apple"}
    @Convert(converter = JsonNodeConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private JsonNode data;

    // Статус предмета (например, AVAILABLE, REQUESTED, ASSIGNED)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus status;

    // Дата создания предмета
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
