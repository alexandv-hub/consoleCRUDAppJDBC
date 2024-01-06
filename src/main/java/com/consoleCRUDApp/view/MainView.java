package com.consoleCRUDApp.view;

import com.consoleCRUDApp.ApplicationContext;
import com.consoleCRUDApp.controller.MainMenuController;

import static com.consoleCRUDApp.controller.MainMenuController.EXIT_APP_NUM_COMMAND;

public class MainView implements BaseView {

    private static final String EXIT_APP_STR_COMMAND = "exit";
    private static final String WELCOME_TO_THE_CONSOLE_CRUD_APPLICATION = "\nWelcome to the console CRUD Application!";
    private static final String CONSOLE_CRUD_APPLICATION_TERMINATED_SUCCESSFULLY = "Console CRUD Application terminated successfully.";

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
