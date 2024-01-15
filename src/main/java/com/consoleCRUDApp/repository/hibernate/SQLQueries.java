package com.consoleCRUDApp.repository.hibernate;

public final class SQLQueries {

    private SQLQueries() {
    }

    public static final String POSTGRES_SQL_SELECT_DATNAME_FROM_PG_DATABASE = "SELECT datname FROM pg_database;";
    public static final String SQL_CREATE_DATABASE = "CREATE DATABASE ";

    static final String HQL_FROM_LABEL = "from Label";
    static final String HQL_FROM_LABEL_WHERE_NAME = "from Label where name = :name";

    static final String HQL_FROM_POST_LEFT_JOIN_FETCH_LABELS = "from Post p LEFT JOIN FETCH p.labels ORDER BY p.id ASC";

    static final String HQL_FROM_WRITER_LEFT_JOIN_FETCH_POSTS = "from Writer w LEFT JOIN FETCH w.posts ORDER BY w.id ASC";

}
