package com.consoleCRUDApp;

import com.consoleCRUDApp.view.MainView;

import java.sql.SQLException;

import static com.consoleCRUDApp.config.DBConnection.createDbIfNotExist;

public class MainApp {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        createDbIfNotExist();
        ApplicationContext context = ApplicationContext.getInstance();
        MainView mainMenuView = new MainView();
        mainMenuView.runMainMenu(context);
    }
}
