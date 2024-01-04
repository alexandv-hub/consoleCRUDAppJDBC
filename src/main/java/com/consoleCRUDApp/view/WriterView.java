package com.consoleCRUDApp.view;

import com.consoleCRUDApp.ApplicationContext;
import com.consoleCRUDApp.controller.WriterController;
import com.consoleCRUDApp.model.Entity;
import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.Writer;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.ColumnData;
import com.github.freva.asciitable.HorizontalAlign;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.consoleCRUDApp.controller.EntityController.GO_BACK_TO_MAIN_MENU_COMMAND;

public class WriterView extends BaseEntityView {

    private WriterController writerController;

    private void ensureControllerIsInitialized() {
        if (writerController == null) {
            writerController = ApplicationContext.getInstance().getWriterController();
        }
    }

    @Override
    public void startMenu() {
        ensureControllerIsInitialized();

        showConsoleEntityMenu(writerController.getEntityClassName());

        String inputCommand = getUserInputCommand();
        while (!inputCommand.equals(GO_BACK_TO_MAIN_MENU_COMMAND)) {
            writerController.executeMenuUserCommand(inputCommand);
            inputCommand = getUserInputCommand();
        }
        writerController.exit();
        showConsoleMainMenu();
    }

    @Override
    public String toStringTableViewWithIds(Entity writer) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Writer>> columns = getColumnDataWithIds();
        List<Writer> writers = List.of((Writer) writer);
        return "\n" + AsciiTable.getTable(borderStyle, writers, columns);
    }

    @Override
    public String toStringTableViewEntityNoIds(Entity writer) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Writer>> columns = getColumnDataNoIds();
        List<Writer> writers = List.of((Writer) writer);
        return "\n" + AsciiTable.getTable(borderStyle, writers, columns);
    }

    public List<ColumnData<Writer>> getColumnDataWithIds() {
        return Arrays.asList(
                new Column().header("ID")
                        .headerAlign(HorizontalAlign.CENTER)
                        .dataAlign(HorizontalAlign.CENTER)
                        .with(writer -> String.valueOf(writer.getId())),
                new Column().header("First Name")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Writer::getFirstName),
                new Column().header("Last Name")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Writer::getLastName),
                new Column().header("Posts (Content(ID))")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(50)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(writer -> {
                            if (writer.getPosts() != null) {
                                return writer.getPosts().stream()
                                        .map(post -> post.getContent() + "(" + post.getId().toString() + ")")
                                        .collect(Collectors.joining(", "));
                            } else {
                                return "";
                            }
                        })
        );
    }

    private List<ColumnData<Writer>> getColumnDataNoIds() {
        return Arrays.asList(
                new Column().header("First Name")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Writer::getFirstName),
                new Column().header("Last Name")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Writer::getLastName),
                new Column().header("Posts (Content)")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(50)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(writer -> {
                            if (writer.getPosts() != null) {
                                return writer.getPosts().stream()
                                        .map(Post::getContent)
                                        .collect(Collectors.joining(", "));
                            } else {
                                return "";
                            }
                        })
        );
    }
}
