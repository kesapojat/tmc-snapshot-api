package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.service.ParticipantService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@ActiveProfiles("test")
public final class ParticipantControllerTest {

    private static final String HY_INSTANCE = "hy";

    @Autowired
    private WebApplicationContext ctx;

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private ParticipantController participantController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(participantController).build();
    }

    @Test
    public void shouldFetchParticipantFromService() throws Exception {

        final Participant participant = new Participant("hiphei");
        when(participantService.find(HY_INSTANCE, "hiphei")).thenReturn(participant);

        mockMvc.perform(get("/hy/participants/hiphei"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("hiphei")));

        verify(participantService).find("hy", "hiphei");
        verifyNoMoreInteractions(participantService);
    }

    @Test
    public void shouldReturn404OnNonExistantParticipantId() throws Exception {

        when(participantService.find(HY_INSTANCE, "noSuchUser")).thenThrow(new NotFoundException());

        mockMvc.perform(get("/hy/participants/noSuchUser"))
                .andExpect(status().is(404));
    }
}
