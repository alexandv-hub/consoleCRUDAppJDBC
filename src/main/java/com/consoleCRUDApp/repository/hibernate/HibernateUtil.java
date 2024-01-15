package com.consoleCRUDApp.repository.hibernate;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.Writer;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import static com.consoleCRUDApp.view.messages.ErrorMessages.Database.ERROR_WHEN_SETTING_UP_HIBERNATE_SESSION_FACTORY;

@Getter
public class HibernateUtil {

    private HibernateUtil() {
    }

    private static SessionFactory sessionFactory;

    static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            setUp();
        }
        return sessionFactory;
    }

    private static void setUp() {
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .build();
        try {
            sessionFactory =
                    new MetadataSources(registry)
                            .addAnnotatedClass(Label.class)
                            .addAnnotatedClass(Post.class)
                            .addAnnotatedClass(Writer.class)
                            .buildMetadata()
                            .buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we
            // had trouble building the SessionFactory so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
            System.out.println(ERROR_WHEN_SETTING_UP_HIBERNATE_SESSION_FACTORY);
            throw e;
        }
    }
}
