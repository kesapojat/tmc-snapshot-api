package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.TmcParticipant;
import fi.helsinki.cs.tmc.snapshot.api.service.ParticipantService;
import fi.helsinki.cs.tmc.snapshot.api.service.TmcService;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instance}/participants", produces = "application/json")
public final class ParticipantController {

    @Autowired
    private TmcService tmcService;

    @Autowired
    private ParticipantService participantService;

    @RequestMapping(method = RequestMethod.GET)
    public List<TmcParticipant> list(@PathVariable final String instance) throws IOException {

        return tmcService.findAll(instance);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{username}")
    public Participant read(@PathVariable final String instance, @PathVariable final String username) throws IOException {

        return participantService.find(instance, username);
    }
}
