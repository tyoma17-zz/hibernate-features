package com.tyoma17.hibernate.enums.client;

import com.tyoma17.hibernate.enums.entity.Employee;
import com.tyoma17.hibernate.enums.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static com.tyoma17.hibernate.enums.entity.EmployeeStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
class EnumsClientIT {

    @Test
    void testEnums() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Employee employee1 = new Employee("Josh Stockham", "2014JA11001", FULL_TIME);
        Employee employee2 = new Employee("Ammie Corrio", "2014AI21543", PART_TIME);
        Employee employee3 = new Employee("Ernie Stenseth", "2014ET25100", CONTRACT);

        session.persist(employee1);
        session.persist(employee2);
        session.persist(employee3);

        session.getTransaction().commit();
        log.info("Three employees persisted to DB");
        session.close();

        session = HibernateUtil.getSessionFactory().openSession();

        employee1 = session.get(Employee.class, 1L);
        assertEquals(1L, employee1.getId());
        assertEquals("Josh Stockham", employee1.getName());
        assertEquals("2014JA11001", employee1.getEmployeeId());
        assertEquals(FULL_TIME, employee1.getEmployeeStatus());

        employee2 = session.get(Employee.class, 2L);
        assertEquals(2L, employee2.getId());
        assertEquals("Ammie Corrio", employee2.getName());
        assertEquals("2014AI21543", employee2.getEmployeeId());
        assertEquals(PART_TIME, employee2.getEmployeeStatus());

        employee3 = session.get(Employee.class, 3L);
        assertEquals(3L, employee3.getId());
        assertEquals("Ernie Stenseth", employee3.getName());
        assertEquals("2014ET25100", employee3.getEmployeeId());
        assertEquals(CONTRACT, employee3.getEmployeeStatus());

        session.close();
    }
}