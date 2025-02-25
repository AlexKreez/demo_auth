package com.example.demo.controllers;

import com.example.demo.domain.template.Template;
import com.example.demo.services.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @PreAuthorize("hasAuthority('WAREHOUSE_MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<Template> createTemplate(@RequestBody Template template) {
        Template savedTemplate = templateService.createTemplate(template);
        return ResponseEntity.ok(savedTemplate);
    }

    @PreAuthorize("hasAuthority('WAREHOUSE_MANAGER')")
    @GetMapping("/get_all")
    public ResponseEntity<List<Template>> getAllTemplates() {
        List<Template> templates = templateService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    @PreAuthorize("hasAuthority('WAREHOUSE_MANAGER')")
    @GetMapping("/search/{id}")
    public ResponseEntity<Template> getTemplateById(@PathVariable Long id) {
        Template template = templateService.getTemplateById(id);
        return ResponseEntity.ok(template);
    }

    @PreAuthorize("hasAuthority('WAREHOUSE_MANAGER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Template> updateTemplate(@PathVariable Long id, @RequestBody Template template) {
        Template updatedTemplate = templateService.updateTemplate(id, template);
        return ResponseEntity.ok(updatedTemplate);
    }

    @PreAuthorize("hasAuthority('WAREHOUSE_MANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('WAREHOUSE_MANAGER')")
    @GetMapping("/search_template_value")
    public ResponseEntity<List<Template>> searchTemplates(@RequestParam String value) {
        return ResponseEntity.ok(templateService.findTemplatesByValue(value));
    }

}
