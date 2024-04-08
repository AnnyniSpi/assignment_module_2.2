package dev.annyni;

import dev.annyni.controller.LabelController;
import dev.annyni.controller.PostController;
import dev.annyni.controller.WriterController;
import dev.annyni.repository.LabelRepository;
import dev.annyni.repository.PostRepository;
import dev.annyni.repository.WriterRepository;
import dev.annyni.repository.imp.JdbcLabelRepositoryImpl;
import dev.annyni.repository.imp.JdbcPostRepositoryImpl;
import dev.annyni.repository.imp.JdbcWriterRepositoryImpl;
import dev.annyni.service.LabelService;
import dev.annyni.service.PostService;
import dev.annyni.service.WriterService;
import dev.annyni.view.LabelView;
import dev.annyni.view.PostView;
import dev.annyni.view.WriterView;

/**
 * todo Document type ProjectFactory
 */
public class ProjectFactory {
    private final LabelRepository labelRepository = new JdbcLabelRepositoryImpl();
    private final LabelService labelService = new LabelService(labelRepository);

    private final WriterRepository writerRepository = new JdbcWriterRepositoryImpl();
    private final WriterService writerService = new WriterService(writerRepository);

    private final PostRepository postRepository = new JdbcPostRepositoryImpl();
    private final PostService postService = new PostService(postRepository);

    private final LabelController labelController = new LabelController(labelService);
    private final WriterController writerController = new WriterController(writerService);
    private final PostController postController = new PostController(postService);

    private final LabelView labelView = new LabelView(labelController);
    private final WriterView writerView = new WriterView(writerController, postController);
    private final PostView postView = new PostView(writerController, postController, labelController);

    public void runLabel(){
        labelView.printMenu();
    }

    public void runWriter(){
        writerView.printMenu();
    }

    public void runPost(){
        postView.printMenu();
    }
}
