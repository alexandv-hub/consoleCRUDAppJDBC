package com.consoleCRUDApp.repository.jdbc;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.repository.LabelRepository;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.consoleCRUDApp.config.DBConnection.getPreparedStatement;
import static com.consoleCRUDApp.model.Status.DELETED;
import static com.consoleCRUDApp.repository.jdbc.SQLQueries.*;

@Getter
public class JdbcLabelRepositoryImpl implements LabelRepository {

    @Override
    public Optional<Label> save(Label label) {
        int affectedRows = 0;
        if (!isLabelExistInRepository(label)) {
            try (PreparedStatement statement = getPreparedStatement(SQL_INSERT_LABEL, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, label.getName());
                statement.setString(2, String.valueOf(label.getStatus()));
                affectedRows = statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        label.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating label failed, no ID obtained.");
                    }
                }
            } catch (Exception e) {
                System.out.println("\n>>> ERROR: New label save failed!");
                e.printStackTrace(System.out);
                return Optional.empty();
            }
        }
        return affectedRows > 0 ? Optional.of(label) : Optional.empty();
    }

    @Override
    public List<Label> findAll() {
        try (PreparedStatement statement = getPreparedStatement(SQL_SELECT_ALL_LABELS)) {
            List<Label> labels = new ArrayList<>();
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
            return labels;
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isLabelExistInRepository(Label label) {
        try (PreparedStatement statement = getPreparedStatement(SQL_SELECT_ID_FROM_LABEL_BY_NAME)) {

            statement.setString(1, label.getName());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.out.println("\n>>> ERROR: ");
            e.printStackTrace(System.out);
        }
        return false;
    }

    @Override
    public Long getIdByName(String labelName) {
        try (PreparedStatement statement = getPreparedStatement(SQL_SELECT_ID_FROM_LABEL_BY_NAME_AND_STATUS)) {
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
        try (PreparedStatement statement = getPreparedStatement(SQL_SELECT_ALL_FROM_LABEL_BY_ID_AND_STATUS)) {
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
        int affectedRows = 0;
        try (PreparedStatement statement = getPreparedStatement(SQL_UPDATE_LABEL_NAME_BY_ID)) {
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
        int affectedRows = 0;
        try (PreparedStatement statement = getPreparedStatement(SQL_UPDATE_LABEL_STATUS_BY_ID)) {
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
