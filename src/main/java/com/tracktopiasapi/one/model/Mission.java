package com.tracktopiasapi.one.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "missions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"topics"})
@EqualsAndHashCode(exclude = {"topics"})
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "due_date")
    private Date dueDate;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false)
    private String priority;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToMany
    @JoinTable(
        name = "mission_topics",
        joinColumns = @JoinColumn(name = "mission_id"),
        inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private Set<Topic> topics = new HashSet<>();
}
