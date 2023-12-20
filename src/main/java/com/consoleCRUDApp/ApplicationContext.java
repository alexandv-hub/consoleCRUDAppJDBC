package com.consoleCRUDApp;

import com.consoleCRUDApp.controller.LabelController;
import com.consoleCRUDApp.controller.PostController;
import com.consoleCRUDApp.controller.WriterController;
import com.consoleCRUDApp.repository.LabelRepository;
import com.consoleCRUDApp.repository.PostRepository;
import com.consoleCRUDApp.repository.WriterRepository;
import com.consoleCRUDApp.repository.jdbc.JdbcLabelRepositoryImpl;
import com.consoleCRUDApp.repository.jdbc.JdbcPostRepositoryImpl;
import com.consoleCRUDApp.repository.jdbc.JdbcWriterRepositoryImpl;
import com.consoleCRUDApp.service.LabelServiceImpl;
import com.consoleCRUDApp.service.PostServiceImpl;
import com.consoleCRUDApp.service.WriterServiceImpl;
import com.consoleCRUDApp.view.LabelView;
import com.consoleCRUDApp.view.PostView;
import com.consoleCRUDApp.view.WriterView;
import lombok.Getter;

import java.sql.SQLException;

@Getter
public class ApplicationContext {

    @Getter
    private static final ApplicationContext instance;

    static {
        try {
            instance = new ApplicationContext();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final LabelController labelController;
    private final PostController postController;
    private final WriterController writerController;

    private ApplicationContext() throws SQLException {
        LabelRepository labelRepository = new JdbcLabelRepositoryImpl();
        PostRepository postRepository = new JdbcPostRepositoryImpl(labelRepository);
        WriterRepository writerRepository = new JdbcWriterRepositoryImpl(postRepository, labelRepository);

        LabelServiceImpl labelService = new LabelServiceImpl(labelRepository);
        PostServiceImpl postService = new PostServiceImpl(postRepository);
        WriterServiceImpl writerService = new WriterServiceImpl(writerRepository);

        WriterView writerView = new WriterView();
        PostView postView = new PostView();
        LabelView labelView = new LabelView();

        labelController = new LabelController(labelService, labelView);
        postController = new PostController(postService, postView);
        writerController = new WriterController(writerService, writerView);
    }

}
