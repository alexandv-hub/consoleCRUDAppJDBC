package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.ApplicationContext;

import static com.consoleCRUDApp.view.messages.ErrorMessages.Inputs.DEFAULT_RETURN_YOUR_INPUT_IS_NOT_A_COMMAND;

public class MainMenuController implements BaseController {

    private static final String EXIT_FROM_MAIN_MENU = "Exit from Main menu";
    private static final String SHOW_WRITER_MENU_COMMAND = "1";
    private static final String SHOW_POST_MENU_COMMAND = "2";
    private static final String SHOW_LABEL_MENU_COMMAND = "3";
    public static final String EXIT_APP_NUM_COMMAND = "4";

    private final WriterController writerController;
    private final PostController postController;
    private final LabelController labelController;


    public MainMenuController(ApplicationContext context) {
        this.writerController = context.getWriterController();
        this.postController = context.getPostController();
        this.labelController = context.getLabelController();
    }

    @Override
    public void executeMenuUserCommand(String inputCommand) {
        switch (inputCommand) {
            case SHOW_WRITER_MENU_COMMAND:
                writerController.showMenu();
                break;
            case SHOW_POST_MENU_COMMAND:
                postController.showMenu();
                break;
            case SHOW_LABEL_MENU_COMMAND:
                labelController.showMenu();
                break;
            case EXIT_APP_NUM_COMMAND:
                exit();
                return;
            default:
                MAIN_VIEW.showInConsole(DEFAULT_RETURN_YOUR_INPUT_IS_NOT_A_COMMAND);
        }
    }

    public void exit() {
        MAIN_VIEW.showInConsole(EXIT_FROM_MAIN_MENU);
    }
}
