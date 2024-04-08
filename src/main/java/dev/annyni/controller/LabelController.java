package dev.annyni.controller;

import dev.annyni.model.Label;
import dev.annyni.service.LabelService;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * todo Document type LabelController
 */

@RequiredArgsConstructor
public class LabelController {
    private final LabelService labelService;

    public Label getLabelById(Long labelId){
        return labelService.getLabelById(labelId);
    }

    public List<Label> getAllLabels(){
        return labelService.getAllLabels();
    }

    public Label createLabel(Label label){
        return labelService.createLabel(label);
    }

    public Label updateLabelById(Label label){
        return labelService.updateLabel(label);
    }

    public void deleteLabelById(Long labelId){
        labelService.deleteLabelById(labelId);
    }

}
