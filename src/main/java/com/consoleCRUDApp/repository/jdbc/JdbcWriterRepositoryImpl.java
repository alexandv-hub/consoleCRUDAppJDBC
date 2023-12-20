package com.consoleCRUDApp.repository.jdbc;

import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.PostStatus;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.model.Writer;
import com.consoleCRUDApp.repository.LabelRepository;
import com.consoleCRUDApp.repository.PostRepository;
import com.consoleCRUDApp.repository.WriterRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.*;
import java.util.*;

import static com.consoleCRUDApp.config.DBConnection.getConnection;
import static com.consoleCRUDApp.model.Status.DELETED;

@AllArgsConstructor
@Getter
public class JdbcWriterRepositoryImpl implements WriterRepository {

    private final PostRepository postRepository;
    private final LabelRepository labelRepository;

    @Override
    public Writer save(Writer writer) {
        String insertWriterSql = "INSERT INTO PROSELYTE_JDBC_DB.writer (firstName, lastName, status) VALUES (?, ?, ?)";

        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);
            Savepoint savepoint = connection.setSavepoint("SavepointWriter");

            try (PreparedStatement writerStatement = connection.prepareStatement(insertWriterSql, Statement.RETURN_GENERATED_KEYS)) {
                writerStatement.setString(1, writer.getFirstName());
                writerStatement.setString(2, writer.getLastName());
                writerStatement.setString(3, String.valueOf(Status.ACTIVE));

                int affectedRows = writerStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating writer failed, no rows affected.");
                }

                try (ResultSet generatedKeys = writerStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        writer.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating writer failed, no ID obtained.");
                    }
                }

                saveWriterPosts(writer, connection);
                connection.commit();
            } catch (SQLException e) {
                System.out.println("\n>>> SQLException. New writer save failed! Executing rollback to savepoint...");
                connection.rollback(savepoint);
                System.out.println("\n>>> ERROR: ");
                e.printStackTrace(System.out);
            }

        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return writer;
    }

    private void saveWriterPosts(Writer writer, Connection connection) throws SQLException {
        String insertWriterPostSql = "INSERT INTO PROSELYTE_JDBC_DB.writer_post (writer_id, post_id) VALUES (?, ?)";

        try (PreparedStatement postInsertStatement = connection.prepareStatement(insertWriterPostSql)) {
            if (writer.getPosts() != null) {
                for (Post post : writer.getPosts()) {
                    if (post != null) {
                        postInsertStatement.setLong(1, writer.getId());
                        if (post.getId() != null) {
                            postInsertStatement.setLong(2, post.getId());
                        } else {
                            Post newPost = postRepository.save(post);
                            if (newPost == null || newPost.getId() == null) {
                                throw new SQLException("Creating new writer post failed, no ID obtained.");
                            }
                            postInsertStatement.setLong(2, newPost.getId());
                        }
                    }
                    postInsertStatement.executeUpdate();
                }
            }
        }
    }

    @Override
    public Optional<Writer> findById(Long id) {
        String sql = "SELECT w.*, p.id AS post_id, p.content, p.created, p.updated, p.post_status, p.status " +
                "FROM PROSELYTE_JDBC_DB.writer w " +
                "LEFT JOIN PROSELYTE_JDBC_DB.writer_post wp ON w.id = wp.writer_id " +
                "LEFT JOIN PROSELYTE_JDBC_DB.post p ON wp.post_id = p.id " +
                "WHERE w.status != ? AND (p.status != ? OR p.status IS NULL)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, String.valueOf(Status.DELETED));
            statement.setLong(2, id);

            Map<Long, Writer> writerMap = getIdToWriterWithPostsMap(statement);

            return Optional.ofNullable(writerMap.get(id));
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
            return Optional.empty();
        }
    }

    @Override
    public List<Writer> findAll() {
        String sql = "SELECT w.*, p.id AS post_id, p.content, p.created, p.updated, p.post_status, p.status " +
                "FROM PROSELYTE_JDBC_DB.writer w " +
                "LEFT JOIN PROSELYTE_JDBC_DB.writer_post wp ON w.id = wp.writer_id " +
                "LEFT JOIN PROSELYTE_JDBC_DB.post p ON wp.post_id = p.id " +
                "WHERE w.status != ? AND p.status != ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, String.valueOf(Status.DELETED));
            statement.setString(2, String.valueOf(Status.DELETED));

            Map<Long, Writer> writerMap = getIdToWriterWithPostsMap(statement);

            return new ArrayList<>(writerMap.values());
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
            return Collections.emptyList();
        }
    }

    private Map<Long, Writer> getIdToWriterWithPostsMap(PreparedStatement statement) throws SQLException {
        Map<Long, Writer> writerMap = new HashMap<>();

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                long writerId = resultSet.getLong("id");
                Writer writer = writerMap.get(writerId);

                if (writer == null) {
                    writer = Writer.builder()
                            .id(writerId)
                            .firstName(resultSet.getString("firstName"))
                            .lastName(resultSet.getString("lastName"))
                            .status(Status.valueOf(resultSet.getString("w.status")))
                            .build();
                    writerMap.put(writerId, writer);
                }

                if (resultSet.getLong("post_id") != 0
                        && resultSet.getString("post_status") != null
                        && !resultSet.getString("post_status").equals(String.valueOf(Status.DELETED))) {

                    long postId = resultSet.getLong("post_id");
                    if (writer.getPosts() == null || writer.getPosts().stream().noneMatch(post -> post.getId() == postId)) {
                        Post post = Post.builder()
                                .id(resultSet.getLong("post_id"))
                                .content(resultSet.getString("content"))
                                .postStatus(PostStatus.valueOf(resultSet.getString("post_status")))
                                .created(resultSet.getTimestamp("created").toLocalDateTime())
                                .updated(resultSet.getTimestamp("updated") != null ? resultSet.getTimestamp("updated").toLocalDateTime() : null)
                                .build();

                        if (writer.getPosts() == null) {
                            writer.setPosts(new ArrayList<>());
                        }
                        writer.getPosts().add(post);
                    }
                }
            }
        }
        return writerMap;
    }

    @Override
    public Optional<Writer> update(Writer writer) {
        String sql = "UPDATE PROSELYTE_JDBC_DB.writer SET firstName = ?, lastName = ? WHERE id = ?";
        int affectedRows = 0;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, writer.getFirstName());
            statement.setString(2, writer.getLastName());
            statement.setLong(3, writer.getId());

            affectedRows = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return affectedRows > 0 ? Optional.of(writer) : Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "UPDATE PROSELYTE_JDBC_DB.writer SET status = ? WHERE id = ?";
        int affectedRows = 0;
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, String.valueOf(DELETED));
            statement.setLong(2, id);
            affectedRows = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return affectedRows > 0;
    }

    @Override
    public Class<Writer> getEntityClass() {
        return Writer.class;
    }
}
