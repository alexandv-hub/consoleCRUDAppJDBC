package com.consoleCRUDApp.view;

import com.consoleCRUDApp.ApplicationContext;
import com.consoleCRUDApp.controller.MainMenuController;

import static com.consoleCRUDApp.controller.MainMenuController.EXIT_APP_NUM_COMMAND;
import static com.consoleCRUDApp.view.messages.SystemMessages.CONSOLE_CRUD_APPLICATION_TERMINATED_SUCCESSFULLY;
import static com.consoleCRUDApp.view.messages.SystemMessages.WELCOME_TO_THE_CONSOLE_CRUD_APPLICATION;

public class MainView implements BaseView {
    public void runMainMenu(ApplicationContext context) {
        System.out.println(
                WELCOME_TO_THE_CONSOLE_CRUD_APPLICATION);

        MainMenuController mainMenuController = new MainMenuController(context);

        showConsoleMainMenu();
        String inputCommand = getUserInputCommand();
        while (!inputCommand.equals(EXIT_APP_STR_COMMAND)
                && !inputCommand.equals(EXIT_APP_NUM_COMMAND)) {
            mainMenuController.executeMenuUserCommand(inputCommand);
            inputCommand = getUserInputCommand();
        }

        mainMenuController.exit();
        close();
        showInConsole(CONSOLE_CRUD_APPLICATION_TERMINATED_SUCCESSFULLY);
    }
}
