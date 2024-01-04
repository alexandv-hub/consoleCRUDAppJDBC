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

import static com.consoleCRUDApp.config.DBConnection.*;
import static com.consoleCRUDApp.model.Status.DELETED;
import static com.consoleCRUDApp.repository.jdbc.SQLQueries.*;

@AllArgsConstructor
@Getter
public class JdbcWriterRepositoryImpl implements WriterRepository {

    private final PostRepository postRepository;
    private final LabelRepository labelRepository;

    @Override
    public Optional<Writer> save(Writer writer) {
        Connection connection = null;
        Savepoint savepointWriter = null;

        int affectedRows;
        try {
            connection = getConnectionNoAutoCommit();
            savepointWriter = connection.setSavepoint("savepointWriter");

            saveWriterPosts(writer);

            long writerId;
            try (PreparedStatement writerStatement = getPreparedStatement(SQL_INSERT_WRITER, Statement.RETURN_GENERATED_KEYS)) {
                writerStatement.setString(1, writer.getFirstName());
                writerStatement.setString(2, writer.getLastName());
                writerStatement.setString(3, String.valueOf(Status.ACTIVE));
                affectedRows = writerStatement.executeUpdate();

                try (ResultSet generatedKeys = writerStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        writerId = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Creating writer failed, no ID obtained.");
                    }
                }
            }
            writer.setId(writerId);

            saveWriterPostLinks(writer);

            connection.commit();
        } catch (Exception e) {
            System.out.println("\n>>> ERROR: New writer save failed!");
            e.printStackTrace(System.out);
            if (connection != null) {
                try {
                    connection.rollback(savepointWriter);
                    System.out.println("\n>>> INFO: Rollback to savepoint...");
                } catch (SQLException ex) {
                    System.out.println("\n>>> ERROR: when roll back to savepoint!");
                    e.printStackTrace(System.out);
                }
            }
            return Optional.empty();
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("\n>>> INFO: closing connection...");
                } catch (SQLException e) {
                    System.out.println("\n>>> ERROR: when closing connection!");
                    e.printStackTrace(System.out);
                }
            }
        }
        return affectedRows > 0 ? Optional.of(writer) : Optional.empty();
    }

    private void saveWriterPosts(Writer writer) {
        if (writer.getPosts() != null) {
            for (Post post : writer.getPosts()) {
                if (post != null) {
                    Optional<Post> optionalPost = postRepository.save(post);
                    if (optionalPost.isPresent()) {
                        long postId = optionalPost.get().getId();
                        post.setId(postId);
                    }
                }
            }
        }
    }

    private void saveWriterPostLinks(Writer writer) throws SQLException {
        try (PreparedStatement postInsertStatement = getPreparedStatement(SQL_INSERT_WRITER_POSTS)) {
            if (writer.getPosts() != null) {
                for (Post post : writer.getPosts()) {
                    if (post != null) {
                        postInsertStatement.setLong(1, writer.getId());
                        postInsertStatement.setLong(2, post.getId());
                        postInsertStatement.setString(3, String.valueOf(Status.ACTIVE));
                        postInsertStatement.executeUpdate();
                    }
                }
            }
        }
    }

    @Override
    public Optional<Writer> findById(Long id) {
        try (PreparedStatement statement = getPreparedStatement(SQL_SELECT_WRITER_BY_ID)) {
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
        try (PreparedStatement statement = getPreparedStatement(SQL_SELECT_ALL_WRITERS)) {
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

    private Map<Long, Writer> getIdToWriterWithPostsMap(PreparedStatement statement) {
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
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return writerMap;
    }

    @Override
    public Optional<Writer> update(Writer writer) {
        int affectedRows = 0;
        try (PreparedStatement statement = getPreparedStatement(SQL_UPDATE_WRITER_BY_ID)) {
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
        int affectedRows = 0;
        try (PreparedStatement statement = getPreparedStatement(SQL_UPDATE_WRITER_STATUS_BY_ID)) {
            statement.setString(1, String.valueOf(DELETED));
            statement.setLong(2, id);
            affectedRows = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return affectedRows > 0;
    }
}
