package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.util.EventProcessor;
import fi.helsinki.cs.tmc.snapshot.api.util.EventTransformer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SnapshotServiceImpl implements SnapshotService {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private EventProcessor eventProcessor;

    @Autowired
    private EventTransformer eventTransformer;

    @Override
    public List<Snapshot> find(final String instance, final String username, final String course, final String exercise) throws IOException {

        final Collection<SnapshotEvent> events = exerciseService.find(instance, username, course, exercise).getSnapshotEvents();

        System.out.println(events.size());

        System.out.println("EVENTS:");
        for (SnapshotEvent event : events) {
            System.out.println(event.getHappenedAt());
        }

        eventProcessor.process(events);
        return eventTransformer.toSnapshotList(events);

    }

    @Override
    public Snapshot find(final String instance, final String username, final String course, final String exercise, final Long snapshot) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

}
