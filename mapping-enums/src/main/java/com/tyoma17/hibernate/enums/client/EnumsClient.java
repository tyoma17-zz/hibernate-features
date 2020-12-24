package com.tyoma17.hibernate.enums.client;

import com.tyoma17.hibernate.enums.entity.Employee;
import com.tyoma17.hibernate.enums.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;

import static com.tyoma17.hibernate.enums.entity.EmployeeStatus.*;

@Log4j2
public class EnumsClient {

    public static void main(String[] args) {

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

        for (long i = 1; i <= 3; i++) {
            Employee employee = session.get(Employee.class, i);
            log.info("Persisted employee: {}", employee);
        }

        session.close();
    }
}