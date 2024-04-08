package service;

import dev.annyni.model.Status;
import dev.annyni.model.Writer;
import dev.annyni.repository.WriterRepository;
import dev.annyni.service.WriterService;
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
 * todo Document type WriterServiceTest
 */
public class WriterServiceTest {

    @Mock
    private WriterRepository writerRepository;

    private WriterService writerService;

    private Writer testWriter;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);

        writerService = new WriterService(writerRepository);

        testWriter = Writer.builder()
            .id(1L)
            .firstName("Test")
            .lastName("Test")
            .posts(new ArrayList<>())
            .status(Status.ACTIVE)
            .build();
    }

    @Test
    void findByIdTest(){
        Mockito.when(writerRepository.findById(anyLong())).thenReturn(testWriter);

        Writer writer = writerService.getWriterById(1L);

        assertNotNull(writer);
        assertEquals("Test", writer.getFirstName());
        assertEquals("Test", writer.getLastName());
        assertEquals(Status.ACTIVE, writer.getStatus());
    }

    @Test
    void findAllTest(){
        List<Writer> writers = new ArrayList<>();
        writers.add(testWriter);

        Mockito.when(writerRepository.findAll()).thenReturn(writers);

        List<Writer> writerList = writerService.getAllWriters();

        assertNotNull(writerList);
        assertEquals(1, writerList.size());
        assertEquals("Test", writerList.get(0).getFirstName());
        assertEquals("Test", writerList.get(0).getLastName());
        assertEquals(Status.ACTIVE, writerList.get(0).getStatus());
    }

    @Test
    void createTest(){
        Mockito.when(writerRepository.save(any(Writer.class))).thenReturn(testWriter);

        Writer writer = writerService.createWriter(testWriter);

        assertNotNull(writer);
        assertEquals("Test", writer.getFirstName());
        assertEquals("Test", writer.getLastName());
        assertEquals(Status.ACTIVE, writer.getStatus());

        verify(writerRepository, timeout(1)).save(testWriter);
    }

    @Test
    void updateTest(){
        Writer updateWriter = Writer.builder()
            .id(1L)
            .firstName("Update")
            .lastName("Update")
            .posts(new ArrayList<>())
            .status(Status.ACTIVE)
            .build();

        Mockito.when(writerRepository.update(any(Writer.class))).thenReturn(updateWriter);

        Writer writer = writerService.updateWriter(updateWriter);

        assertNotNull(writer);
        assertEquals("Update", writer.getFirstName());
        assertEquals("Update", writer.getLastName());
        assertEquals(Status.ACTIVE, writer.getStatus());

        verify(writerRepository, times(1)).update(updateWriter);
    }

    @Test
    void deleteTest(){
        writerService.deleteWriterById(1L);
        verify(writerRepository, times(1)).deleteById(1L);
    }
}
