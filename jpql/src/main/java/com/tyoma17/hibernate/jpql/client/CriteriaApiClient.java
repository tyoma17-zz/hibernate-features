package com.tyoma17.hibernate.jpql.client;

import com.tyoma17.hibernate.jpql.entity.Guide;
import com.tyoma17.hibernate.jpql.entity.Guide_;
import com.tyoma17.hibernate.jpql.entity.Student;
import com.tyoma17.hibernate.jpql.entity.Student_;
import com.tyoma17.hibernate.jpql.util.DbUtils;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

@Log4j2
public class CriteriaApiClient {

    public static void main(String[] args) {

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

            for (Guide guide : guides) {
                log.info("Persisted guide: {}", guide);
            }

            /////////////////////////////////////////////////////////////////

            CriteriaQuery<String> criteria2 = cb.createQuery(String.class);
            Root<Student> root2 = criteria2.from(Student.class);
            Path<String> name = root2.get(Student_.NAME);
            criteria2.select(name);

            TypedQuery<String> query2 = entityManager.createQuery(criteria2);
            List<String> studentNames = query2.getResultList();

            log.info("Students names:");
            for (String n : studentNames) {
                log.info(n);
            }

            /////////////////////////////////////////////////////////////////

            CriteriaQuery<Guide> criteria3 = cb.createQuery(Guide.class);
            Root<Guide> root3 = criteria3.from(Guide.class);
            Path<String> name2 = root3.get(Guide_.NAME);
            criteria3.where(cb.equal(name2, "Ian Lamb"));
            criteria3.select(root3);

            TypedQuery<Guide> query3 = entityManager.createQuery(criteria3);

            log.info("Searching for a guide with the name 'Ian Lamb'...");
            log.info("Result: {}", query3.getSingleResult());

            /////////////////////////////////////////////////////////////////

            String guideName = "Ian Lamb"; // simulating dynamic query

            CriteriaQuery<Guide> criteria4 = cb.createQuery(Guide.class);
            Root<Guide> root4 = criteria4.from(Guide.class);
            criteria4.where(cb.equal(root4.get(Guide_.NAME), cb.parameter(String.class, "name")));
            criteria4.select(root4);

            TypedQuery<Guide> query4 = entityManager.createQuery(criteria4)
                    .setParameter("name", guideName);
            log.info("Result: {}", query3.getSingleResult());

            /////////////////////////////////////////////////////////////////

            log.info("Testing Join Fetch...");
            entityManager.clear();

            CriteriaQuery<Guide> criteria5 = cb.createQuery(Guide.class).distinct(true);
            Root<Guide> root5 = criteria5.from(Guide.class);
            root5.fetch(Guide_.STUDENTS);
            criteria5.select(root5);

            TypedQuery<Guide> query5 = entityManager.createQuery(criteria5);
            guides = query5.getResultList();
//            select
//              guide0_.id as id1_0_0_,
//                    students1_.id as id1_1_1_,
//              guide0_.name as name2_0_0_,
//                    guide0_.salary as salary3_0_0_,
//              guide0_.STAFF_ID as staff_id4_0_0_,
//                    students1_.ENROLLMENT_ID as enrollme2_1_1_,
//              students1_.GUIDE_ID as guide_id4_1_1_,
//                    students1_.name as name3_1_1_,
//              students1_.GUIDE_ID as guide_id4_1_0__,
//                    students1_.id as id1_1_0__
//            from
//              Guide guide0_
//            inner join
//              Student students1_
//            on guide0_.id=students1_.GUIDE_ID

        } catch (Exception e) {
            log.error(e.getMessage(), e);
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