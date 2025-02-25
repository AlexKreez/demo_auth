package com.example.demo.services;

import com.example.demo.domain.inventory.Item;
import com.example.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public Item updateItem(Long id, Item updatedItem) {
        Item item = getItemById(id);
        item.setData(updatedItem.getData());
        item.setStatus(updatedItem.getStatus());
        item.setTemplate(updatedItem.getTemplate());
        // Можно обновлять и другие поля при необходимости
        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
