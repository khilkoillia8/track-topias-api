package com.tracktopiasapi.one.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "habits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"topics"})
@EqualsAndHashCode(exclude = {"topics"})
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "habit_weekdays", joinColumns = @JoinColumn(name = "habit_id"))
    @Column(name = "weekday", nullable = false)
    private List<String> weekdays;

    @Column(nullable = false)
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToMany
    @JoinTable(
        name = "habit_topics",
        joinColumns = @JoinColumn(name = "habit_id"),
        inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private Set<Topic> topics = new HashSet<>();
}
