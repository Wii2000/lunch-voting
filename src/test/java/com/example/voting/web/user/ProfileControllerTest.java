package com.example.voting.web.user;

import com.example.voting.model.User;
import com.example.voting.repository.UserRepository;
import com.example.voting.to.UserTo;
import com.example.voting.util.JsonUtil;
import com.example.voting.util.UserUtil;
import com.example.voting.web.AbstractControllerTest;
import com.example.voting.web.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.voting.util.JsonUtil.writeValue;
import static com.example.voting.web.user.UserTestUtil.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProfileControllerTest extends AbstractControllerTest {
    static final String URL = ProfileController.REST_URL;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(user));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL))
                .andExpect(status().isNoContent());
        MATCHER.assertMatch(userRepository.findAll(), admin, user2);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "newUser", "new@mail.com", "newpass");
        MvcResult result = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTo)))
                .andExpect(status().isCreated())
                .andReturn();

        User registered = JsonUtil.readValue(MATCHER.getContent(result), User.class);
        User newUser = UserUtil.createNewFromTo(newTo);
        int newId = registered.id();
        newUser.setId(newId);
        MATCHER.assertMatch(registered, newUser);
        MATCHER.assertMatch(userRepository.getById(newId), newUser);
    }

    @Test
    void registerInvalid() throws Exception {
        UserTo newTo = new UserTo(null, null, null, null);
        perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void registerDuplicate() throws Exception {
        UserTo newTo = new UserTo(null, "newUser", USER_MAIL, "password");
        perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        UserTo updateTo = new UserTo(null, "updated", "update@mail.com", "updated");
        perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updateTo)))
                .andExpect(status().isNoContent());

        User updated = userRepository.getById(USER_ID);
        User fromTo = UserUtil.createNewFromTo(updateTo);
        fromTo.setId(updated.id());
        MATCHER.assertMatch(updated, fromTo);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalid() throws Exception {
        UserTo updateTo = new UserTo(null, null, null, null);
        perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updateTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateDuplicate() throws Exception {
        UserTo updateTo = new UserTo(null, "updated", ADMIN_MAIL, "updated");
        perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updateTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL)));
    }
}