package com.consoleCRUDApp.view;

import com.consoleCRUDApp.model.Entity;

import java.sql.SQLException;

public abstract class BaseEntityView implements BaseView {

    public static final String ENTER_THE_ID_OF_THE_ENTITY = "\nPlease enter the ID of the entity: ";
    public static final String ENTER_A_NUMERIC_VALUE = "\nNo input provided. Please enter a numeric value.";
    public static final String INVALID_ID_PLEASE_ENTER_A_NUMERIC_VALUE = "\nInvalid ID. Please enter a numeric value.";

    public abstract void startMenu() throws SQLException;

    public void showConsoleEntityMenu(String entityClassName) {
        System.out.println(
                "\n" + entityClassName + " entity CRUD-commands menu: \n" +
                "Please select an action:\n" +
                "1. Create new entity\n" +
                "2. Find entity by ID\n" +
                "3. Show all entities\n" +
                "4. Update existing entity by ID\n" +
                "5. Delete entity by ID\n" +
                "_____________________________\n" +
                "6. Go back to Main menu");
    }

    public Long promptEntityIdFromUser() {
        Long entityId = null;

        while (entityId == null) {
            String inputCommand = getUserInput(ENTER_THE_ID_OF_THE_ENTITY).trim();

            if (inputCommand.isEmpty()) {
                showInConsole(ENTER_A_NUMERIC_VALUE);
                continue;
            }

            try {
                entityId = Long.parseLong(inputCommand);
            } catch (NumberFormatException e) {
                showInConsole(INVALID_ID_PLEASE_ENTER_A_NUMERIC_VALUE);
            }
        }
        return entityId;
    }


    public void outputEntityWithIdNotFound(Long id) {
        showInConsole("\nEntity with ID '" + id + "' not found!");
    }

    public void outputEntityOperationCancelled(String currOperationName, String entityClassSimpleName) {
        showInConsole("\n" + entityClassSimpleName + " entity " + currOperationName + " has been cancelled!");
    }

    public void outputYouAreAboutTo(String operationName,
                                    String entityClassName,
                                    Entity entity) {
        showInConsole("\nYou are about to " + operationName + " " + entityClassName + " entity: " + entity.toStringTableViewEntityNoIds());
    }

    public void outputEntityOperationFinishedSuccessfully(String operationName,
                                                          String entityClassSimpleName,
                                                          Long id) {
        showInConsole(
                "\nOperation " + operationName + " " + entityClassSimpleName + " entity with ID='" + id
                + "' finished successfully!");
    }

    public void outputMessageEntityAlreadyExists(String entityClassSimpleName,
                                                 String entityFieldName,
                                                 String entityMainFieldStringValue) {
        showInConsole(
                "\n" + entityClassSimpleName + " entity with " + entityFieldName + " '" + entityMainFieldStringValue
                + "' already exits in repository!");
    }

    public void outputExitedFromMainMenu(String repositoryClassName) {
        showInConsole("Exited from " + repositoryClassName + " menu.");
    }

}
