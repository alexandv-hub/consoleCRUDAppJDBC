package com.consoleCRUDApp;

import com.consoleCRUDApp.controller.LabelController;
import com.consoleCRUDApp.controller.PostController;
import com.consoleCRUDApp.controller.WriterController;
import com.consoleCRUDApp.repository.LabelRepository;
import com.consoleCRUDApp.repository.PostRepository;
import com.consoleCRUDApp.repository.WriterRepository;
import com.consoleCRUDApp.repository.hibernate.HibLabelRepositoryImpl;
import com.consoleCRUDApp.repository.hibernate.HibPostRepositoryImpl;
import com.consoleCRUDApp.repository.hibernate.HibWriterRepositoryImpl;
import com.consoleCRUDApp.service.impl.LabelServiceImpl;
import com.consoleCRUDApp.service.impl.PostServiceImpl;
import com.consoleCRUDApp.service.impl.WriterServiceImpl;
import com.consoleCRUDApp.view.LabelView;
import com.consoleCRUDApp.view.PostView;
import com.consoleCRUDApp.view.WriterView;
import lombok.Getter;

import static com.consoleCRUDApp.view.messages.ErrorMessages.Database.SORRY_FAILED_TO_INITIALIZE_APPLICATION_CONTEXT_AND_START_THE_APPLICATION;

@Getter
public class ApplicationContext {

    private static ApplicationContext instance;

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = createApplicationContext();
        }
        return instance;
    }

    private final LabelController labelController;
    private final PostController postController;
    private final WriterController writerController;

    private ApplicationContext(LabelController labelController,
                       PostController postController,
                       WriterController writerController) {
        this.labelController = labelController;
        this.postController = postController;
        this.writerController = writerController;
    }

    private static ApplicationContext createApplicationContext() {
        LabelController labelController = null;
        PostController postController = null;
        WriterController writerController = null;

        try {
            LabelRepository labelRepository = new HibLabelRepositoryImpl();
            PostRepository postRepository = new HibPostRepositoryImpl();
            WriterRepository writerRepository = new HibWriterRepositoryImpl();

            LabelServiceImpl labelService = new LabelServiceImpl(labelRepository);
            PostServiceImpl postService = new PostServiceImpl(postRepository);
            WriterServiceImpl writerService = new WriterServiceImpl(writerRepository);

            WriterView writerView = new WriterView();
            PostView postView = new PostView();
            LabelView labelView = new LabelView();

            labelController = new LabelController(labelService, labelView);
            postController = new PostController(postService, postView);
            writerController = new WriterController(writerService, writerView);
        } catch (Throwable t) {
            System.out.println(SORRY_FAILED_TO_INITIALIZE_APPLICATION_CONTEXT_AND_START_THE_APPLICATION);
            t.printStackTrace(System.out);
            System.exit(1);
        }
        return new ApplicationContext(labelController, postController, writerController);
    }
}

