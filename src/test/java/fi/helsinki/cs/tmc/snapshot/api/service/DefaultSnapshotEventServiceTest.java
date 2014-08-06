package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.util.EventReader;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DefaultSpywareService.class, DefaultSnapshotEventService.class, EventReader.class })
public final class DefaultSnapshotEventServiceTest {

    @Mock
    private DefaultSpywareService spywareService;

    @Mock
    private EventReader eventReader;

    @InjectMocks
    private DefaultSnapshotEventService injectedSnapshotEventService;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        Whitebox.setInternalState(eventReader, new ObjectMapper());
    }

    @Test
    public void shouldFindAllSnapshotEvents() throws Exception {

        final File indexFile = new File("test-data/test.idx");
        final String indexFileContent = FileUtils.readFileToString(indexFile);

        final File dataFile = new File("test-data/test.dat");

        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);

        when(spywareService.fetchIndex("hy", "karpo")).thenReturn(indexFileContent);
        when(spywareService.fetchChunkByRange(any(String.class), any(String.class), any(Integer.class), any(Integer.class)))
                           .thenReturn(Arrays.copyOfRange(bytes, 0, 11683))
                           .thenReturn(Arrays.copyOfRange(bytes, 11683, 12945))
                           .thenReturn(Arrays.copyOfRange(bytes, 12945, 14557));

        when(eventReader.readEvents(any(List.class))).thenCallRealMethod();

        final Collection<SnapshotEvent> events = injectedSnapshotEventService.findAll("hy", "karpo");

        assertNotNull(events);

        assertEquals(64, events.size());
    }

    @Test
    public void shouldNotFailForCorruptedJsonData() throws Exception {

        final File indexFile = new File("test-data/error.idx");
        final String indexFileContent = FileUtils.readFileToString(indexFile);

        final File dataFile = new File("test-data/error.dat");

        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);

        when(spywareService.fetchIndex("hy", "karpo")).thenReturn(indexFileContent);
        when(spywareService.fetchChunkByRange(any(String.class), any(String.class), any(Integer.class), any(Integer.class)))
                           .thenReturn(Arrays.copyOfRange(bytes, 0, 11741));

        when(eventReader.readEvents(any(List.class))).thenCallRealMethod();

        final Collection<SnapshotEvent> events = injectedSnapshotEventService.findAll("hy", "karpo");

        assertNotNull(events);
        assertEquals(0, events.size());
    }

    @Test
    public void shouldNotFailForCorruptedData() throws Exception {

        final File indexFile = new File("test-data/content-error.idx");
        final String indexFileContent = FileUtils.readFileToString(indexFile);

        final File dataFile = new File("test-data/content-error.dat");

        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);

        when(spywareService.fetchIndex("mooc", "pekka")).thenReturn(indexFileContent);
        when(spywareService.fetchChunkByRange(any(String.class), any(String.class), any(Integer.class), any(Integer.class)))
                           .thenReturn(Arrays.copyOfRange(bytes, 0, 710));

        when(eventReader.readEvents(any(List.class))).thenCallRealMethod();

        final Collection<SnapshotEvent> events = injectedSnapshotEventService.findAll("mooc", "pekka");

        assertNotNull(events);
        assertEquals(2, events.size());
    }

    @Test
    public void shouldNotFailForCorruptedPatchData() throws Exception {

        final File indexFile = new File("test-data/patch-data.idx");
        final String indexFileContent = FileUtils.readFileToString(indexFile);

        final File dataFile = new File("test-data/patch-data.dat");

        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);

        when(spywareService.fetchIndex("peliohjelmointi", "pekka")).thenReturn(indexFileContent);
        when(spywareService.fetchChunkByRange(any(String.class), any(String.class), any(Integer.class), any(Integer.class)))
                           .thenReturn(Arrays.copyOfRange(bytes, 0, 550));

        when(eventReader.readEvents(any(List.class))).thenCallRealMethod();

        final Collection<SnapshotEvent> events = injectedSnapshotEventService.findAll("peliohjelmointi", "pekka");

        assertNotNull(events);
        assertEquals(5, events.size());
    }
}
