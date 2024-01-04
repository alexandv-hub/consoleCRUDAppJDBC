package com.consoleCRUDApp.view;

import com.consoleCRUDApp.ApplicationContext;
import com.consoleCRUDApp.controller.PostController;
import com.consoleCRUDApp.model.Entity;
import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Post;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.ColumnData;
import com.github.freva.asciitable.HorizontalAlign;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.consoleCRUDApp.controller.EntityController.GO_BACK_TO_MAIN_MENU_COMMAND;

public class PostView extends BaseEntityView {

    private PostController postController;

    private void ensureControllerIsInitialized() {
        if (postController == null) {
            postController = ApplicationContext.getInstance().getPostController();
        }
    }

    @Override
    public void startMenu() {
        ensureControllerIsInitialized();

        showConsoleEntityMenu(postController.getEntityClassName());

        String inputCommand = getUserInputCommand();
        while (!inputCommand.equals(GO_BACK_TO_MAIN_MENU_COMMAND)) {
            postController.executeMenuUserCommand(inputCommand);
            inputCommand = getUserInputCommand();
        }
        postController.exit();
        showConsoleMainMenu();
    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String toStringTableViewWithIds(Entity post) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Post>> columns = getColumnDataWithIds();
        List<Post> posts = List.of((Post) post);
        return "\n" + AsciiTable.getTable(borderStyle, posts, columns);
    }

    @Override
    public String toStringTableViewEntityNoIds(Entity post) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Post>> columns = getColumnDataNoIds();
        List<Post> posts = List.of((Post) post);
        return "\n" + AsciiTable.getTable(borderStyle, posts, columns);
    }

    public List<ColumnData<Post>> getColumnDataWithIds() {
        return Arrays.asList(
                new Column().header("ID")
                        .headerAlign(HorizontalAlign.CENTER)
                        .dataAlign(HorizontalAlign.CENTER)
                        .with(post -> String.valueOf(post.getId())),
                new Column().header("Content")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Post::getContent),
                new Column().header("Created")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(post -> post.getCreated() != null ? DATE_FORMATTER.format(post.getCreated()) : ""),
                new Column().header("Updated")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(post -> post.getUpdated() != null ? DATE_FORMATTER.format(post.getUpdated()) : ""),
                new Column().header("Labels (Name(id))")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(50)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(post -> {
                            if (post.getLabels() != null) {
                                return post.getLabels().stream()
                                        .map(label -> label.getName() + "(" + label.getId().toString()+")")
                                        .collect(Collectors.joining(", "));
                            }
                            return "";
                        }),
                new Column().header("PostStatus")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(50)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(post -> String.valueOf(post.getPostStatus()))
        );
    }

    private List<ColumnData<Post>> getColumnDataNoIds() {
        return Arrays.asList(
                new Column().header("Content")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Post::getContent),
                new Column().header("Created")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(post -> post.getCreated() != null ? DATE_FORMATTER.format(post.getCreated()) : ""),
                new Column().header("Updated")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(post -> post.getUpdated() != null ? DATE_FORMATTER.format(post.getUpdated()) : ""),
                new Column().header("Labels (name)")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(50)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(post -> {
                            if (post.getLabels() != null) {
                                return post.getLabels().stream()
                                        .map(Label::getName)
                                        .collect(Collectors.joining(", "));
                            }
                            return "";
                        }),
                new Column().header("PostStatus")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(50)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(post -> String.valueOf(post.getPostStatus()))
        );
    }

}
