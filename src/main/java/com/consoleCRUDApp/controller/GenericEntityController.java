package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Entity;
import com.consoleCRUDApp.service.GenericEntityService;
import com.consoleCRUDApp.view.BaseEntityView;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
public abstract class GenericEntityController<T extends Entity,
                                              S extends GenericEntityService<T>,
                                              V extends BaseEntityView>
                                    implements EntityController {

    protected final S service;
    protected final V baseEntityView;

    public abstract void showEntitiesListFormatted(List<T> activeEntities);
    public abstract void saveNewEntity(T entity, String operationName) throws SQLException;
    public abstract T prepareNewEntity();
    public abstract T requestEntityUpdatesFromUser(Long id) throws SQLException;

    @Override
    public void showMenu() throws SQLException {
        baseEntityView.startMenu();
    }

    @Override
    public void createAndSaveNewEntity() throws SQLException {
        baseEntityView.showInConsole("Starting CREATE new entity...");
        String createOperationName = "CREATE";

        T entity = prepareNewEntity();

        String entityClassSimpleName = entity.getClass().getSimpleName();

        showInfoMessageYouAreAboutTo(createOperationName, entityClassSimpleName, entity);
        if (baseEntityView.userConfirmsOperation()) {
            saveNewEntity(entity, createOperationName);
        } else {
            showInfoMessageOperationCancelled(createOperationName, entityClassSimpleName);
        }
    }

    @Override
    public void findEntityById() throws SQLException {
        baseEntityView.showInConsole("Starting find entity by ID...");
        Long id = baseEntityView.promptEntityIdFromUser();
        service.findById(id).ifPresentOrElse(
                entity -> {
                    baseEntityView.printlnToConsole("\nFound " + getEntityClassName() + " entity:");
                    showEntitiesListFormatted(List.of(entity));
                },
                () -> showInfoMessageEntityWithIdNotFound(id)
        );
    }

    @Override
    public void showAllActiveEntities() throws SQLException {
        baseEntityView.showInConsole("Starting show all entities...");
        List<T> activeEntities = service.findAll();

        if (!activeEntities.isEmpty()) {
            baseEntityView.printlnToConsole("\nFound " + activeEntities.size() + " entities.");
            baseEntityView.printlnToConsole(
                    "All " + service.getEntityClass().getSimpleName() + " entities: ");
            showEntitiesListFormatted(activeEntities);
        } else {
            baseEntityView.showInConsole("\nNo entities found!");
        }
    }

    @Override
    public void updateEntity() throws SQLException {
        baseEntityView.showInConsole("Starting UPDATE entity by ID...");
        Long id = baseEntityView.promptEntityIdFromUser();
        service.findById(id).ifPresentOrElse(
                entity -> {
                    try {
                        processEntityUpdate(entity);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> showInfoMessageEntityWithIdNotFound(id)
        );
    }

    private void processEntityUpdate(T entity) throws SQLException {
        String updateOperationName = "UPDATE";
        String entityClassSimpleName = entity.getClass().getSimpleName();

        showInfoMessageYouAreAboutTo(updateOperationName, entityClassSimpleName, entity);
        if (baseEntityView.userConfirmsOperation()) {
            T updatedEntity = requestEntityUpdatesFromUser(entity.getId());

            service.update(updatedEntity);

            showInfoMessageEntityOperationFinishedSuccessfully(updateOperationName, entity.getId());
        } else {
            showInfoMessageOperationCancelled(updateOperationName, entityClassSimpleName);
        }
    }

    @Override
    public void deleteEntityById() throws SQLException {
        baseEntityView.showInConsole("Starting DELETE entity by ID...");
        Long id = baseEntityView.promptEntityIdFromUser();
        service.findById(id).ifPresentOrElse(
                entity -> {
                    try {
                        processEntityDeletion(entity, id);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> showInfoMessageEntityWithIdNotFound(id)
        );
    }

    private void processEntityDeletion(T entity, Long id) throws SQLException {
        String operationName = "DELETE";
        String entityClassSimpleName = entity.getClass().getSimpleName();
        showInfoMessageYouAreAboutTo(operationName, entityClassSimpleName, entity);

        if (baseEntityView.userConfirmsOperation()) {
            deleteEntity(operationName, id);
        } else {
            showInfoMessageOperationCancelled(operationName, entityClassSimpleName);
        }
    }

    private void deleteEntity(String operationName, Long id) throws SQLException {
        try {
            service.deleteById(id);
            showInfoMessageEntityOperationFinishedSuccessfully(operationName, id);
        } catch (NoSuchElementException e) {
            System.out.println("\n>>> ERROR: ");
        }
    }

    @Override
    public void exit() {
        baseEntityView.outputExitedFromMainMenu(getEntityClassName());
    }


    private void showInfoMessageEntityWithIdNotFound(Long id) {
        baseEntityView.outputEntityWithIdNotFound(id);
    }

    void showInfoMessageOperationCancelled(String currOperationName, String entityClassSimpleName) {
        baseEntityView.outputEntityOperationCancelled(currOperationName, entityClassSimpleName);
    }

    private void showInfoMessageYouAreAboutTo(String operationName, String entityClassName, T entity) {
        baseEntityView.outputYouAreAboutTo(operationName, entityClassName, entity);
    }

    void showInfoMessageEntityOperationFinishedSuccessfully(String operationName, Long id) {
        String entityClassSimpleName = service.getEntityClass().getSimpleName();
        baseEntityView.outputEntityOperationFinishedSuccessfully(operationName, entityClassSimpleName, id);
    }

}

