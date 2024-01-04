package com.consoleCRUDApp.model;

public interface Entity {

    Long getId();
    void setId(Long nextId);

    Status getStatus();
    void setStatus(Status status);
}
