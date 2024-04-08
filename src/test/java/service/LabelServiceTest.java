package service;

import dev.annyni.model.Label;
import dev.annyni.model.Status;
import dev.annyni.repository.LabelRepository;
import dev.annyni.service.LabelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * todo Document type LabelServiceTest
 */
public class LabelServiceTest {
    @Mock
    private LabelRepository labelRepository;

    private LabelService labelService;

    private Label testLabel;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);

        labelService = new LabelService(labelRepository);

        testLabel = Label.builder()
            .id(1L)
            .name("Test")
            .status(Status.ACTIVE)
            .build();
    }

    @Test
    void findByIdTest(){
        Mockito.when(labelRepository.findById(anyLong())).thenReturn(testLabel);

        Label label = labelService.getLabelById(1L);

        assertNotNull(label);
        assertEquals("Test", label.getName());
        assertEquals(Status.ACTIVE, label.getStatus());
    }

    @Test
    void getAllLabelsTest(){
        List<Label> labels = new ArrayList<>();
        labels.add(testLabel);

        Mockito.when(labelRepository.findAll()).thenReturn(labels);

        List<Label> labelList = labelService.getAllLabels();

        assertNotNull(labelList);
        assertEquals(1, labelList.size());
        assertEquals("Test", labelList.get(0).getName());
        assertEquals(Status.ACTIVE, labelList.get(0).getStatus());
    }

    @Test
    void createLabelTest(){
        Mockito.when(labelRepository.save(any(Label.class))).thenReturn(testLabel);

        Label label = labelService.createLabel(testLabel);

        assertNotNull(label);
        assertEquals("Test", label.getName());
        assertEquals(Status.ACTIVE, label.getStatus());

        verify(labelRepository, times(1)).save(testLabel);
    }

    @Test
    void updateLabelTest(){
        Label updateLabel = Label.builder()
            .id(1L)
            .name("Update")
            .status(Status.ACTIVE)
            .build();

        Mockito.when(labelRepository.update(any(Label.class))).thenReturn(updateLabel);

        Label label = labelService.updateLabel(updateLabel);

        assertNotNull(label);
        assertEquals("Update", label.getName());
        assertEquals(Status.ACTIVE, label.getStatus());

        verify(labelRepository, times(1)).save(updateLabel);
    }

    @Test
    void deleteLabelTest(){
        labelService.deleteLabelById(1L);
        verify(labelRepository, times(1)).deleteById(1L);
    }
}
