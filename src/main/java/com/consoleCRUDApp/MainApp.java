package com.consoleCRUDApp;

import com.consoleCRUDApp.view.MainView;

import static com.consoleCRUDApp.config.LiquibaseDBCreator.createDbIfNotExist;

public class MainApp {

    public static void main(String[] args) {
        createDbIfNotExist();
        ApplicationContext context = ApplicationContext.getInstance();
        MainView mainMenuView = new MainView();
        mainMenuView.runMainMenu(context);
    }
}
