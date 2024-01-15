package com.consoleCRUDApp.repository.hibernate;

import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.model.Writer;
import com.consoleCRUDApp.repository.WriterRepository;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Optional;

import static com.consoleCRUDApp.repository.hibernate.HibernateUtil.getSessionFactory;
import static com.consoleCRUDApp.repository.hibernate.SQLQueries.HQL_FROM_WRITER_LEFT_JOIN_FETCH_POSTS;


@AllArgsConstructor
public class HibWriterRepositoryImpl implements WriterRepository {

    @Override
    public Optional<Writer> save(Writer writer) {
        getSessionFactory().inTransaction(session ->
                session.persist(writer));
        return Optional.of(writer);
    }

    @Override
    public List<Writer> findAll() {
        return getSessionFactory().fromTransaction(session ->
                session.createSelectionQuery(HQL_FROM_WRITER_LEFT_JOIN_FETCH_POSTS, Writer.class)
                        .getResultList());
    }

    @Override
    public Optional<Writer> findById(Long id) {
        return Optional.ofNullable(getSessionFactory().fromTransaction(session -> {
            Writer writer = session.get(Writer.class, id);
            if (writer != null) {
                Hibernate.initialize(writer.getPosts());
                writer.getPosts().forEach(post -> Hibernate.initialize(post.getLabels()));
            }
            return writer;
        }));
    }

    @Override
    public Optional<Writer> update(Writer writer) {
        return Optional.ofNullable(getSessionFactory().fromTransaction(session ->
                session.merge(writer)));
    }

    @Override
    public boolean deleteById(Long id) {
        getSessionFactory().inTransaction(session -> {
            Writer writer = session.find(Writer.class, id);
            if (writer != null) {
                writer.setStatus(Status.DELETED);
                session.merge(writer);
            }
        });
        return true;
    }
}
