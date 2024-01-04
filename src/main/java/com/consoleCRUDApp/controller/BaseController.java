package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.view.MainView;

public interface BaseController {

    MainView MAIN_VIEW = new MainView();

    void executeMenuUserCommand(String inputCommand);
    void exit();
}

