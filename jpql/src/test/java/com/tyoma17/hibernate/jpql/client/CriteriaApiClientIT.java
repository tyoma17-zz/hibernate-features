package com.tyoma17.hibernate.jpql.client;

import com.tyoma17.hibernate.jpql.entity.Guide;
import com.tyoma17.hibernate.jpql.entity.Guide_;
import com.tyoma17.hibernate.jpql.entity.Student;
import com.tyoma17.hibernate.jpql.entity.Student_;
import com.tyoma17.hibernate.jpql.util.DbUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Log4j2
class CriteriaApiClientIT {

    @AfterEach
    void tearDown() {
        try {
            DbUtils.clearDatabase();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCriteriaApi() {
        DbUtils.bootstrapDb();
        EntityManager entityManager = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        List<Guide> guides = null;

        try {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Guide> criteria = cb.createQuery(Guide.class);
            Root<Guide> root = criteria.from(Guide.class);
            criteria.select(root);

            TypedQuery<Guide> query = entityManager.createQuery(criteria);
            guides = query.getResultList();

            Guide guide1 = guides.get(0);
            assertEquals(1L, guide1.getId());
            assertEquals("Mike Lawson", guide1.getName());
            assertEquals("2000MO10789", guide1.getStaffId());
            assertEquals(1000, guide1.getSalary());
            assertEquals(2, guide1.getStudents().size());

            Guide guide2 = guides.get(1);
            assertEquals(2L, guide2.getId());
            assertEquals("Ian Lamb", guide2.getName());
            assertEquals("2000IM10901", guide2.getStaffId());
            assertEquals(2000, guide2.getSalary());

            Student student1 = guide1.getStudents().get(0);
            Student student2 = guide1.getStudents().get(1);

            /////////////////////////////////////////////////////////////////

            CriteriaQuery<String> criteria2 = cb.createQuery(String.class);
            Root<Student> root2 = criteria2.from(Student.class);
            Path<String> name = root2.get(Student_.NAME);
            criteria2.select(name);

            TypedQuery<String> query2 = entityManager.createQuery(criteria2);
            List<String> studentNames = query2.getResultList();

            log.info("Students names:");
            assertEquals("John Smith", studentNames.get(0));
            assertEquals("Amy Gill", studentNames.get(1));

            /////////////////////////////////////////////////////////////////

            CriteriaQuery<Guide> criteria3 = cb.createQuery(Guide.class);
            Root<Guide> root3 = criteria3.from(Guide.class);
            Path<String> name2 = root3.get(Guide_.NAME);
            criteria3.where(cb.equal(name2, "Ian Lamb"));
            criteria3.select(root3);

            TypedQuery<Guide> query3 = entityManager.createQuery(criteria3);

            log.info("Searching for a guide with the name 'Ian Lamb'...");
            assertEquals(guide2, query3.getSingleResult());

            /////////////////////////////////////////////////////////////////

            String guideName = "Ian Lamb"; // simulating dynamic query

            CriteriaQuery<Guide> criteria4 = cb.createQuery(Guide.class);
            Root<Guide> root4 = criteria4.from(Guide.class);
            criteria4.where(cb.equal(root4.get(Guide_.NAME), cb.parameter(String.class, "name")));
            criteria4.select(root4);

            TypedQuery<Guide> query4 = entityManager.createQuery(criteria4)
                    .setParameter("name", guideName);
            assertEquals(guide2, query4.getSingleResult());

            /////////////////////////////////////////////////////////////////

            log.info("Testing Join Fetch...");
            entityManager.clear();

            CriteriaQuery<Guide> criteria5 = cb.createQuery(Guide.class).distinct(true);
            Root<Guide> root5 = criteria5.from(Guide.class);
            root5.fetch(Guide_.STUDENTS);
            criteria5.select(root5);

            TypedQuery<Guide> query5 = entityManager.createQuery(criteria5);
            guides = query5.getResultList();


        } catch (Exception e) {
            fail(e.getMessage(), e);
        } finally {

            if (entityManager != null) {
                entityManager.close();
            }
        }

        log.info("Listing all guides' students after entity manager is closed: ");
        guides.stream()
                .map(Guide::getStudents)
                .flatMap(List::stream)
                .forEach(log::info);
    }
}