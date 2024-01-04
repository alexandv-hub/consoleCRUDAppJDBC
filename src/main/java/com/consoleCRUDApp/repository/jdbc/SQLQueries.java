package com.consoleCRUDApp.repository.jdbc;

class SQLQueries {

    static final String SQL_SELECT_ALL_LABELS = "SELECT * FROM PROSELYTE_JDBC_DB.label WHERE status != ?";
    static final String SQL_INSERT_LABEL =
            "INSERT INTO PROSELYTE_JDBC_DB.label (name, status) VALUES (?, ?) " +
            "ON DUPLICATE KEY UPDATE status = VALUES(status)";
    static final String SQL_SELECT_ID_FROM_LABEL_BY_NAME = "SELECT id FROM PROSELYTE_JDBC_DB.label WHERE name = ?";
    static final String SQL_SELECT_ID_FROM_LABEL_BY_NAME_AND_STATUS = "SELECT id FROM PROSELYTE_JDBC_DB.label WHERE name = ? AND status != ?";
    static final String SQL_SELECT_ALL_FROM_LABEL_BY_ID_AND_STATUS = "SELECT * FROM PROSELYTE_JDBC_DB.label WHERE id = ? AND status = ?";
    static final String SQL_UPDATE_LABEL_NAME_BY_ID = "UPDATE PROSELYTE_JDBC_DB.label SET name = ? WHERE id = ?";
    static final String SQL_UPDATE_LABEL_STATUS_BY_ID = "UPDATE PROSELYTE_JDBC_DB.label SET status = ? WHERE id = ?";

    static final String SQL_INSERT_POST =
            "INSERT INTO PROSELYTE_JDBC_DB.post (content, created, post_status, status) VALUES (?, ?, ?, ?)";
    static final String SQL_UPDATE_POST_LABELS_STATUS =
            "UPDATE PROSELYTE_JDBC_DB.post_label SET status = ? WHERE post_id = ?";
    static final String SQL_INSERT_POST_LABEL =
            "INSERT INTO PROSELYTE_JDBC_DB.post_label (post_id, label_id, status) VALUES (?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE status = VALUES(status)";
    static final String SQL_SELECT_POST_BY_ID =
            "SELECT p.*, l.id AS label_id, l.name AS label_name FROM PROSELYTE_JDBC_DB.post p " +
            "LEFT JOIN PROSELYTE_JDBC_DB.post_label pl ON p.id = pl.post_id AND pl.status != ?" +
            "LEFT JOIN PROSELYTE_JDBC_DB.label l ON pl.label_id = l.id AND l.status != ?" +
            "WHERE p.id = ? " +
            "AND p.status != ? ";
    static final String SQL_SELECT_ALL_POSTS =
            "SELECT p.*, l.id AS label_id, l.name AS label_name FROM PROSELYTE_JDBC_DB.post p " +
            "LEFT JOIN PROSELYTE_JDBC_DB.post_label pl ON p.id = pl.post_id AND pl.status != ?" +
            "LEFT JOIN PROSELYTE_JDBC_DB.label l ON pl.label_id = l.id " +
            "AND p.status != ? " +
            "AND l.status != ? ";
    static final String SQL_UPDATE_POST_BY_ID =
            "UPDATE PROSELYTE_JDBC_DB.post SET content = ?, updated = ?, post_status = ? WHERE id = ?";
    static final String SQL_UPDATE_POST_STATUS_BY_ID =
            "UPDATE PROSELYTE_JDBC_DB.post SET status = ?, post_status = ? WHERE id = ?";

    static final String SQL_INSERT_WRITER =
            "INSERT INTO PROSELYTE_JDBC_DB.writer (firstName, lastName, status) VALUES (?, ?, ?)";
    static final String SQL_INSERT_WRITER_POSTS =
            "INSERT INTO PROSELYTE_JDBC_DB.writer_post (writer_id, post_id, status) VALUES (?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE status = VALUES(status)";
    static final String SQL_SELECT_WRITER_BY_ID =
            "SELECT w.*, p.id AS post_id, p.content, p.created, p.updated, p.post_status, p.status " +
            "FROM PROSELYTE_JDBC_DB.writer w " +
            "LEFT JOIN PROSELYTE_JDBC_DB.writer_post wp ON w.id = wp.writer_id " +
            "LEFT JOIN PROSELYTE_JDBC_DB.post p ON wp.post_id = p.id " +
            "WHERE w.status != ? AND (p.status != ? OR p.status IS NULL)";
    static final String SQL_SELECT_ALL_WRITERS =
            "SELECT w.*, p.id AS post_id, p.content, p.created, p.updated, p.post_status, p.status " +
            "FROM PROSELYTE_JDBC_DB.writer w " +
            "LEFT JOIN PROSELYTE_JDBC_DB.writer_post wp ON w.id = wp.writer_id " +
            "LEFT JOIN PROSELYTE_JDBC_DB.post p ON wp.post_id = p.id " +
            "WHERE w.status != ? AND p.status != ?";
    static final String SQL_UPDATE_WRITER_BY_ID =
            "UPDATE PROSELYTE_JDBC_DB.writer SET firstName = ?, lastName = ? WHERE id = ?";
    static final String SQL_UPDATE_WRITER_STATUS_BY_ID =
            "UPDATE PROSELYTE_JDBC_DB.writer SET status = ? WHERE id = ?";
}

