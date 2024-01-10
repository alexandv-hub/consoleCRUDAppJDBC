package com.consoleCRUDApp.view.messages;

public final class ErrorMessages {

    private ErrorMessages() {
    }

    public static class Database {
        public static final String SORRY_UNABLE_TO_FIND_FILE = "Sorry, unable to find file: ";
        public static final String DB_CONNECTION_FAILED = "DB connection failed!";
        public static final String NO_DATABASE_FOUND = "No database found!";
        public static final String DATABASE_CREATION_FAILED = "Database creation failed!";
        public static final String FLYWAY_MIGRATION_UPDATE_FAILED = "Flyway migration update failed!";
        public static final String SORRY_FAILED_TO_INITIALIZE_APPLICATION_CONTEXT_AND_START_THE_APPLICATION = "Failed to initialize ApplicationContext and start the application: ";
    }

    public static class Inputs {
        public static final String DEFAULT_RETURN_YOUR_INPUT_IS_NOT_A_COMMAND = "default return: your input is not a command\n";
        public static final String INVALID_INPUT_PLEASE_ENTER_A_NUMERIC_VALUE = "\nInvalid input! Please input a numeric value.\n";
        public static final String EMPTY_INPUT_NOT_ALLOWED = "Empty input not allowed!\n";
        public static final String ENTER_ONLY_Y_OR_N_COMMAND = "Command not valid. Enter only 'y' or 'n' command.";
    }

    public static class Entity {
        public static final String SAVE_NEW_LABEL_OPERATION_FAILED = "\nSave new Label operation failed!!!\n";
        public static final String SAVE_NEW_POST_OPERATION_FAILED = "\nSave new Post operation failed!!!\n";
        public static final String INCORRECT_INPUT_THE_STATUS_MUST_BE_1_FOR_ACTIVE_OR_2_FOR_UNDER_REVIEW = "\nIncorrect input! The status must be '1' for ACTIVE or '2' for UNDER_REVIEW.";
        public static final String INCORRECT_ID_INPUT_WRITER_POST_LIST_CONTAINS_NO_POST_WITH_THIS_ID = "\nIncorrect ID input! Writer post list contains no post with this ID.\n";
        public static final String SAVE_NEW_WRITER_OPERATION_FAILED = "\nSave new Writer operation failed!!!\n";
    }
}
