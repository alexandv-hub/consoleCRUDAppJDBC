package com.consoleCRUDApp.view.messages;

public final class SystemMessages {

    private SystemMessages() {
    }

    public static class Database {
        public static final String CONNECTED_TO_POSTGRE_SQL_SERVER_SUCCESSFULLY = "Connected to PostgreSQL Server successfully...";
        public static final String DATABASE_SUCCESSFULLY_CREATED = "Database successfully created.";
    }

    public static final String WELCOME_TO_THE_CONSOLE_CRUD_APPLICATION = "\nWelcome to the console CRUD Application!";
    public static final String CONSOLE_CRUD_APPLICATION_TERMINATED_SUCCESSFULLY = "Console CRUD Application terminated successfully.";

    public static class Inputs {
        public static final String ENTER_YOUR_COMMAND = "\nEnter your command: ";
        public static final String CONFIRM_THE_OPERATION_Y_N = "\n\nPlease confirm the operation: y/n: ";
    }

    public static class Entity {
        public static final String PLEASE_INPUT_THE_LABEL_NEW_NAME = "\nPlease input the Label new Name: ";
        public static final String INPUT_THE_NEW_POST_CONTENT = "\nPlease input the new Post Content: ";
        public static final String WOULD_YOU_LIKE_TO_UPDATE_THE_POST_LABELS = "\nWould you like to UPDATE the Post labels?";
        public static final String PLEASE_INPUT_THE_POST_NEW_POST_STATUS_1_ACTIVE_2_UNDER_REVIEW = "\nPlease input the Post new PostStatus ('1'-ACTIVE, '2'-UNDER_REVIEW): ";
        public static final String PLEASE_INPUT_THE_POST_NEW_CONTENT = "\nPlease input the Post new Content: ";
        public static final String PLEASE_INPUT_NEW_WRITER_S_FIRST_NAME = "\nPlease input new writer's First Name: ";
        public static final String PLEASE_INPUT_NEW_WRITER_S_LAST_NAME = "\nPlease input new writer's Last Name: ";
        public static final String WOULD_YOU_LIKE_TO_CREATE_WRITER_POST_LIST = "\nWould you like to CREATE Writer post list?";
        public static final String PLEASE_INPUT_WRITER_S_NEW_FIRST_NAME = "\nPlease input writer's new First Name: ";
        public static final String PLEASE_INPUT_WRITER_S_NEW_LAST_NAME = "Please input writer's new Last Name: ";
        public static final String WOULD_YOU_LIKE_TO_UPDATE_WRITER_S_POSTS = "\nWould you like to update Writer's posts?";
        public static final String WOULD_YOU_LIKE_TO_CONTINUE_TO_UPDATE_WRITER_S_POSTS_OR_SAVE_ALL_CHANGES = "\nWould you like to continue to update Writer's posts? (input 'n' => SAVE ALL CHANGES!)";
        public static final String ENTER_THE_ID_OF_THE_ENTITY = "\nPlease enter the ID of the entity: ";
        public static final String PLEASE_INPUT_NEW_LABEL_NAME = "\nPlease input new Label Name: ";
    }
}
