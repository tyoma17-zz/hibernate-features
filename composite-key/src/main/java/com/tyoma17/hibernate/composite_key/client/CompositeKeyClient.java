package com.tyoma17.hibernate.composite_key.client;

import com.tyoma17.hibernate.composite_key.entity.Child;
import com.tyoma17.hibernate.composite_key.entity.Parent;
import com.tyoma17.hibernate.composite_key.entity.ParentPrimaryKey;
import com.tyoma17.hibernate.composite_key.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;

@Log4j2
public class CompositeKeyClient {

    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        ParentPrimaryKey parentPrimaryKey = new ParentPrimaryKey("Charlotte", "Crawford");
        Parent parent = new Parent(parentPrimaryKey);

        Child child1 = new Child("Ruby");
        Child child2 = new Child("Groovy");

        parent.addChild(child1);
        parent.addChild(child2);

        session.persist(parent);

        session.getTransaction().commit();
        session.close();

        session = HibernateUtil.getSessionFactory().openSession();
        parent = session.get(Parent.class, parentPrimaryKey);
        log.info("Persisted parent: {}", parent);
        session.close();
    }
}