package com.marius.dienorastis.controllers;

import com.marius.dienorastis.EntryService;
import com.marius.dienorastis.MyUserDetailsService;
import com.marius.dienorastis.models.Entry;
import com.marius.dienorastis.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MyUserDetailsService userDetailsServiceMock;
    @MockBean
    EntryService entryServiceMock;

    @Test
    void shouldGetAllEntriesForCurrentUser() throws Exception {
        this.mockMvc.perform(get("/get")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void shouldSaveANewEntry() throws Exception {
        String postContent = "{\n" +
                "\"name\": \"test name\",\n" +
                "\"content\": \"test content\"\n" +
                "}";

        User testUser = new User();
        testUser.setEmail("email@email.com");
        testUser.setId(1);
        testUser.setPassword("pass");

        when(userDetailsServiceMock.getCurrentUser()).thenReturn(testUser);

        MvcResult result = this.mockMvc.perform(post("/save").contentType(MediaType.APPLICATION_JSON)
                .content(postContent))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString().contains(postContent));
    }

    @Test
    void shouldReturnNotFoundWhenDeletingAnEntryThatDoesNotExist() throws Exception {
        this.mockMvc.perform(delete("/delete", 0))
                .andDo(print())
                .andExpect(status().is(404));
    }

    @Test
    void shouldDeleteEntryWhenEntryExists() throws Exception {
        when(entryServiceMock.get(1)).thenReturn(java.util.Optional.of(new Entry()));

        this.mockMvc.perform(delete("/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateEntry() throws Exception {
        String putContent = "{\n" +
                "\"name\": \"test name\",\n" +
                "\"content\": \"test content\"\n" +
                "}";

        when(entryServiceMock.get(1)).thenReturn(java.util.Optional.of(new Entry()));

        MvcResult result = this.mockMvc.perform(put("/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(putContent))
                .andExpect(status().isOk()).andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString().contains(putContent));
    }

    @Test
    void shouldRegisterANewUser() throws Exception {
        String userJson = "{\n" +
                "\t\"email\" : \"a@a.com\",\n" +
                "\t\"password\": \"aaaaaaa\"\n" +
                "}";

        this.mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnTrueWhenEmailIsValid() {
        Assertions.assertThat(AppController.isValidEmail("a@aa.com")).isTrue();
    }

    @Test
    void shouldReturnFalseWhenGivenEmailIsNotValid() {
        Assertions.assertThat(AppController.isValidEmail("not a valid email")).isFalse();
        Assertions.assertThat(AppController.isValidEmail(null)).isFalse();
    }

}