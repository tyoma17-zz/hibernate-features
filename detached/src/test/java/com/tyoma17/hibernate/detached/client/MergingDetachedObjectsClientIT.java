package com.tyoma17.hibernate.detached.client;

import com.tyoma17.hibernate.detached.entity.Guide;
import com.tyoma17.hibernate.detached.entity.Student;
import com.tyoma17.hibernate.detached.util.DbUtils;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MergingDetachedObjectsClientIT {

    @Test
    void test() {

        DbUtils.bootstrapDb();

        EntityManager em1 = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        em1.getTransaction().begin();

        Guide guide2 = em1.find(Guide.class, 2L);
        List<Student> students = guide2.getStudents();
        int numOfStudents = students.size();

        Student student = null;

        for (Student nextStudent : students) {

            if (nextStudent.getId() == 2L) {
                student = nextStudent;
            }
        }

        em1.getTransaction().commit();
        em1.close();

        assertEquals(2000, guide2.getSalary());
        assertEquals("Amy Gill", student.getName());

        guide2.setSalary(2500);
        student.setName("Amy Jade Gill");

        EntityManager em2 = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        em2.getTransaction().begin();

        Guide mergedGuide = em2.merge(guide2);
        assertEquals(2500, mergedGuide.getSalary());
        assertEquals("Amy Jade Gill", mergedGuide.getStudents().get(1).getName());

        em2.getTransaction().commit();
        em2.close();
    }
}