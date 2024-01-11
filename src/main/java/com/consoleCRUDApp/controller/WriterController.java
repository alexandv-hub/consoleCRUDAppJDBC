package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.PostStatus;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.model.Writer;
import com.consoleCRUDApp.service.impl.WriterServiceImpl;
import com.consoleCRUDApp.view.WriterView;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.ColumnData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.consoleCRUDApp.view.messages.ErrorMessages.Entity.INCORRECT_ID_INPUT_WRITER_POST_LIST_CONTAINS_NO_POST_WITH_THIS_ID;
import static com.consoleCRUDApp.view.messages.ErrorMessages.Entity.SAVE_NEW_WRITER_OPERATION_FAILED;
import static com.consoleCRUDApp.view.messages.ErrorMessages.Inputs.INVALID_INPUT_PLEASE_ENTER_A_NUMERIC_VALUE;
import static com.consoleCRUDApp.view.messages.SystemMessages.Entity.*;

public class WriterController
        extends GenericEntityController<Writer, WriterServiceImpl, WriterView>
        implements LabelNamesInputDialog, PostCreateInputDialog {

    private static final String WRITER_ENTITY_NAME = "WRITER";

    private final WriterView writerView = baseEntityView;

    public WriterController(WriterServiceImpl writerService,
                            WriterView writerView) {
        super(writerService, writerView);
    }

    private static Writer getBuildWriter(Long id, String firstName, String lastName, List<Post> posts) {
        return Writer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .status(Status.ACTIVE)
                .posts(posts)
                .build();
    }

    @Override
    public Writer prepareNewEntity() {
        String newWriterFirstName = writerView.getUserInputNotEmpty(PLEASE_INPUT_NEW_WRITER_S_FIRST_NAME);
        String newWriterLastName = writerView.getUserInputNotEmpty(PLEASE_INPUT_NEW_WRITER_S_LAST_NAME);

        List<Post> newWriterPosts = promptCreateWriterPosts();

        return getBuildWriter(
                null,
                newWriterFirstName,
                newWriterLastName,
                newWriterPosts);
    }

    private List<Post> promptCreateWriterPosts() {
        List<Post> postList = new ArrayList<>();

        writerView.showInConsole(WOULD_YOU_LIKE_TO_CREATE_WRITER_POST_LIST);
        if (writerView.userConfirmsOperation()) {
            int counter = 1;
            do {
                writerView.showInConsole("\nCreating Post " + counter + " for Writer entity...\n");
                Post newPost = Post.builder()
                        .content(writerView.getUserInputNotEmpty("\nEnter Post " + counter + " Content: "))
                        .created(LocalDateTime.now())
                        .postStatus(PostStatus.UNDER_REVIEW)
                        .status(Status.ACTIVE)
                        .labels(promptPostLabelsNamesFromUser(writerView))
                        .build();

                postList.add(newPost);

                counter++;
                writerView.showInConsole("\nWould you like to " + CREATE + " the new Writer post " + counter + "?");
            } while (writerView.userConfirmsOperation());
        }
        else {
            showInfoMessageOperationCancelled(CREATE + " post list", WRITER_ENTITY_NAME);
        }
        return postList;
    }

    private List<Post> getWriterPosts(Long id) {
        return service.findById(id)
                .map(Writer::getPosts)
                .orElse(Collections.emptyList());
    }

    @Override
    public Writer requestEntityUpdatesFromUser(Long writerId) {
        String newWriterFirstName = writerView.getUserInputNotEmpty(
                PLEASE_INPUT_WRITER_S_NEW_FIRST_NAME);
        String newWriterLastName = writerView.getUserInputNotEmpty(
                PLEASE_INPUT_WRITER_S_NEW_LAST_NAME);

        List<Post> writerPosts = getWriterPosts(writerId);
        writerView.showInConsole(
                WOULD_YOU_LIKE_TO_UPDATE_WRITER_S_POSTS);
        while (writerView.userConfirmsOperation()) {
            writerView.showWriterPostsFormatted(writerPosts);
            writerPosts = promptUpdateWriterPosts(writerPosts);
            writerView.showInConsole(WOULD_YOU_LIKE_TO_CONTINUE_TO_UPDATE_WRITER_S_POSTS_OR_SAVE_ALL_CHANGES);
        }

        return getBuildWriter(
                writerId,
                newWriterFirstName,
                newWriterLastName,
                writerPosts);
    }

    private List<Post> promptUpdateWriterPosts(List<Post> posts) {

        Long userInputPostId;
        boolean isUserInputValid = true;

        do {
            List<Long> postIds = posts.stream()
                    .map(Post::getId)
                    .collect(Collectors.toList());

            try {
                String userInputAnswerStr = writerView.getUserInput(
                        "\nPlease choose the writer's post operation num: " +
                                "\n1 - " + UPDATE + " by ID, " +
                                "\n2 - " + DELETE + " by ID, " +
                                "\n3 - ADD new posts" +
                                "\n(or empty input to cancel): ");
                if (userInputAnswerStr.isEmpty()) {
                    break;
                }

                int userInputAnswerInt = Integer.parseInt(userInputAnswerStr);

                if (userInputAnswerInt == 1) {
                    userInputPostId = promptUserInputPostIdFromPostsIds(postIds, UPDATE);
                    if (userInputPostId == null) {
                        break;
                    }
                    processUpdatePostInPostsByUserInputPostId(posts, userInputPostId);
                }

                if (userInputAnswerInt == 2) {
                    userInputPostId = promptUserInputPostIdFromPostsIds(postIds, DELETE);
                    if (userInputPostId == null) {
                        break;
                    }
                    posts = processRemovePostFromPostsByUserInputPostId(posts, userInputPostId);
                    continue;
                }

                if (userInputAnswerInt == 3) {
                    List<Post> newPosts = promptCreateWriterPosts();
                    posts.addAll(newPosts);
                }
            }
            catch (NumberFormatException nfe) {
                writerView.showInConsole(INVALID_INPUT_PLEASE_ENTER_A_NUMERIC_VALUE);
                isUserInputValid = false;
            }
        }  while (!isUserInputValid);

        writerView.showWriterPostsFormatted(posts);
        return posts;
    }

    private List<Post> processRemovePostFromPostsByUserInputPostId(List<Post> posts, Long userInputPostId) {
        writerView.showInConsole(
                "\nDo you confirm " + DELETE + " writer's post (ID='" + userInputPostId + "') operation?");
        if (writerView.userConfirmsOperation()) {
            long finalUserInputPostId = userInputPostId;
            posts = posts.stream()
                    .filter(post -> post.getId() == null || post.getId() != finalUserInputPostId)
                    .collect(Collectors.toList());
        }
        return posts;
    }

    private void processUpdatePostInPostsByUserInputPostId(List<Post> posts, Long userInputPostId) {
        long finalUserInputPostId = userInputPostId;

        writerView.showInConsole(
                "\nDo you confirm " + UPDATE + " writer's post (ID='" + finalUserInputPostId +"') operation?");
        if (writerView.userConfirmsOperation()) {

            Post postToUpdate = posts.stream()
                    .filter(post -> post.getId().equals(finalUserInputPostId))
                    .findFirst().orElse(null);

            int postIndex = posts.indexOf(postToUpdate);

            if (postToUpdate != null) {
                Post updatedPostById = promptUpdatePostById(postToUpdate.getId(), writerView);
                posts.set(postIndex, updatedPostById);
            }
        }
    }

    private Long promptUserInputPostIdFromPostsIds(List<Long> postIds, String operationName) {
        boolean isUserInputValid;
        Long userInputPostId = null;

        do {
            isUserInputValid = true;
            try {
                String userInputPostIdStr = writerView.getUserInput(
                        "\nPlease input the ID of the post you would like to " + operationName +" (or empty input to cancel): ");
                if (userInputPostIdStr.isEmpty()) {
                    break;
                }
                userInputPostId = Long.parseLong(userInputPostIdStr);
            }
            catch (NumberFormatException nfe) {
                writerView.showInConsole(INVALID_INPUT_PLEASE_ENTER_A_NUMERIC_VALUE);
                isUserInputValid = false;
                continue;
            }

            if (!postIds.contains(userInputPostId)) {
                writerView.showInConsole(INCORRECT_ID_INPUT_WRITER_POST_LIST_CONTAINS_NO_POST_WITH_THIS_ID);
                isUserInputValid = false;
            }
        } while (!isUserInputValid);

        return userInputPostId;
    }

    @Override
    public void showEntitiesListFormatted(List<Writer> activeEntities) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Writer>> columns = writerView.getColumnDataWithIds();
        String rend = AsciiTable.getTable(borderStyle, activeEntities, columns);
        writerView.showInConsole(rend);
    }

    @Override
    public void saveNewEntity(Writer newWriterToSave) {
        Optional<Writer> savedWriterOptional = service.save(newWriterToSave);
        if (savedWriterOptional.isPresent()) {
            if (savedWriterOptional.get().getId() != null) {
                long savedId = savedWriterOptional.get().getId();
                showInfoMessageEntityOperationFinishedSuccessfully(SAVE, getEntityName(), savedId);
            }
        } else {
            writerView.showInConsole(SAVE_NEW_WRITER_OPERATION_FAILED);
            showMenu();
        }
    }

    @Override
    public String getEntityName() {
        return WRITER_ENTITY_NAME;
    }
}
