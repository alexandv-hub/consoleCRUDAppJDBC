package com.consoleCRUDApp.view;

import com.consoleCRUDApp.ApplicationContext;
import com.consoleCRUDApp.controller.LabelController;
import com.consoleCRUDApp.model.DBEntity;
import com.consoleCRUDApp.model.Label;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.ColumnData;
import com.github.freva.asciitable.HorizontalAlign;

import java.util.Arrays;
import java.util.List;

import static com.consoleCRUDApp.controller.EntityController.GO_BACK_TO_MAIN_MENU_COMMAND;
import static com.consoleCRUDApp.view.messages.SystemMessages.Entity.PLEASE_INPUT_NEW_LABEL_NAME;

public class LabelView extends BaseEntityView {

    private LabelController labelController;

    private void ensureControllerIsInitialized() {
        if (labelController == null) {
            labelController = ApplicationContext.getInstance().getLabelController();
        }
    }

    @Override
    public void startMenu() {
        ensureControllerIsInitialized();

        showConsoleEntityMenu(labelController.getEntityName());

        String inputCommand = getUserInputCommand();
        while (!inputCommand.equals(GO_BACK_TO_MAIN_MENU_COMMAND)) {
            labelController.executeMenuUserCommand(inputCommand);
            inputCommand = getUserInputCommand();
        }
        labelController.exit();
        showConsoleMainMenu();
    }

    public String promptNewLabelNameFromUser() {
        return getUserInputNotEmpty(PLEASE_INPUT_NEW_LABEL_NAME);
    }


    @Override
    public String toStringTableViewWithIds(DBEntity label) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Label>> columns = getColumnDataWithIds();
        List<Label> labels = List.of((Label) label);
        return "\n" + AsciiTable.getTable(borderStyle, labels, columns);
    }

    @Override
    public String toStringTableViewEntityNoIds(DBEntity label) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Label>> columns = getColumnDataNoIds();
        List<Label> labels = List.of((Label) label);
        return "\n" + AsciiTable.getTable(borderStyle, labels, columns);
    }

    public List<ColumnData<Label>> getColumnDataWithIds() {
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
