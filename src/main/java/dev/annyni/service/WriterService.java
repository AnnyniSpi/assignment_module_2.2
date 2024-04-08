package dev.annyni.service;

import dev.annyni.model.Writer;
import dev.annyni.repository.WriterRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * todo Document type WriterService
 */
@RequiredArgsConstructor
public class WriterService {
    private final WriterRepository writerRepository;

    public Writer getWriterById(Long writerId){
        return writerRepository.findById(writerId);
    }

    public List<Writer> getAllWriters(){
        return writerRepository.findAll();
    }

    public Writer createWriter(Writer writer){
        return writerRepository.save(writer);
    }

    public Writer updateWriter(Writer writer){
        return writerRepository.update(writer);
    }

    public void deleteWriterById(Long writerId){
        writerRepository.deleteById(writerId);
    }
}
