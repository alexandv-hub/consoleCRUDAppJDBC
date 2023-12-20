package com.consoleCRUDApp.view;

import com.consoleCRUDApp.ApplicationContext;
import com.consoleCRUDApp.controller.LabelController;

import java.sql.SQLException;

import static com.consoleCRUDApp.controller.EntityController.GO_BACK_TO_MAIN_MENU_COMMAND;

public class LabelView extends BaseEntityView {

    private LabelController labelController;

    private void ensureControllerIsInitialized() {
        if (labelController == null) {
            labelController = ApplicationContext.getInstance().getLabelController();
        }
    }

    @Override
    public void startMenu() throws SQLException {
        ensureControllerIsInitialized();

        showConsoleEntityMenu(labelController.getEntityClassName());

        String inputCommand = getUserInputCommand();
        while (!inputCommand.equals(GO_BACK_TO_MAIN_MENU_COMMAND)) {
            labelController.executeMenuUserCommand(inputCommand);
            inputCommand = getUserInputCommand();
        }
        labelController.exit();
        showConsoleMainMenu();
    }

    public String promptNewLabelNameFromUser() {
        return getUserInput("\nPlease input new Label Name: ");
    }

}
