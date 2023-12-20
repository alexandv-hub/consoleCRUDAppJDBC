package com.consoleCRUDApp.model;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.ColumnData;
import com.github.freva.asciitable.HorizontalAlign;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Builder
public class Post implements Entity {
    private Long id;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<Label> labels;
    private PostStatus postStatus;
    private Status status;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String toStringTableViewWithIds() {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Post>> columns = getColumnDataWithIds();
        return "\n" + AsciiTable.getTable(borderStyle, List.of(this), columns);
    }

    @Override
    public String toStringTableViewEntityNoIds() {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Post>> columns = getColumnDataNoIds();
        return "\n" + AsciiTable.getTable(borderStyle, List.of(this), columns);
    }

    public static List<ColumnData<Post>> getColumnDataWithIds() {
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
