/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmidi.task;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author normal
 */
public class MidiPlayTaskTest {

    private static final Logger LOG = LoggerConfigurator.getlnstance().getCallerLogger();

    private final File name = new File("./testdata/entertainer.mid");
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
        File name_dummy = new File("./testdata/entertainer2.mid");
        try (MidiPlayTask instance = new MidiPlayTask(name_dummy, count)) {
		}
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_isFile() throws MidiUnavailableException, IOException, FileNotFoundException, InvalidMidiDataException {
        File name_dummy = new File("./testdata/");
        try (MidiPlayTask instance = new MidiPlayTask(name_dummy, count)) {
		}
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
        LOG.info("getLoopCount");
        try (MidiPlayTask instance = new MidiPlayTask(name, count)) {
			int expResult = count;
			int result = instance.getLoopCount();
			assertEquals(expResult, result);
		}
    }

    /**
     * Test of getMicrosecondLength method, of class MidiPlayTask.
     *
     * @throws javax.sound.midi.MidiUnavailableException
     */
    @Test
    public void testGetMicrosecondLength() throws IllegalArgumentException, MidiUnavailableException, IOException, FileNotFoundException, InvalidMidiDataException {
        LOG.info("getMicrosecondLength");
        try (MidiPlayTask instance = new MidiPlayTask(name, count)) {
			long expResult = 71999928L;
			long result = instance.getMicrosecondLength();
			assertEquals(expResult, result);
		}
    }

    /**
     * Test of getMicrosecondPosition method, of class MidiPlayTask.
     *
     * @throws javax.sound.midi.MidiUnavailableException
     */
    @Test
    public void testGetMicrosecondPosition() throws IllegalArgumentException, MidiUnavailableException, IOException, FileNotFoundException, InvalidMidiDataException {
        LOG.info("getMicrosecondPosition");
        try (MidiPlayTask instance = new MidiPlayTask(name, count)) {
			long expResult = 0L;
			long result = instance.getMicrosecondPosition();
			assertEquals(expResult, result);
		}
    }

    /**
     * Test of run method, of class MidiPlayTask.
     */
    @Test
    public void testRun() throws IOException, FileNotFoundException, MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        LOG.info("run");
        ScheduledExecutorService playerRunner = Executors.newSingleThreadScheduledExecutor();

        try (MidiPlayTask instance = new MidiPlayTask(name, count)) {
            playerRunner.execute(instance);
            Thread.sleep(20000);
            instance.stop();
        }
    }

}
