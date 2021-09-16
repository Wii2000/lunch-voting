package com.example.voting.web.vote;

import com.example.voting.exception.IllegalRequestDataException;
import com.example.voting.exception.UniqueConstraintException;
import com.example.voting.model.Vote;
import com.example.voting.repository.RestaurantRepository;
import com.example.voting.repository.VoteRepository;
import com.example.voting.util.TimeUtil;
import com.example.voting.web.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.voting.util.ValidationUtil.checkNotFoundWithId;
import static com.example.voting.web.GlobalExceptionHandler.EXCEPTION_CHANGE_VOTE;
import static com.example.voting.web.GlobalExceptionHandler.EXCEPTION_DUPLICATE_VOTE;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VoteController {
    static final String REST_URL = "/api/votes";

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private Clock clock;

    @GetMapping("/{id}")
    @Operation(summary = "Get user's own vote by id")
    public ResponseEntity<Vote> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(voteRepository.findByUserIdAndId(authUser.id(), id));
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Vote for restaurant")
    public ResponseEntity<Vote> createWithLocation(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam int restaurantId
    ) {
        log.info("create {}", restaurantId);

        if (voteRepository.findByRegisteredAndUserId(LocalDate.now(), authUser.id()).isPresent()) {
            throw new UniqueConstraintException(EXCEPTION_DUPLICATE_VOTE);
        }

        Vote vote = voteRepository.save(new Vote(
                null,
                authUser.getUser(),
                checkNotFoundWithId(restaurantRepository.findById(restaurantId).orElse(null), restaurantId),
                LocalDate.now()
        ));

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(vote.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(vote);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @Operation(summary = "Update user's own vote")
    public void update(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam int restaurantId,
            @PathVariable int id
    ) {
        log.info("update {}", restaurantId);
        if (LocalTime.now(clock).isAfter(TimeUtil.VOTING_CHANGE_END_TIME)) {
            throw new IllegalRequestDataException(EXCEPTION_CHANGE_VOTE);
        }
        voteRepository.save(
                new Vote(id, authUser.getUser(), restaurantRepository.getById(restaurantId), LocalDate.now())
        );
    }
}
