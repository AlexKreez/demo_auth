package com.example.demo.controllers;

import com.example.demo.domain.inventory.Item;
import com.example.demo.services.ItemService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/create")
    public ResponseEntity<?> createItem(@RequestParam(required = false) Long templateId,
                                           @RequestParam(required = false) String templateName,
                                           @RequestBody(required = false) JsonNode itemData) {
        Object result = itemService.createItem(templateId, templateName, itemData);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Item item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item) {
        Item updatedItem = itemService.updateItem(id, item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search_item_field")
    public ResponseEntity<List<Item>> searchItems(@RequestParam String value) {
        return ResponseEntity.ok(itemService.findItemsByValue(value));
        }
}
