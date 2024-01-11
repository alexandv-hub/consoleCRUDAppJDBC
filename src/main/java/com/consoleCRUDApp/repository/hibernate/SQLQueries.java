package com.consoleCRUDApp.repository.hibernate;

public final class SQLQueries {

    private SQLQueries() {
    }

    public static final String POSTGRES_SQL_SELECT_DATNAME_FROM_PG_DATABASE = "SELECT datname FROM pg_database;";
    public static final String SQL_CREATE_DATABASE = "CREATE DATABASE ";

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

    static final String HQL_FROM_LABEL_WHERE_STATUS = "from Label where status = :status";
    static final String HQL_FROM_LABEL_WHERE_NAME_AND_STATUS = "from Label where name = :name and status = :status";
    static final String HQL_FROM_LABEL_WHERE_ID_AND_STATUS = "from Label where id = :id and status = :status";
    static final String HQL_UPDATE_LABEL_SET_STATUS_WHERE_ID = "update Label set status = :status where id = :id";

    public static final String HQL_FROM_POST_WHERE_STATUS = "from Post where status = :status ";
    public static final String FROM_POST_WHERE_ID_AND_STATUS = "from Post where id = :id and status = :status";
    public static final String HQL_UPDATE_POST_SET_STATUS_POST_STATUS_WHERE_ID = "update Post set status = :status, postStatus = :postStatus where id = :id";

    public static final String HQL_FROM_WRITER_WHERE_ID_AND_STATUS = "from Writer where id = :id and status = :status";
    public static final String HQL_FROM_WRITER_WHERE_STATUS = "from Writer where status = :status ";
    public static final String HQL_UPDATE_WRITER_SET_STATUS_WHERE_ID = "update Writer set status = :status where id = :id";

}
