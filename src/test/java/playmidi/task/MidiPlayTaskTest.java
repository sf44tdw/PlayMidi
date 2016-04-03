/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmidi.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author normal
 */
public class MidiPlayTaskTest {

    private final File name = new File("H:/music/midi/music/eva.mid");
    private final int count = 1;

    public MidiPlayTaskTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructor_exists() throws MidiUnavailableException, IOException, FileNotFoundException, InvalidMidiDataException {
        File name_dummy = new File("Z:/music/midi/music/eva.mid");
        MidiPlayTask instance = new MidiPlayTask(name_dummy, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_isFile() throws MidiUnavailableException, IOException, FileNotFoundException, InvalidMidiDataException {
        File name_dummy = new File("H:/music/midi/music");
        MidiPlayTask instance = new MidiPlayTask(name_dummy, count);
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructor_canRead() {
//    }
//    @Test(expected = MidiUnavailableException.class)
//    public void testConstructor4() throws MidiUnavailableException {
//    }
    /**
     * Test of getLoopCount method, of class MidiPlayTask.
     *
     * @throws javax.sound.midi.MidiUnavailableException
     */
    @Test
    public void testGetLoopCount() throws IllegalArgumentException, MidiUnavailableException, IOException, FileNotFoundException, InvalidMidiDataException {
        System.out.println("getLoopCount");
        MidiPlayTask instance = new MidiPlayTask(name, count);
        int expResult = count;
        int result = instance.getLoopCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMicrosecondLength method, of class MidiPlayTask.
     *
     * @throws javax.sound.midi.MidiUnavailableException
     */
    @Test
    public void testGetMicrosecondLength() throws IllegalArgumentException, MidiUnavailableException, IOException, FileNotFoundException, InvalidMidiDataException {
        System.out.println("getMicrosecondLength");
        MidiPlayTask instance = new MidiPlayTask(name, count);
        long expResult = 243713897L;
        long result = instance.getMicrosecondLength();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMicrosecondPosition method, of class MidiPlayTask.
     *
     * @throws javax.sound.midi.MidiUnavailableException
     */
    @Test
    public void testGetMicrosecondPosition() throws IllegalArgumentException, MidiUnavailableException, IOException, FileNotFoundException, InvalidMidiDataException {
        System.out.println("getMicrosecondPosition");
        MidiPlayTask instance = new MidiPlayTask(name, count);
        long expResult = 0L;
        long result = instance.getMicrosecondPosition();
        assertEquals(expResult, result);
    }
    private static final Log LOG = LogFactory.getLog(MidiPlayTaskTest.class);

    /**
     * Test of run method, of class MidiPlayTask.
     */
    @Test
    public void testRun() throws IOException, FileNotFoundException, MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        System.out.println("run");
        ScheduledExecutorService playerRunner = Executors.newSingleThreadScheduledExecutor();

        try (MidiPlayTask instance = new MidiPlayTask(name, count)) {
            playerRunner.execute(instance);
            Thread.sleep(20000);
            instance.stop();
        }
    }

}
