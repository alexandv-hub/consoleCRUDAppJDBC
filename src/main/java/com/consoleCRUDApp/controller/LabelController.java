package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.service.LabelServiceImpl;
import com.consoleCRUDApp.view.LabelView;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.ColumnData;

import java.sql.SQLException;
import java.util.List;

public class LabelController
                        extends GenericEntityController<Label, LabelServiceImpl, LabelView> {

    private final LabelView labelView = baseEntityView;

    public LabelController(LabelServiceImpl service, LabelView labelView) {
        super(service, labelView);
    }

    @Override
    public Label prepareNewEntity() {
        String newLabelName = labelView.promptNewLabelNameFromUser();

        return Label.builder()
                .name(newLabelName)
                .status(Status.ACTIVE)
                .build();
    }

    @Override
    public void saveNewEntity(Label newLabelToSave, String operationName) throws SQLException {
        if (!service.isLabelExistInRepository(newLabelToSave)) {
            Label savedLabel = service.save(newLabelToSave);
            if (service.findById(savedLabel.getId()).isPresent()) {
                showInfoMessageEntityOperationFinishedSuccessfully(operationName, savedLabel.getId());
            } else {
                labelView.showInConsole("\nSave new Label operation failed!!!\n");
                showMenu();
            }
        }
        else {
            String entityClassSimpleName = service.getEntityClass().getSimpleName();
            labelView.outputMessageEntityAlreadyExists(entityClassSimpleName, "Name", newLabelToSave.getName());
        }
    }

    @Override
    public Label requestEntityUpdatesFromUser(Long id) {
        String updatedLabelName = labelView.getUserInput("Please input the Label new Name: ");

        return Label.builder()
                .id(id)
                .name(updatedLabelName)
                .build();
    }

    @Override
    public void showEntitiesListFormatted(List<Label> activeEntities) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Label>> columns = Label.getColumnDataWithIds();
        String rend = AsciiTable.getTable(borderStyle, activeEntities, columns);
        labelView.showInConsole(rend);
    }

    @Override
    public String getEntityClassName() {
        return "LABEL";
    }
}
