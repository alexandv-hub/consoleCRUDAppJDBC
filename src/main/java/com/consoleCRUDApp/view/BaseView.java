package com.consoleCRUDApp.view;

import java.util.Scanner;

public interface BaseView {

    Scanner scanner = new Scanner(System.in);

    String CONFIRM_THE_OPERATION_Y_N = "\nPlease confirm the operation: y/n: ";
    String ENTER_ONLY_Y_OR_N_COMMAND = "Command not valid. Enter only 'y' or 'n' command.";
    String ENTER_YOUR_COMMAND = "\n\nEnter your command: ";

    String YES_USER_COMMAND = "y";
    String NO_USER_COMMAND = "n";

    default void showConsoleMainMenu() {
        System.out.println(
                "\nMain menu:\n" +
                    "Please select an action:\n" +
                    "1. Go to Writers menu\n" +
                    "2. Go to Posts menu\n" +
                    "3. Go to Labels menu\n" +
                    "_____________________________\n" +
                    "4. Exit");
    }

    default void showInConsole(String outputMessage) {
        System.out.print(outputMessage);
    }

    default void printlnToConsole(String outputMessage) {
        System.out.println(outputMessage);
    }

    default String getUserInputCommand() {
        showInConsole(ENTER_YOUR_COMMAND);
        return scanner.nextLine().trim();
    }

    default String getUserInput(String request) {
        showInConsole(request);
        return scanner.nextLine().trim();
    }

    default String requestYesOrNoFromUser() {
        while (true) {
            String userAnswer = getUserInput(CONFIRM_THE_OPERATION_Y_N).trim();

            if (userAnswer.equalsIgnoreCase(YES_USER_COMMAND)
                    || userAnswer.equalsIgnoreCase(NO_USER_COMMAND)) {
                return userAnswer;
            } else {
                showInConsole(ENTER_ONLY_Y_OR_N_COMMAND);
            }
        }
    }

    default boolean userConfirmsOperation() {
        String yesOrNoUserAnswer = requestYesOrNoFromUser();
        return yesOrNoUserAnswer.equalsIgnoreCase(YES_USER_COMMAND);
    }

    default void close() {
        scanner.close();
    }

}
