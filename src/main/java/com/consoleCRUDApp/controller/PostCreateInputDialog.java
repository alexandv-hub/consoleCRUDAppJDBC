package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.ApplicationContext;
import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.PostStatus;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.view.BaseEntityView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.consoleCRUDApp.view.messages.ErrorMessages.Entity.INCORRECT_INPUT_THE_STATUS_MUST_BE_1_FOR_ACTIVE_OR_2_FOR_UNDER_REVIEW;
import static com.consoleCRUDApp.view.messages.ErrorMessages.Inputs.INVALID_INPUT_PLEASE_ENTER_A_NUMERIC_VALUE;
import static com.consoleCRUDApp.view.messages.SystemMessages.Entity.*;

public interface PostCreateInputDialog extends LabelNamesInputDialog {

    default Post promptUpdatePostById(Long postId, BaseEntityView baseEntityView) {
        String updatedPostContent = baseEntityView.getUserInputNotEmpty(PLEASE_INPUT_THE_POST_NEW_CONTENT);

        int updatedPostStatus = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                updatedPostStatus = Integer.parseInt(baseEntityView.getUserInput(
                        PLEASE_INPUT_THE_POST_NEW_POST_STATUS_1_ACTIVE_2_UNDER_REVIEW));
                if (updatedPostStatus == 1 || updatedPostStatus == 2) {
                    validInput = true;
                } else {
                    baseEntityView.showInConsole(INCORRECT_INPUT_THE_STATUS_MUST_BE_1_FOR_ACTIVE_OR_2_FOR_UNDER_REVIEW);
                }
            } catch (NumberFormatException e) {
                baseEntityView.showInConsole(INVALID_INPUT_PLEASE_ENTER_A_NUMERIC_VALUE);
            }
        }

        LocalDateTime postCreated = null;
        List<Label> postLabels = new ArrayList<>();

        PostController postController = ApplicationContext.getInstance().getPostController();

        Optional<Post> postOptional = postController.service.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            postCreated = post.getCreated();
            postLabels = post.getLabels();
        }

        baseEntityView.showInConsole(WOULD_YOU_LIKE_TO_UPDATE_THE_POST_LABELS);
        if (baseEntityView.userConfirmsOperation()) {
            postLabels = promptPostLabelsNamesFromUser(baseEntityView);
        }

        return Post.builder()
                .id(postId)
                .content(updatedPostContent)
                .created(postCreated)
                .updated(LocalDateTime.now())
                .labels(postLabels)
                .postStatus(updatedPostStatus == 1 ? PostStatus.ACTIVE : PostStatus.UNDER_REVIEW)
                .status(Status.ACTIVE)
                .build();
    }
}
