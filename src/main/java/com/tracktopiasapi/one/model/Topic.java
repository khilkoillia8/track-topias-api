package com.tracktopiasapi.one.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"missions", "habits"})
@EqualsAndHashCode(exclude = {"missions", "habits"})
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "topics")
    private Set<Mission> missions = new HashSet<>();

    @ManyToMany(mappedBy = "topics")
    private Set<Habit> habits = new HashSet<>();
}
