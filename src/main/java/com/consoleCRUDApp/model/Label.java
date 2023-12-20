package com.consoleCRUDApp.model;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.ColumnData;
import com.github.freva.asciitable.HorizontalAlign;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@ToString
public class Label implements Entity {
    private Long id;
    private String name;
    private Status status;

    @Override
    public String toStringTableViewWithIds() {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Label>> columns = getColumnDataWithIds();
        return "\n" + AsciiTable.getTable(borderStyle, List.of(this), columns);
    }

    @Override
    public String toStringTableViewEntityNoIds() {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Label>> columns = getColumnDataNoIds();
        return "\n" + AsciiTable.getTable(borderStyle, List.of(this), columns);
    }

    public static List<ColumnData<Label>> getColumnDataWithIds() {
        return Arrays.asList(
                new Column().header("ID")
                        .headerAlign(HorizontalAlign.CENTER)
                        .dataAlign(HorizontalAlign.CENTER)
                        .with(label -> String.valueOf(label.getId())),
                new Column().header("Name")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Label::getName)
        );
    }

    private List<ColumnData<Label>> getColumnDataNoIds() {
        return List.of(
                new Column().header("Name")
                        .headerAlign(HorizontalAlign.LEFT)
                        .maxWidth(30)
                        .dataAlign(HorizontalAlign.LEFT)
                        .with(Label::getName)
        );
    }
}
