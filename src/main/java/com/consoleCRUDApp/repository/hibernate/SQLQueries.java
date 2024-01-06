package com.consoleCRUDApp.repository.hibernate;

class SQLQueries {

    private SQLQueries() {
    }

    static final String SQL_INSERT_INTO_POST_LABEL =
            "INSERT INTO post_label (post_id, label_id, status) VALUES (:postId, :labelId, :status) " +
                    "ON DUPLICATE KEY UPDATE status = :status";
    static final String SQL_UPDATE_POST_LABEL_SET_STATUS_BY_POST_ID =
            "UPDATE post_label SET status = :status WHERE post_id = :postId AND status != :status";

    static final String SQL_INSERT_INTO_WRITER_POST =
            "INSERT INTO writer_post (writer_id, post_id, status) VALUES (:writerId, :postId, :status) " +
                    "ON DUPLICATE KEY UPDATE status = :status";
    static final String SQL_UPDATE_WRITER_POST_SET_STATUS_BY_POST_ID =
            "UPDATE writer_post SET status = :status WHERE writer_id = :writerId AND status != :status";
}
