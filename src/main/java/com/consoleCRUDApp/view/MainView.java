package com.consoleCRUDApp.view;

import com.consoleCRUDApp.ApplicationContext;
import com.consoleCRUDApp.controller.MainMenuController;

import static com.consoleCRUDApp.controller.MainMenuController.EXIT_APP_NUM_COMMAND;

public class MainView implements BaseView {

    private static final String EXIT_APP_STR_COMMAND = "exit";

    public void runMainMenu(ApplicationContext context) {
        System.out.println(
                "\nWelcome to the console CRUD Application!");

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
        showInConsole("Console CRUD Application terminated successfully.");
    }

}
