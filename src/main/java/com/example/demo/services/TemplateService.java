package com.example.demo.services;

import com.example.demo.domain.template.Template;
import com.example.demo.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;

    public Template createTemplate(Template template) {
        return templateRepository.save(template);
    }

    public List<Template> getAllTemplates() {
        return templateRepository.findAll();
    }

    public Template getTemplateById(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Шаблон не найден"));
    }

    public Template updateTemplate(Long id, Template updatedTemplate) {
        Template template = getTemplateById(id);
        template.setName(updatedTemplate.getName());
        template.setTemplate(updatedTemplate.getTemplate());
        return templateRepository.save(template);
    }

    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }
}