package com.example.voting.web.menu;

import com.example.voting.to.MenuTo;
import com.example.voting.util.JsonUtil;
import com.example.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.example.voting.web.Matcher.getContent;
import static com.example.voting.web.menu.AdminMenuController.REST_URL;
import static com.example.voting.web.user.UserTestUtil.ADMIN_MAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuControllerTest extends AbstractControllerTest {
    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/by?date=" + LocalDate.now().plusDays(1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(JsonUtil.readValues(getContent(result), MenuTo.class)).hasSize(2))
                .andDo(print());
    }
}
