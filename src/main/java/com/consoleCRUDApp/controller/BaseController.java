package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.view.MainView;

import java.sql.SQLException;

public interface BaseController {

    MainView MAIN_VIEW = new MainView();

    void executeMenuUserCommand(String inputCommand) throws SQLException;
    void exit();
}

