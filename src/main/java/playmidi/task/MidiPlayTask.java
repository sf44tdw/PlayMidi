/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmidi.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 指定の回数だけファイルの再生を行い、再生中は待機する。
 *
 * @author normal
 */
public final class MidiPlayTask implements Runnable, AutoCloseable {
    
    private static final Log LOG = LogFactory.getLog(MidiPlayTask.class);
    
    private final File midiFile;
    private final int loopCount;
    private final Sequencer sequencer;
    
    public MidiPlayTask(File midiFile, int count) throws FileNotFoundException, IOException, MidiUnavailableException, InvalidMidiDataException {
        
        this.midiFile = midiFile;
        this.loopCount = count;
        
        if (this.midiFile == null) {
            throw new NullPointerException("ファイルが指定されていません。");
        }
        if (!midiFile.exists()) {
            MessageFormat msg1 = new MessageFormat("ファイルが見つかりません。File={0}");
            Object[] parameters1 = {this.midiFile.getAbsolutePath()};
            throw new FileNotFoundException(msg1.format(parameters1));
        }
        if (!midiFile.isFile()) {
            MessageFormat msg1 = new MessageFormat("ファイルではありません。File={0}");
            Object[] parameters1 = {this.midiFile.getAbsolutePath()};
            throw new IllegalArgumentException(msg1.format(parameters1));
        }
        if (!midiFile.canRead()) {
            MessageFormat msg1 = new MessageFormat("ファイルが読み込めません。File={0}");
            Object[] parameters1 = {this.midiFile.getAbsolutePath()};
            throw new IllegalArgumentException(msg1.format(parameters1));
        }
        MessageFormat msg1 = new MessageFormat("File={0},LoopCount={1}");
        Object[] parameters1 = {this.midiFile.getAbsolutePath(), loopCount};
        LOG.info(msg1.format(parameters1));
        
        LOG.trace("シーケンサ初期化開始");
        sequencer = MidiSystem.getSequencer();
        sequencer.setLoopEndPoint(-1L);
        sequencer.setLoopCount(loopCount);
        sequencer.open();
        LOG.trace("シーケンサ初期化完了");
        
        LOG.trace("ファイル読み込み開始");
        try (FileInputStream in = new FileInputStream(this.midiFile)) {
            Sequence sequence = MidiSystem.getSequence(in);
            sequencer.setLoopCount(count);
            sequencer.setSequence(sequence);
        }
        LOG.trace("ファイル読み込み完了");
    }
    
    public int getLoopCount() {
        return loopCount;
    }
    
    public synchronized long getMicrosecondLength() {
        return sequencer.getMicrosecondLength();
    }
    
    public synchronized long getMicrosecondPosition() {
        return sequencer.getMicrosecondPosition();
    }
    
    @Override
    public synchronized void run() {
        try {
            this.play(loopCount);
        } catch (InterruptedException ex) {
            this.close();
            LOG.error(ex);
        }
    }

    /**
     * midiファイルを指定の回数再生する
     */
    private synchronized void play(int count) throws InterruptedException {
        sequencer.setLoopCount(count);
        MessageFormat msg1 = new MessageFormat("再生開始。File={0},LoopCount={1},Length(MicroSecnd)={2}");
        Object[] parameters1 = {midiFile.getAbsolutePath(), count, sequencer.getMicrosecondLength()};
        LOG.info(msg1.format(parameters1));
        sequencer.start();
    }

    /**
     * 再生を停止する。
     */
    public synchronized void stop() {
        if (sequencer.isRunning()) {
            sequencer.stop();
            LOG.info("再生停止");
        }
    }

    //シーケンサーを解放する。
    @Override
    public synchronized void close() throws IllegalStateException {
        stop();//停止
        sequencer.close();
        LOG.info("クローズ");
    }
    
}
