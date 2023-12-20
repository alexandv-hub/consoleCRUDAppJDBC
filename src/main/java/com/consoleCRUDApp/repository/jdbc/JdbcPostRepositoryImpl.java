package com.consoleCRUDApp.repository.jdbc;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.PostStatus;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.repository.LabelRepository;
import com.consoleCRUDApp.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import static com.consoleCRUDApp.config.DBConnection.getConnection;
import static com.consoleCRUDApp.model.Status.DELETED;

@AllArgsConstructor
@Getter
public class JdbcPostRepositoryImpl implements PostRepository {

    private final LabelRepository labelRepository;

    @Override
    public Post save(Post post) {
        String insertPostSql = "INSERT INTO PROSELYTE_JDBC_DB.post (content, created, post_status, status) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);
            Savepoint savepoint = connection.setSavepoint("SavepointPost");

            try (PreparedStatement postStatement = connection.prepareStatement(insertPostSql, Statement.RETURN_GENERATED_KEYS)) {
                postStatement.setString(1, post.getContent());
                postStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                postStatement.setString(3, String.valueOf(post.getPostStatus()));
                postStatement.setString(4, String.valueOf(post.getStatus()));

                int affectedRows = postStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating post failed, no rows affected.");
                }

                try (ResultSet generatedKeys = postStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        post.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating post failed, no ID obtained.");
                    }
                }

                savePostLabels(post, connection);
                connection.commit();
            } catch (SQLException e) {
                System.out.println("\n>>> SQLException. New post save failed! Executing rollback to savepoint...");
                connection.rollback(savepoint);
                System.out.println("\n>>> ERROR: ");
                e.printStackTrace(System.out);
            }

        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return post;
    }

    private void savePostLabels(Post post, Connection connection) throws SQLException {
        String deleteOldPostLabelsSql = "DELETE FROM PROSELYTE_JDBC_DB.post_label WHERE post_id = ?";
        String insertPostLabelSql = "INSERT INTO PROSELYTE_JDBC_DB.post_label (post_id, label_id) VALUES (?, ?)";

        // Удаление старых связей
        try (PreparedStatement labelDeleteStatement = connection.prepareStatement(deleteOldPostLabelsSql)) {
            labelDeleteStatement.setLong(1, post.getId());
            labelDeleteStatement.executeUpdate();
        }

        // Вставка новых связей
        try (PreparedStatement labelInsertStatement = connection.prepareStatement(insertPostLabelSql)) {
            for (Label label : post.getLabels()) {
                if (label != null) {
                    labelInsertStatement.setLong(1, post.getId());
                    if (label.getId() != null) {
                        labelInsertStatement.setLong(2, label.getId());
                    } else {
                        Long existingLabelId = labelRepository.getIdByName(label.getName());
                        if (existingLabelId != null) {
                            labelInsertStatement.setLong(2, existingLabelId);
                        } else {
                            Label newLabel = labelRepository.save(label);
                            if (newLabel == null || newLabel.getId() == null) {
                                throw new SQLException("Creating new label failed, no ID obtained.");
                            }
                            labelInsertStatement.setLong(2, newLabel.getId());
                        }
                    }
                    labelInsertStatement.executeUpdate();
                }
            }
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "SELECT p.*, l.id AS label_id, l.name AS label_name FROM PROSELYTE_JDBC_DB.post p\n" +
                "    LEFT JOIN PROSELYTE_JDBC_DB.post_label pl ON p.id = pl.post_id\n" +
                "    LEFT JOIN PROSELYTE_JDBC_DB.label l ON pl.label_id = l.id\n" +
                "    WHERE p.id = ? \n" +
                "    AND p.status != ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.setString(2, String.valueOf(Status.DELETED));

            Map<Long, Post> postMap = getIdToPostWithLabelsMap(statement);

            if (!postMap.isEmpty()) {
                return Optional.of(postMap.get(id));
            }
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return Optional.empty();
    }

    @Override
    public List<Post> findAll() {
        String sql = "SELECT p.*, l.id AS label_id, l.name AS label_name FROM PROSELYTE_JDBC_DB.post p\n" +
                "    LEFT JOIN PROSELYTE_JDBC_DB.post_label pl ON p.id = pl.post_id\n" +
                "    LEFT JOIN PROSELYTE_JDBC_DB.label l ON pl.label_id = l.id\n" +
                "    WHERE p.status != ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, String.valueOf(Status.DELETED));

            Map<Long, Post> postMap = getIdToPostWithLabelsMap(statement);
            return new ArrayList<>(postMap.values());
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            return Collections.emptyList();
        }
    }

    private Map<Long, Post> getIdToPostWithLabelsMap(PreparedStatement statement) throws SQLException {
        Map<Long, Post> postMap = new HashMap<>();

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                long postId = resultSet.getLong("id");
                Post post = postMap.get(postId);
                if (post == null) {
                    post = Post.builder()
                            .id(postId)
                            .content(resultSet.getString("content"))
                            .created(resultSet.getTimestamp("created").toLocalDateTime())
                            .postStatus(PostStatus.valueOf(resultSet.getString("post_status")))
                            .status(Status.valueOf(resultSet.getString("status")))
                            .build();

                    Timestamp updatedTimestamp = resultSet.getTimestamp("updated");
                    LocalDateTime updated = updatedTimestamp != null ? updatedTimestamp.toLocalDateTime() : null;
                    post.setUpdated(updated);
                }
                addPostLabel(resultSet, post);
                postMap.put(postId, post);
            }
        }
        return postMap;
    }

    void addPostLabel(ResultSet resultSet, Post post) throws SQLException {
        long labelId = resultSet.getLong("label_id");
        if (labelId != 0) {
            if (post.getLabels() == null) {
                post.setLabels(new ArrayList<>());
            }
            if (post.getLabels().stream().noneMatch(label -> label.getId() == labelId)) {
                Label label = Label.builder()
                        .id(resultSet.getLong("label_id"))
                        .name(resultSet.getString("label_name"))
                        .build();
                post.getLabels().add(label);
            }
        }
    }

    @Override
    public Optional<Post> update(Post post) {
        String sql = "UPDATE PROSELYTE_JDBC_DB.post SET content = ?, updated = ?, post_status = ? WHERE id = ?";
        int affectedRows = 0;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, post.getContent());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(3, String.valueOf(post.getPostStatus()));
            statement.setLong(4, post.getId());
            affectedRows = statement.executeUpdate();

            savePostLabels(post, connection);

        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }

        return affectedRows > 0 ? Optional.of(post) : Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "UPDATE PROSELYTE_JDBC_DB.post SET status = ?, post_status = ? WHERE id = ?";
        int affectedRows = 0;
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, String.valueOf(DELETED));
            statement.setString(2, String.valueOf(DELETED));
            statement.setLong(3, id);
            affectedRows = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return affectedRows > 0;
    }

    @Override
    public Class<Post> getEntityClass() {
        return Post.class;
    }

}
