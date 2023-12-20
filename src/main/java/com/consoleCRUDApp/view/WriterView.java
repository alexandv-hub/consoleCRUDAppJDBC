package com.consoleCRUDApp.view;

import com.consoleCRUDApp.ApplicationContext;
import com.consoleCRUDApp.controller.WriterController;

import java.sql.SQLException;

import static com.consoleCRUDApp.controller.EntityController.GO_BACK_TO_MAIN_MENU_COMMAND;

public class WriterView extends BaseEntityView {

    private WriterController writerController;

    private void ensureControllerIsInitialized() {
        if (writerController == null) {
            writerController = ApplicationContext.getInstance().getWriterController();
        }
    }

    @Override
    public void startMenu() throws SQLException {
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

}
