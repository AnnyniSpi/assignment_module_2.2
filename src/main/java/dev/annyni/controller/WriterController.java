package dev.annyni.controller;

import dev.annyni.model.Writer;
import dev.annyni.service.WriterService;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * todo Document type WriterController
 */
@RequiredArgsConstructor
public class WriterController {
    private final WriterService writerService;

    public Writer getWriterById(Long writerId){
        return writerService.getWriterById(writerId);
    }

    public List<Writer> getAllWriters(){
        return writerService.getAllWriters();
    }

    public Writer createWriter(Writer writer){
        return writerService.createWriter(writer);
    }

    public Writer updateWriter(Writer writer){
        return writerService.updateWriter(writer);
    }

    public void deleteWriterById(Long writerId){
        writerService.deleteWriterById(writerId);
    }
}
