package com.example.voting.web.menu;


import com.example.voting.to.MenuTo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MenuController extends AbstractMenuController {
    static final String REST_URL = "/api/menus";

    @GetMapping
    @Operation(summary = "Get restaurants menus for today")
    public List<MenuTo> getToday() {
        log.info("getToday");
        return super.getByDate(LocalDate.now());
    }

}
