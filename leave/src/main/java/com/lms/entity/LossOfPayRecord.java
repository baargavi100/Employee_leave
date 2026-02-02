package com.lms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "loss_of_pay_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LossOfPayRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User employee;

    private int year;
    private int month;
    private double lopPercentage;
    private int monthlyViolationCount;
    private String reason;
}
