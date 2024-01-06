package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.service.impl.LabelServiceImpl;
import com.consoleCRUDApp.view.LabelView;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.ColumnData;

import java.util.List;
import java.util.Optional;

public class LabelController
        extends GenericEntityController<Label, LabelServiceImpl, LabelView> {

    private static final String LABEL_ENTITY_NAME = "LABEL";

    private static final String SAVE_NEW_LABEL_OPERATION_FAILED = "\nSave new Label operation failed!!!\n";
    private static final String PLEASE_INPUT_THE_LABEL_NEW_NAME = "\nPlease input the Label new Name: ";

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
    public void saveNewEntity(Label newLabelToSave, String operationName) {
        if (!service.isLabelExistInRepository(newLabelToSave)) {
            Optional<Label> savedLabel = service.save(newLabelToSave);
            savedLabel.ifPresentOrElse(
                    label -> showInfoMessageEntityOperationFinishedSuccessfully(operationName, getEntityName(), label.getId()),
                    () -> {
                        labelView.showInConsole(SAVE_NEW_LABEL_OPERATION_FAILED);
                        showMenu();
                    }
            );
        }
        else {
            labelView.outputMessageEntityAlreadyExists(
                    getEntityName(),
                    "Name",
                    newLabelToSave.getName());
        }
    }

    @Override
    public Label requestEntityUpdatesFromUser(Long id) {
        String updatedLabelName = labelView.getUserInputNotEmpty(PLEASE_INPUT_THE_LABEL_NEW_NAME);

        return Label.builder()
                .id(id)
                .name(updatedLabelName)
                .build();
    }

    @Override
    public void showEntitiesListFormatted(List<Label> activeEntities) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Label>> columns = labelView.getColumnDataWithIds();
        String rend = AsciiTable.getTable(borderStyle, activeEntities, columns);
        labelView.showInConsole(rend);
    }

    @Override
    public String getEntityName() {
        return LABEL_ENTITY_NAME;
    }
}
