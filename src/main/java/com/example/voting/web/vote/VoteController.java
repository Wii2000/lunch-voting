package com.example.voting.web.vote;

import com.example.voting.model.Restaurant;
import com.example.voting.model.Vote;
import com.example.voting.repository.RestaurantRepository;
import com.example.voting.repository.VoteRepository;
import com.example.voting.web.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

import static com.example.voting.util.ValidationUtil.checkNotFoundWithId;
import static com.example.voting.util.TimeUtil.getVoteDate;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VoteController {
    static final String REST_URL = "/api/votes";

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private VoteRepository voteRepository;

    @PostMapping
    public ResponseEntity<Vote> createWithLocation(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam int restaurantId
    ) {
        log.info("create {}", restaurantId);
        Restaurant restaurant = checkNotFoundWithId(
                restaurantRepository.findById(restaurantId).orElse(null),
                restaurantId
        );
        LocalDate voteDate = getVoteDate();

        Vote vote = voteRepository.findByRegisteredAndUserId(voteDate, authUser.id());
        if (vote != null) {
            vote.setRestaurant(restaurant);
        } else {
            vote = voteRepository.save(
                    new Vote(null, authUser.getUser(), restaurant, voteDate)
            );
        }

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(vote.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(vote);
    }
}
