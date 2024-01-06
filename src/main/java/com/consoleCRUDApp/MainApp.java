package com.consoleCRUDApp;

import com.consoleCRUDApp.view.MainView;

import static com.consoleCRUDApp.config.FlywayDBConfig.applyFlywayMigrations;

public class MainApp {

    public static void main(String[] args) {
        applyFlywayMigrations();
        ApplicationContext context = ApplicationContext.getInstance();
        MainView mainMenuView = new MainView();
        mainMenuView.runMainMenu(context);
    }
}
