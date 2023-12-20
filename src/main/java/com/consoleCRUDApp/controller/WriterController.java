package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.*;
import com.consoleCRUDApp.service.WriterServiceImpl;
import com.consoleCRUDApp.view.WriterView;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.ColumnData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        List<ColumnData<Writer>> columns = Writer.getColumnDataWithIds();
        String rend = AsciiTable.getTable(borderStyle, activeEntities, columns);
        writerView.showInConsole(rend);
    }

    @Override
    public void saveNewEntity(Writer newWriterToSave, String operationName) throws SQLException {

        Writer savedWriter = service.save(newWriterToSave);
        if (service.findById(savedWriter.getId()).isPresent()) {
            showInfoMessageEntityOperationFinishedSuccessfully(operationName, newWriterToSave.getId());
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
