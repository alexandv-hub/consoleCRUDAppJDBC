package com.consoleCRUDApp.view;

import com.consoleCRUDApp.model.DBEntity;

public abstract class BaseEntityView implements BaseView {

    private static final String ENTER_THE_ID_OF_THE_ENTITY = "\nPlease enter the ID of the entity: ";

    public abstract void startMenu();

    abstract String toStringTableViewWithIds(DBEntity entity);
    abstract String toStringTableViewEntityNoIds(DBEntity entity);

    public void showConsoleEntityMenu(String entityName) {
        System.out.println(
                "\n__________________________________" +
                "\n" + entityName + " entity CRUD-commands menu: \n" +
                "Please select an action:\n" +
                "1. Create new entity\n" +
                "2. Find entity by ID\n" +
                "3. Show all entities\n" +
                "4. Update existing entity by ID\n" +
                "5. Delete entity by ID\n" +
                "__________________________________\n" +
                "6. Go back to Main menu");
    }

    public Long promptEntityIdFromUser() {
        Long entityId = null;

        while (entityId == null) {
            String inputCommand = getUserInputNumeric(ENTER_THE_ID_OF_THE_ENTITY).trim();
            entityId = Long.parseLong(inputCommand);
        }
        return entityId;
    }


    public void outputEntityWithIdNotFound(String entityName, Long id) {
        showInConsole("\n" + entityName + " entity with ID '" + id + "' not found!\n");
    }

    public void outputEntityOperationCancelled(String currOperationName, String entityName) {
        showInConsole("\n" + entityName + " entity " + currOperationName + " has been cancelled!\n");
    }

    public void outputYouAreAboutTo(String operationName,
                                    String entityName,
                                    DBEntity entity) {
        showInConsole(
                "\nYou are about to " + operationName + " " + entityName + " entity: " + toStringTableViewEntityNoIds(entity));
    }

    public void outputEntityOperationFinishedSuccessfully(String operationName,
                                                          String entityName,
                                                          Long id) {
        showInConsole(
                "\nOperation " + operationName + " " + entityName + " entity with ID='" + id
                + "' finished successfully!");
    }

    public void outputMessageEntityAlreadyExists(String entityName,
                                                 String entityFieldName,
                                                 String entityMainFieldStringValue) {
        showInConsole(
                "\n" + entityName + " entity with " + entityFieldName + " '" + entityMainFieldStringValue
                + "' already exits in repository!");
    }

    public void outputExitedFromMainMenu(String repositoryClassName) {
        showInConsole("Exited from " + repositoryClassName + " menu.\n");
    }

}
