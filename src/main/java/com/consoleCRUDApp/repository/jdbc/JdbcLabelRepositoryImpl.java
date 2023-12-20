package com.consoleCRUDApp.repository.jdbc;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.repository.LabelRepository;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.consoleCRUDApp.config.DBConnection.getConnection;
import static com.consoleCRUDApp.model.Status.DELETED;

@Getter
public class JdbcLabelRepositoryImpl implements LabelRepository {

    @Override
    public Label save(Label label) {
        if (!isLabelExistInRepository(label)) {
            String sql = "INSERT INTO PROSELYTE_JDBC_DB.label (name, status) VALUES (?, ?)";
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, label.getName());
                statement.setString(2, String.valueOf(label.getStatus()));

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating label failed, no rows affected.");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        label.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating label failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("\n>>> ERROR: ");
                e.printStackTrace(System.out);
            }
        }
        return label;
    }

    @Override
    public List<Label> findAll() {
        List<Label> labels = new ArrayList<>();
        String sql = "SELECT * FROM PROSELYTE_JDBC_DB.label WHERE status != ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, String.valueOf(Status.DELETED));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Label label = Label.builder()
                            .id(resultSet.getLong("id"))
                            .name(resultSet.getString("name"))
                            .build();
                    labels.add(label);
                }
            }
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return labels;
    }

    @Override
    public boolean isLabelExistInRepository(Label label) {
        String sql = "SELECT id FROM PROSELYTE_JDBC_DB.label WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, label.getName());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
            return false;
        }
    }

    @Override
    public Long getIdByName(String labelName) {
        String sql = "SELECT id FROM PROSELYTE_JDBC_DB.label WHERE name = ? AND status != ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, labelName);
            statement.setString(2, String.valueOf(Status.DELETED));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return null;
    }

    @Override
    public Optional<Label> findById(Long id) {
        String sql = "SELECT * FROM PROSELYTE_JDBC_DB.label WHERE id = ? AND status = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.setString(2, String.valueOf(Status.ACTIVE));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Label label = Label.builder()
                            .id(resultSet.getLong("id"))
                            .name(resultSet.getString("name"))
                            .build();
                    return Optional.of(label);
                }
            }
        }
        catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Label> update(Label entity) {
        String sql = "UPDATE PROSELYTE_JDBC_DB.label SET name = ? WHERE id = ?";
        int affectedRows = 0;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getName());
            statement.setLong(2, entity.getId());
            affectedRows = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }

        return affectedRows > 0 ? Optional.of(entity) : Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "UPDATE PROSELYTE_JDBC_DB.label SET status = ? WHERE id = ?";
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
    public Class<Label> getEntityClass() {
        return Label.class;
    }

}
