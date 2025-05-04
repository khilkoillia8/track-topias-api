package com.tracktopiasapi.one.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "habit_instances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String weekday;

    @Column(nullable = false)
    private boolean completed;
    
    @Column(nullable = false)
    private boolean active;
}
