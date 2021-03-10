package ru.job4j.tracker;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


import java.util.List;

public class HbmTracker implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();


    @Override
    public Item add(Item item) {
        try {
            Session session = sf.openSession();
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public boolean replace(String id, Item item) {
        int Id = Integer.parseInt(id);
        Session session = sf.openSession();
        session.beginTransaction();
        Item items = new Item();
        items.setId(Id);
        session.delete(items);
        item.setId(Id);
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String id) {
        int ID = Integer.parseInt(id);
        Session session = sf.openSession();
        session.beginTransaction();
        Item item = new Item();
        item.setId(ID);
        session.delete(item);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public List<Item> findAll() {
        List<Item> item = (List<Item>) sf.openSession().createQuery("FROM ru.job4j.tracker.Item").list();
        return item;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> item = (List<Item>) sf.openSession().createQuery("from ru.job4j.tracker.Item where name = '" + key + "'").list();
        return item;
    }

    @Override
    public Item findById(String id) {
        int Id = Integer.parseInt(id);
        return sf.openSession().get(Item.class, Id);
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
