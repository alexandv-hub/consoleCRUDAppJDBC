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

import static com.consoleCRUDApp.config.DBConnection.getPreparedStatement;
import static com.consoleCRUDApp.model.Status.DELETED;
import static com.consoleCRUDApp.repository.jdbc.SQLQueries.*;

@AllArgsConstructor
@Getter
public class JdbcPostRepositoryImpl implements PostRepository {

    private final LabelRepository labelRepository;

    @Override
    public Optional<Post> save(Post post) {
        int affectedRows;
        try {
            savePostLabels(post);

            // Insert post
            long postId;
            try (PreparedStatement postStatement = getPreparedStatement(SQL_INSERT_POST, Statement.RETURN_GENERATED_KEYS)) {
                postStatement.setString(1, post.getContent());
                postStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                postStatement.setString(3, String.valueOf(post.getPostStatus()));
                postStatement.setString(4, String.valueOf(post.getStatus()));
                affectedRows = postStatement.executeUpdate();

                try (ResultSet generatedKeys = postStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        postId = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Creating post failed, no ID obtained.");
                    }
                }
            }
            post.setId(postId);

            savePostLabelLinks(post);
        } catch (Exception e) {
            System.out.println("\n>>> ERROR: New post save failed!");
            e.printStackTrace(System.out);
            return Optional.empty();
        }
        return affectedRows > 0 ? Optional.of(post) : Optional.empty();
    }

    private void savePostLabels(Post post) {
        post.getLabels().forEach(label -> {
            Optional<Label> labelOptional = labelRepository.save(label);
            long labelId = labelOptional.map(Label::getId)
                    .orElseGet(() -> labelRepository.getIdByName(label.getName()));
            label.setId(labelId);
        });
    }

    private void savePostLabelLinks(Post post) throws SQLException {
        // Удаление старых связей
        try (PreparedStatement labelDeleteStatement = getPreparedStatement(SQL_UPDATE_POST_LABELS_STATUS)) {
            labelDeleteStatement.setString(1, String.valueOf(Status.DELETED));
            labelDeleteStatement.setLong(2, post.getId());
            labelDeleteStatement.executeUpdate();
        }

        // Вставка новых связей
        for (Label label : post.getLabels()) {
            if (label != null) {
                try (PreparedStatement linkStatement = getPreparedStatement(SQL_INSERT_POST_LABEL)) {
                    linkStatement.setLong(1, post.getId());
                    linkStatement.setLong(2, label.getId());
                    linkStatement.setString(3, String.valueOf(Status.ACTIVE));
                    linkStatement.executeUpdate();
                }
            }
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        try (PreparedStatement statement = getPreparedStatement(SQL_SELECT_POST_BY_ID)) {
            statement.setString(1, String.valueOf(Status.DELETED));
            statement.setString(2, String.valueOf(Status.DELETED));
            statement.setLong(3, id);
            statement.setString(4, String.valueOf(Status.DELETED));

            Map<Long, Post> postMap = getIdToPostWithLabelsMap(statement);

            if (!postMap.isEmpty()) {
                return Optional.of(postMap.get(id));
            }
        } catch (Exception e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return Optional.empty();
    }

    @Override
    public List<Post> findAll() {
        try (PreparedStatement statement = getPreparedStatement(SQL_SELECT_ALL_POSTS)) {
            statement.setString(1, String.valueOf(Status.DELETED));
            statement.setString(2, String.valueOf(Status.DELETED));
            statement.setString(3, String.valueOf(Status.DELETED));

            Map<Long, Post> postMap = getIdToPostWithLabelsMap(statement);

            return new ArrayList<>(postMap.values());
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            return Collections.emptyList();
        }
    }

    private Map<Long, Post> getIdToPostWithLabelsMap(PreparedStatement statement) {
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
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return postMap;
    }

    void addPostLabel(ResultSet resultSet, Post post) {
        try {
            long labelId = resultSet.getLong("label_id");
            if (labelId != 0) {
                if (post.getLabels() == null) {
                    post.setLabels(new ArrayList<>());
                }
                if (post.getLabels().stream()
                        .noneMatch(label -> label.getId() == labelId)) {
                    Label label = Label.builder()
                            .id(resultSet.getLong("label_id"))
                            .name(resultSet.getString("label_name"))
                            .status(Status.valueOf(resultSet.getString("status")))
                            .build();
                    post.getLabels().add(label);
                }
            }
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
    }

    @Override
    public Optional<Post> update(Post post) {
        int affectedRows = 0;
        try {
            savePostLabels(post);

            savePostLabelLinks(post);

            try (PreparedStatement statement = getPreparedStatement(SQL_UPDATE_POST_BY_ID)) {
                statement.setString(1, post.getContent());
                statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                statement.setString(3, String.valueOf(post.getPostStatus()));
                statement.setLong(4, post.getId());
                affectedRows = statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }

        return affectedRows > 0 ? Optional.of(post) : Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        int affectedRows = 0;
        try (PreparedStatement statement = getPreparedStatement(SQL_UPDATE_POST_STATUS_BY_ID)) {
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

}
