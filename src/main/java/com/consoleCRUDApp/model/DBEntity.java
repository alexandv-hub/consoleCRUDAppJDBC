package com.consoleCRUDApp.model;

public interface DBEntity {

    Long getId();
    void setId(Long nextId);

    Status getStatus();
    void setStatus(Status status);
}
