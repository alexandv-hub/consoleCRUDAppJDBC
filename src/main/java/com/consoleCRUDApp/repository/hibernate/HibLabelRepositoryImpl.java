package com.consoleCRUDApp.repository.hibernate;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.repository.LabelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.consoleCRUDApp.repository.hibernate.HibernateUtil.getSessionFactory;
import static com.consoleCRUDApp.repository.hibernate.SQLQueries.*;

public class HibLabelRepositoryImpl implements LabelRepository {

    @Override
    public Optional<Label> save(Label label) {
        getSessionFactory().inTransaction(session ->
                session.persist(label)
        );
        return Optional.of(label);
    }

    @Override
    public List<Label> findAll() {
        List<Label> labels = new ArrayList<>();
        getSessionFactory().inTransaction(session ->
                labels.addAll(session.createSelectionQuery(HQL_FROM_LABEL_WHERE_STATUS, Label.class)
                        .setParameter("status", Status.ACTIVE)
                        .getResultList()));
        return labels;
    }

    @Override
    public boolean isLabelExistInRepository(Label label) {
        AtomicBoolean isLabelExistInRepository = new AtomicBoolean(false);

        getSessionFactory().inTransaction(session ->
                session.createQuery(HQL_FROM_LABEL_WHERE_NAME_AND_STATUS, Label.class)
                        .setParameter("name", label.getName())
                        .setParameter("status", Status.ACTIVE)
                        .uniqueResultOptional()
                        .ifPresent(foundLabel -> isLabelExistInRepository.set(true)));

        return isLabelExistInRepository.get();
    }

    @Override
    public Optional<Label> getLabelByName(String labelName) {
        AtomicReference<Label> labelAtomicReference = new AtomicReference<>();

        getSessionFactory().inTransaction(session ->
                session.createQuery(HQL_FROM_LABEL_WHERE_NAME_AND_STATUS, Label.class)
                        .setParameter("name", labelName)
                        .setParameter("status", Status.ACTIVE)
                        .uniqueResultOptional()
                        .ifPresent(labelAtomicReference::set));

        return Optional.ofNullable(labelAtomicReference.get());
    }

    @Override
    public Optional<Label> findById(Long id) {
        AtomicReference<Label> labelAtomicReference = new AtomicReference<>();

        getSessionFactory().inTransaction(session ->
                session.createQuery(HQL_FROM_LABEL_WHERE_ID_AND_STATUS, Label.class)
                        .setParameter("id", id)
                        .setParameter("status", Status.ACTIVE)
                        .uniqueResultOptional()
                        .ifPresent(labelAtomicReference::set));

        return Optional.ofNullable(labelAtomicReference.get());
    }

    @Override
    public Optional<Label> update(Label label) {
        getSessionFactory().inTransaction(session -> {
            Label existingLabel = session.find(Label.class, label.getId());
            existingLabel.setName(label.getName());
        });
        return Optional.of(label);
    }

    @Override
    public boolean deleteById(Long id) {
        AtomicBoolean isDeleted = new AtomicBoolean(false);

        getSessionFactory().inTransaction(session -> {
            int affectedRows = session.createMutationQuery(HQL_UPDATE_LABEL_SET_STATUS_WHERE_ID)
                    .setParameter("status", Status.DELETED)
                    .setParameter("id", id)
                    .executeUpdate();
            isDeleted.set(affectedRows > 0);
        });

        return isDeleted.get();
    }
}
