package com.consoleCRUDApp.repository.hibernate;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.repository.LabelRepository;

import java.util.List;
import java.util.Optional;

import static com.consoleCRUDApp.repository.hibernate.HibernateUtil.getSessionFactory;
import static com.consoleCRUDApp.repository.hibernate.SQLQueries.HQL_FROM_LABEL;
import static com.consoleCRUDApp.repository.hibernate.SQLQueries.HQL_FROM_LABEL_WHERE_NAME;

public class HibLabelRepositoryImpl implements LabelRepository {

    @Override
    public Optional<Label> save(Label label) {
        getSessionFactory().inTransaction(session ->
                session.merge(label));
        return Optional.of(label);
    }

    @Override
    public List<Label> findAll() {
        return getSessionFactory().fromTransaction(session ->
                session.createSelectionQuery(HQL_FROM_LABEL, Label.class)
                        .getResultList());
    }

    @Override
    public Optional<Label> findByName(String labelName) {
        return getSessionFactory().fromTransaction(session ->
                session.createQuery(HQL_FROM_LABEL_WHERE_NAME, Label.class)
                        .setParameter("name", labelName)
                        .uniqueResultOptional());
    }

    @Override
    public Optional<Label> findById(Long id) {
        return Optional.ofNullable(getSessionFactory().fromTransaction((session ->
                session.find(Label.class, id))));
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
        return getSessionFactory().fromTransaction(session -> {
            Label label = session.find(Label.class, id);
            if (label != null) {
                session.remove(label);
                session.flush();
                return true;
            }
            return false;
        });
    }
}
