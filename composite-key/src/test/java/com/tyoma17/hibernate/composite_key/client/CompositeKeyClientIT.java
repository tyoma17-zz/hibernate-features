package com.tyoma17.hibernate.composite_key.client;

import com.tyoma17.hibernate.composite_key.entity.Child;
import com.tyoma17.hibernate.composite_key.entity.Parent;
import com.tyoma17.hibernate.composite_key.entity.ParentPrimaryKey;
import com.tyoma17.hibernate.composite_key.util.HibernateUtil;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CompositeKeyClientIT {

    @Test
    void testCompositeKey() {

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

        assertEquals("Charlotte", parent.getParentPrimaryKey().getFirstname());
        assertEquals("Crawford", parent.getParentPrimaryKey().getLastname());

        Set<Child> children = parent.getChildren();
        List<Long> ids = children.stream().map(Child::getId).collect(toList());
        List<String> names = children.stream().map(Child::getName).collect(toList());

        assertThat(ids, Matchers.containsInAnyOrder(1L, 2L));
        assertThat(names, Matchers.containsInAnyOrder("Ruby", "Groovy"));

        for (Child child : children) {
            assertEquals(parent, child.getParent());
        }

        session.close();
    }
}