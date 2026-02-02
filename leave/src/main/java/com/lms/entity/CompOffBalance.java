package com.lms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comp_off_balance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompOffBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    private int year;
    private double balance;
}
