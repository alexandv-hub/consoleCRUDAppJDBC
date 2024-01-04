package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.PostStatus;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.model.Writer;
import com.consoleCRUDApp.service.impl.WriterServiceImpl;
import com.consoleCRUDApp.view.WriterView;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.ColumnData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WriterController
        extends GenericEntityController<Writer, WriterServiceImpl, WriterView>
        implements LabelNamesInputDialog {

    private final WriterView writerView = baseEntityView;

    public WriterController(WriterServiceImpl writerService,
                            WriterView writerView) {
        super(writerService, writerView);
    }

    @Override
    public Writer prepareNewEntity() {
        String newWriterFirstName = writerView.getUserInput("\nPlease input the new Writer First Name: ");
        String newWriterLastName = writerView.getUserInput("Please input the new Writer Last Name: ");

        List<Post> newWriterPosts = promptWriterPostsFromUser();

        return Writer.builder()
                .firstName(newWriterFirstName)
                .lastName(newWriterLastName)
                .status(Status.ACTIVE)
                .posts(newWriterPosts)
                .build();
    }

    private List<Post> promptWriterPostsFromUser() {
        List<Post> postList = new ArrayList<>();

        writerView.showInConsole("\nWould you like to CREATE the new Writer posts?");
        if (writerView.userConfirmsOperation()) {
            int counter = 1;
            do {
                writerView.showInConsole("\nCreating Post " + counter + " for Writer entity...\n");
                Post newPost = Post.builder()
                        .content(writerView.getUserInput("Enter Post " + counter + " Content: "))
                        .postStatus(PostStatus.UNDER_REVIEW)
                        .status(Status.ACTIVE)
                        .labels(promptPostLabelsNamesFromUser(writerView))
                        .build();

                postList.add(newPost);

                counter++;
                writerView.showInConsole("\nWould you like to CREATE the new Writer post " + counter + "?");
            } while (writerView.userConfirmsOperation());
        }
        else {
            showInfoMessageOperationCancelled("create", getEntityClassName());
        }
        return postList;
    }

    @Override
    public Writer requestEntityUpdatesFromUser(Long id) {
        String newWriterFirstName = writerView.getUserInput(
                "Please input the Writer new First Name: ");
        String newWriterLastName = writerView.getUserInput(
                "Please input the Writer new Last Name: ");

        return Writer.builder()
                .id(id)
                .firstName(newWriterFirstName)
                .lastName(newWriterLastName)
                .status(Status.ACTIVE)
                .build();
    }

    @Override
    public void showEntitiesListFormatted(List<Writer> activeEntities) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Writer>> columns = writerView.getColumnDataWithIds();
        String rend = AsciiTable.getTable(borderStyle, activeEntities, columns);
        writerView.showInConsole(rend);
    }

    @Override
    public void saveNewEntity(Writer newWriterToSave, String operationName) {
        Optional<Writer> savedWriterOptional = service.save(newWriterToSave);
        if (savedWriterOptional.isPresent()) {
            if (savedWriterOptional.get().getId() != null) {
                long savedId = savedWriterOptional.get().getId();
                showInfoMessageEntityOperationFinishedSuccessfully(operationName, savedId);
            }
        } else {
            writerView.showInConsole("\nSave new Writer operation failed!!!\n");
            showMenu();
        }
    }

    @Override
    public String getEntityClassName() {
        return "WRITER";
    }

}
