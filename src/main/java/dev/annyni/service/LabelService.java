package dev.annyni.service;

import dev.annyni.model.Label;
import dev.annyni.repository.LabelRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * todo Document type LabelService
 */
@RequiredArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;

    public Label getLabelById(Long labelId){
        return labelRepository.findById(labelId);
    }

    public List<Label> getAllLabels(){
        return labelRepository.findAll();
    }

    public Label createLabel(Label label){
        return labelRepository.save(label);
    }

    public Label updateLabel(Label label){
        return labelRepository.update(label);
    }

    public void deleteLabelById(Long labelId){
        labelRepository.deleteById(labelId);
    }
}
