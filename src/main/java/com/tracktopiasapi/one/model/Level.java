package com.tracktopiasapi.one.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "levels")
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int level;

    private int score;

    private int scoreToNextLevel;

    @OneToOne(mappedBy = "level")
    private User user;

    public void addScore(int points) {
        this.score += points;
        checkLevelUp();
    }

    public void removeScore(int points) {
        if (this.score >= points) {
            this.score -= points;
        } else {
            if (this.level > 1) {
                int remainingPointsToRemove = points - this.score;
                this.level--;
                this.scoreToNextLevel = this.level * 100;

                this.score = this.scoreToNextLevel - remainingPointsToRemove;

                if (this.score < 0) {
                    this.score = 0;
                }
            } else {

                this.score = 0;
            }
        }
    }

    private void checkLevelUp() {
        while (score >= scoreToNextLevel) {
            level++;

            score = 0;
            scoreToNextLevel = level * 100;
        }
    }
}
