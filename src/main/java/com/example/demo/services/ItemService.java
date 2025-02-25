package com.example.demo.services;

import com.example.demo.domain.inventory.Item;
import com.example.demo.domain.inventory.ItemStatus;
import com.example.demo.domain.template.Template;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.TemplateRepository;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final TemplateRepository templateRepository;

    @Transactional
    public Object createItem(Long templateId, String templateName, JsonNode itemData) {
        Template template = null;

        if (templateId != null) {
            template = templateRepository.findById(templateId)
                    .orElseThrow(() -> new IllegalArgumentException("❌ Шаблон с указанным ID не найден"));
        } else if (templateName != null) {
            template = templateRepository.findByName(templateName)
                    .orElseThrow(() -> new IllegalArgumentException("❌ Шаблон с указанным именем не найден"));
        }

        if (itemData == null) {
            // Если данные ещё не переданы, просто возвращаем структуру шаблона
            assert template != null;
            return template.getTemplate();
        }
        assert template != null;
        validateItemData(template, itemData);

        Item item = new Item();
        item.setTemplate(template);
        item.setData(itemData);
        item.setStatus(ItemStatus.AVAILABLE);
        item.setCreatedAt(LocalDateTime.now());

        return itemRepository.save(item);
    }

    private void validateItemData(Template template, JsonNode itemData) {
        JsonNode templateSchema = template.getTemplate();
        if (!itemData.fieldNames().hasNext()) {
            throw new IllegalArgumentException("❌ Данные предмета не могут быть пустыми");
        }

        templateSchema.fieldNames().forEachRemaining(field -> {
            if (!itemData.has(field)) {
                throw new IllegalArgumentException("❌ Поле '" + field + "' обязательно согласно шаблону");
            }
        });
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Предмет не найден"));
    }

    public Item updateItem(Long id, Item updatedItem) {
        Item item = getItemById(id);
        item.setData(updatedItem.getData());
        item.setStatus(updatedItem.getStatus());
        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public List<Item> findItemsByValue(String value) {
        return itemRepository.findItemByJsonValue(value);
    }
}
