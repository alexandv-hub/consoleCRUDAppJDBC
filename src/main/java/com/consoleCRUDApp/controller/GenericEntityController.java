package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.DBEntity;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.service.GenericEntityService;
import com.consoleCRUDApp.view.BaseEntityView;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
public abstract class GenericEntityController<T extends DBEntity,
                                              S extends GenericEntityService<T>,
                                              V extends BaseEntityView>
                                    implements EntityController {

    static final String CREATE = "CREATE";
    static final String SAVE = "SAVE";
    static final String UPDATE = "UPDATE";
    static final String DELETE = "DELETE";

    protected final S service;
    protected final V baseEntityView;

    abstract void showEntitiesListFormatted(List<T> activeEntities);
    abstract void saveNewEntity(T entity);
    abstract T prepareNewEntity();
    abstract T requestEntityUpdatesFromUser(Long id);

    @Override
    public void showMenu() {
        baseEntityView.startMenu();
    }

    @Override
    public void createAndSaveNewEntity() {
        baseEntityView.showInConsole("\nStarting " + CREATE + " new " + getEntityName() + " entity...\n");

        T entity = prepareNewEntity();

        showInfoMessageYouAreAboutTo(CREATE, getEntityName(), entity);
        if (baseEntityView.userConfirmsOperation()) {
            saveNewEntity(entity);
        } else {
            showInfoMessageOperationCancelled(CREATE, getEntityName());
        }
        baseEntityView.showConsoleEntityMenu(getEntityName());
    }

    @Override
    public void findEntityById() {
        baseEntityView.showInConsole("Starting find " + getEntityName() + " entity by ID...\n");

        Long id = baseEntityView.promptEntityIdFromUser();

        service.findById(id).ifPresentOrElse(
                entity -> {
                    baseEntityView.printlnToConsole("\nFound " + getEntityName() + " entity:");
                    showEntitiesListFormatted(List.of(entity));
                },
                () -> showInfoMessageEntityWithIdNotFound(id)
        );
        baseEntityView.showConsoleEntityMenu(getEntityName());
    }

    @Override
    public void showAllActiveEntities() {
        baseEntityView.showInConsole("\nStarting show all " + getEntityName() + " entities...\n");

        List<T> activeEntities = service.findAll();

        if (!activeEntities.isEmpty()) {
            baseEntityView.printlnToConsole("\nFound " + activeEntities.size() + " " + getEntityName() + " records.");
            baseEntityView.printlnToConsole("All " + getEntityName() + " entities: ");
            showEntitiesListFormatted(activeEntities);
        } else {
            baseEntityView.showInConsole("\nNo " + getEntityName() + " entities found!");
        }
        baseEntityView.showConsoleEntityMenu(getEntityName());
    }

    @Override
    public void updateEntity() {
        baseEntityView.showInConsole("Starting " + UPDATE + " " + getEntityName() + " entity by ID...\n");

        Long id = baseEntityView.promptEntityIdFromUser();

        service.findById(id).ifPresentOrElse(
                entity -> processEntityUpdate(entity),
                () -> showInfoMessageEntityWithIdNotFound(id)
        );
        baseEntityView.showConsoleEntityMenu(getEntityName());
    }

    void processEntityUpdate(T entity) {

        showInfoMessageYouAreAboutTo(UPDATE, getEntityName(), entity);
        if (baseEntityView.userConfirmsOperation()) {
            T updatedEntity = requestEntityUpdatesFromUser(entity.getId());
            updatedEntity.setStatus(Status.ACTIVE);

            service.update(updatedEntity);

            showInfoMessageEntityOperationFinishedSuccessfully(UPDATE, getEntityName(), entity.getId());
        } else {
            showInfoMessageOperationCancelled(UPDATE, getEntityName());
        }
    }

    @Override
    public void deleteEntityById() {
        baseEntityView.showInConsole("Starting " + DELETE + " " + getEntityName() + " entity by ID...\n");

        Long id = baseEntityView.promptEntityIdFromUser();

        service.findById(id).ifPresentOrElse(
                entity -> processEntityDeletion(entity, id),
                () -> showInfoMessageEntityWithIdNotFound(id)
        );
        baseEntityView.showConsoleEntityMenu(getEntityName());
    }

    private void processEntityDeletion(T entity, Long id) {
        baseEntityView.outputYouAreAboutTo(DELETE, getEntityName(), entity);
        if (baseEntityView.userConfirmsOperation()) {
            deleteEntity(id);
        } else {
            showInfoMessageOperationCancelled(DELETE, getEntityName());
        }
    }

    private void deleteEntity(Long id) {
        try {
            service.deleteById(id);
            showInfoMessageEntityOperationFinishedSuccessfully(DELETE, getEntityName(), id);
        } catch (NoSuchElementException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
    }

    @Override
    public void exit() {
        baseEntityView.outputExitedFromMainMenu(getEntityName());
    }


    private void showInfoMessageEntityWithIdNotFound(Long id) {
        baseEntityView.outputEntityWithIdNotFound(getEntityName(), id);
    }

    void showInfoMessageOperationCancelled(String currOperationName, String entityName) {
        baseEntityView.outputEntityOperationCancelled(currOperationName, entityName);
    }

    void showInfoMessageYouAreAboutTo(String operationName, String entityName, DBEntity entity) {
        baseEntityView.outputYouAreAboutTo(operationName, entityName, entity);
    }

    void showInfoMessageEntityOperationFinishedSuccessfully(String operationName, String entityName, Long id) {
        baseEntityView.outputEntityOperationFinishedSuccessfully(operationName,entityName, id);
    }
}

