package com.tracktopiasapi.one.web.controller;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Topic;
import com.tracktopiasapi.one.model.mapper.TopicMapper;
import com.tracktopiasapi.one.services.TopicService;
import com.tracktopiasapi.one.web.dto.TopicCreationDto;
import com.tracktopiasapi.one.web.dto.TopicDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;
    private final TopicMapper topicMapper;

    @GetMapping
    public ResponseEntity<List<TopicDto>> getAllTopicsByUserId(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        var topics = topicService.getAllTopicsByUserId(userDetails.getId());
        return ResponseEntity.ok(topicMapper.toTopicDTOList(topics));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDto> getTopicById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.of(
                topicService.getTopicById(id)
                    .filter(topic -> topic.getUser().getId().equals(userDetails.getId()))
                    .map(topicMapper::toTopicDTO)
        );
    }

    @PostMapping
    public ResponseEntity<TopicDto> createTopic(
            @RequestBody @Valid TopicCreationDto topicDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Topic topic = topicMapper.toTopic(topicDto);
            Topic createdTopic = topicService.createTopic(topic, userDetails);
            return ResponseEntity.ok(topicMapper.toTopicDTO(createdTopic));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDto> updateTopic(
            @PathVariable Long id,
            @RequestBody @Valid TopicCreationDto topicDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            return topicService.getTopicById(id)
                .filter(topic -> topic.getUser().getId().equals(userDetails.getId()))
                .map(existingTopic -> {
                    Topic topicDetails = topicMapper.toTopic(topicDto);
                    return topicService.updateTopic(id, topicDetails)
                            .map(topicMapper::toTopicDTO)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            return topicService.getTopicById(id)
                .filter(topic -> topic.getUser().getId().equals(userDetails.getId()))
                .map(topic -> {
                    if (topicService.deleteTopic(id)) {
                        return ResponseEntity.noContent().<Void>build();
                    }
                    return ResponseEntity.notFound().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
