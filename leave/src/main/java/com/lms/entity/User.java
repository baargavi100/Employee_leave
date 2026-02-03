package com.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false)
    private String role;  // EMPLOYEE, MANAGER, ADMIN

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "total_working_days")
    private Integer totalWorkingDays = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}