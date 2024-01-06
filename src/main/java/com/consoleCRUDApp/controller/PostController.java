package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.PostStatus;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.service.impl.PostServiceImpl;
import com.consoleCRUDApp.view.PostView;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.ColumnData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PostController
        extends GenericEntityController<Post, PostServiceImpl, PostView>
        implements LabelNamesInputDialog, PostCreateInputDialog {

    private static final String POST_ENTITY_NAME = "POST";

    private static final String INPUT_THE_NEW_POST_CONTENT = "\nPlease input the new Post Content: ";
    private static final String SAVE_NEW_POST_OPERATION_FAILED = "\nSave new Post operation failed!!!\n";

    private final PostView postView = baseEntityView;

    public PostController(PostServiceImpl postService,
                          PostView postView) {
        super(postService, postView);
    }

    @Override
    public Post prepareNewEntity() {
        String newPostContent = postView.getUserInputNotEmpty(INPUT_THE_NEW_POST_CONTENT);

        List<Label> newPostLabels = promptPostLabelsNamesFromUser(postView);

        return Post.builder()
                .content(newPostContent)
                .labels(newPostLabels)
                .postStatus(PostStatus.UNDER_REVIEW)
                .status(Status.ACTIVE)
                .build();
    }

    @Override
    public void saveNewEntity(Post newPostToSave, String operationName) {
        newPostToSave.setCreated(LocalDateTime.now());

        Optional<Post> savedPostOptional = service.save(newPostToSave);
        if (savedPostOptional.isPresent()) {
            if (service.findById(savedPostOptional.get().getId()).isPresent()) {
                showInfoMessageEntityOperationFinishedSuccessfully(operationName, getEntityName(), newPostToSave.getId());
            }
        } else {
            postView.showInConsole(SAVE_NEW_POST_OPERATION_FAILED);
            showMenu();
        }
    }

    @Override
    public Post requestEntityUpdatesFromUser(Long postId) {
        return promptUpdatePostById(postId, postView);
    }

    @Override
    public void showEntitiesListFormatted(List<Post> activeEntities) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Post>> columns = postView.getColumnDataWithIds();
        String rend = AsciiTable.getTable(borderStyle, activeEntities, columns);
        postView.showInConsole(rend);
    }

    @Override
    public String getEntityName() {
        return POST_ENTITY_NAME;
    }
}
