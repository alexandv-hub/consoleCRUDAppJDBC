package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.PostStatus;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.service.PostServiceImpl;
import com.consoleCRUDApp.view.PostView;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.ColumnData;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostController
        extends GenericEntityController<Post, PostServiceImpl, PostView>
        implements LabelNamesInputDialog {

    private final PostView postView = baseEntityView;

    public PostController(PostServiceImpl postService,
                          PostView postView) {
        super(postService, postView);
    }

    @Override
    public Post prepareNewEntity() {
        String newPostContent = postView.getUserInput("\nPlease input the new Post Content: ");

        List<Label> newPostLabels = promptPostLabelsNamesFromUser(postView);

        return Post.builder()
                .content(newPostContent)
                .created(LocalDateTime.now())
                .labels(newPostLabels)
                .postStatus(PostStatus.UNDER_REVIEW)
                .status(Status.ACTIVE)
                .build();
    }

    @Override
    public void saveNewEntity(Post newPostToSave, String operationName) throws SQLException {
        Post savedPost = service.save(newPostToSave);
        if (service.findById(savedPost.getId()).isPresent()) {
            showInfoMessageEntityOperationFinishedSuccessfully(operationName, newPostToSave.getId());
        } else {
            postView.showInConsole("\nSave new Post operation failed!!!\n");
            showMenu();
        }
    }

    @Override
    public Post requestEntityUpdatesFromUser(Long id) throws SQLException {
        String updatedPostContent = postView.getUserInput("\nPlease input the Post new Content: ");

        int updatedPostStatus = Integer.parseInt(postView.getUserInput("\nPlease input the Post new PostStatus ('1'-ACTIVE, '2'-UNDER_REVIEW): "));
        while (updatedPostStatus != 1 && updatedPostStatus != 2) {
            updatedPostStatus = Integer.parseInt(postView.getUserInput("\nIncorrect input! Please input the Post new PostStatus ('1'-ACTIVE, '2'-UNDER_REVIEW): "));
        }

        List<Label> updatedLabelList = new ArrayList<>();
        postView.showInConsole("Would you like to UPDATE the Post labels?");
        if (postView.userConfirmsOperation()) {
            updatedLabelList = promptPostLabelsNamesFromUser(postView);
        } else {
            Optional<Post> postOptional = service.findById(id);
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                updatedLabelList = post.getLabels();
            }
        }

        return Post.builder()
                .id(id)
                .content(updatedPostContent)
                .labels(updatedLabelList)
                .postStatus(updatedPostStatus == 1 ? PostStatus.ACTIVE : PostStatus.UNDER_REVIEW )
                .status(Status.ACTIVE)
                .build();
    }

    @Override
    public void showEntitiesListFormatted(List<Post> activeEntities) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Post>> columns = Post.getColumnDataWithIds();
        String rend = AsciiTable.getTable(borderStyle, activeEntities, columns);
        postView.showInConsole(rend);
    }

    @Override
    public String getEntityClassName() {
        return "POST";
    }

}
