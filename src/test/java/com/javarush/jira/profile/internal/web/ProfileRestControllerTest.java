package com.javarush.jira.profile.internal.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static com.javarush.jira.login.internal.web.UserTestData.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;



class ProfileRestControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    void testGetProfileSuccess() throws Exception {
        long existingUserId = 1;
        mockMvc.perform(get(ProfileRestController.REST_URL)
                        .with(user(new AuthUser(user))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingUserId));

    }

    @Test
    void testUpdateProfileSuccess() throws Exception {
        Set<String> mailNotifications = Set.of("WEEKLY");
        Set<ContactTo> contacts = Set.of(new ContactTo("codeExample", "valueExample"));

        ProfileTo updatedProfileTo = new ProfileTo(1L, mailNotifications, contacts);
        mockMvc.perform(put(ProfileRestController.REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedProfileTo))
                        .with(csrf())
                        .with(user(new AuthUser(user))))
                .andExpect(status().isNoContent());
    }
}