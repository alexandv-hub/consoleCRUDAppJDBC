package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Entity;
import com.consoleCRUDApp.service.GenericEntityService;
import com.consoleCRUDApp.view.BaseEntityView;
import lombok.AllArgsConstructor;

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
    public abstract void saveNewEntity(T entity, String operationName);
    public abstract T prepareNewEntity();
    public abstract T requestEntityUpdatesFromUser(Long id);

    @Override
    public void showMenu() {
        baseEntityView.startMenu();
    }

    @Override
    public void createAndSaveNewEntity() {
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
    public void findEntityById() {
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
    public void showAllActiveEntities() {
        baseEntityView.showInConsole("Starting show all entities...");
        List<T> activeEntities = service.findAll();

        if (!activeEntities.isEmpty()) {
            baseEntityView.printlnToConsole("\nFound " + activeEntities.size() + " entities.");
            baseEntityView.printlnToConsole(
                    "All entities: ");
            showEntitiesListFormatted(activeEntities);
        } else {
            baseEntityView.showInConsole("\nNo entities found!");
        }
    }

    @Override
    public void updateEntity() {
        baseEntityView.showInConsole("Starting UPDATE entity by ID...");
        Long id = baseEntityView.promptEntityIdFromUser();
        service.findById(id).ifPresentOrElse(
                entity -> processEntityUpdate(entity),
                () -> showInfoMessageEntityWithIdNotFound(id)
        );
    }

    private void processEntityUpdate(T entity) {
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
    public void deleteEntityById() {
        baseEntityView.showInConsole("Starting DELETE entity by ID...");
        Long id = baseEntityView.promptEntityIdFromUser();
        service.findById(id).ifPresentOrElse(
                entity -> processEntityDeletion(entity, id),
                () -> showInfoMessageEntityWithIdNotFound(id)
        );
    }

    private void processEntityDeletion(T entity, Long id) {
        String operationName = "DELETE";
        String entityClassSimpleName = entity.getClass().getSimpleName();
        baseEntityView.outputYouAreAboutTo(operationName, entityClassSimpleName, entity);

        if (baseEntityView.userConfirmsOperation()) {
            deleteEntity(operationName, id);
        } else {
            showInfoMessageOperationCancelled(operationName, entityClassSimpleName);
        }
    }

    private void deleteEntity(String operationName, Long id) {
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

    private void showInfoMessageYouAreAboutTo(String operationName, String entityClassName, Entity entity) {
        baseEntityView.outputYouAreAboutTo(operationName, entityClassName, entity);
    }

    void showInfoMessageEntityOperationFinishedSuccessfully(String operationName, Long id) {
        baseEntityView.outputEntityOperationFinishedSuccessfully(operationName, id);
    }
}

