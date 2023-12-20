package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.view.BaseEntityView;

import java.util.ArrayList;
import java.util.List;

public interface LabelNamesInputDialog {

    default List<Label> promptPostLabelsNamesFromUser(BaseEntityView baseEntityView) {
        List<Label> newPostLabels = new ArrayList<>();

        int labelCounter = 1;
        String postLabelName =  promptLabelNameFromUser(labelCounter, baseEntityView);

        while (!postLabelName.isBlank()) {
            Label newLabelEntity = Label.builder()
                    .name(postLabelName)
                    .status(Status.ACTIVE)
                    .build();

            newPostLabels.add(newLabelEntity);

            labelCounter++;
            postLabelName = promptLabelNameFromUser(labelCounter, baseEntityView);
        }
        return newPostLabels;
    }

    default String promptLabelNameFromUser(int labelCounter, BaseEntityView baseEntityView) {
        String labelNameRequest = "Please input the Post Label " + labelCounter + " Name (or empty input to stop entering the new writer post Labels): ";
        return baseEntityView.getUserInput(labelNameRequest).trim();
    }

}
