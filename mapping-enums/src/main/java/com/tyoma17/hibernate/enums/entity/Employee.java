package com.tyoma17.hibernate.enums.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Employee {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @Column(name = "EMPLOYEE_ID", unique = true)
    private String employeeId;

    @Enumerated(STRING) // <-- if ORDINAL, then numbers will be saved to DB
    @Column(name = "EMPLOYEE_STATUS")
    private EmployeeStatus employeeStatus;

    public Employee(String name, String employeeId, EmployeeStatus employeeStatus) {
        this.name = name;
        this.employeeId = employeeId;
        this.employeeStatus = employeeStatus;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id = " + id +
                ", name = '" + name + "'" +
                ", employeeId = '" + employeeId + "'" +
                ", employeeStatus = " + employeeStatus +
                '}';
    }
}