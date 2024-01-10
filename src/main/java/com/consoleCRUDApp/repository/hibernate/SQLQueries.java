package com.consoleCRUDApp.repository.hibernate;

class SQLQueries {

    private SQLQueries() {
    }

    static final String SQL_INSERT_INTO_POST_LABEL =
            "INSERT INTO post_label (post_id, label_id, status) VALUES (:postId, :labelId, CAST(:status AS status_type)) " +
                    "ON CONFLICT (post_id, label_id) DO UPDATE SET status = CAST(:status AS status_type);";

    static final String SQL_UPDATE_POST_LABEL_SET_STATUS_BY_POST_ID =
            "UPDATE post_label SET status = CAST(:status AS status_type) " +
                    "WHERE post_id = :postId AND status != CAST(:status AS status_type);";

    static final String SQL_INSERT_INTO_WRITER_POST =
            "INSERT INTO writer_post (writer_id, post_id, status) VALUES (:writerId, :postId, CAST(:status AS status_type)) " +
                    "ON CONFLICT (writer_id, post_id) DO UPDATE SET status = CAST(:status AS status_type);";

    static final String SQL_UPDATE_WRITER_POST_SET_STATUS_BY_POST_ID =
            "UPDATE writer_post SET status = CAST(:status AS status_type) " +
                    "WHERE writer_id = :writerId AND status != CAST(:status AS status_type);";
}
