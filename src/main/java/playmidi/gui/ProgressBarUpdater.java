/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmidi.gui;

import javax.swing.JProgressBar;

import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;
import playmidi.task.MidiPlayTask;

/**
 *
 * @author normal
 */
public class ProgressBarUpdater implements Runnable {

	private static final Logger LOG = LoggerConfigurator.getlnstance().getCallerLogger();
    private final JProgressBar bar;
    private final MidiPlayTask pTask;

    public ProgressBarUpdater(JProgressBar bar, MidiPlayTask pTask) {
        this.bar = bar;
        if (this.bar == null) {
            throw new NullPointerException("プログレスバーなし");
        }
        this.pTask = pTask;
        if (this.pTask == null) {
            throw new NullPointerException("プレイヤータスクなし");
        }
        this.bar.setMinimum(0);
        this.bar.setMaximum(1000);
    }

    @Override
    public void run() {
        long temp = (this.pTask.getMicrosecondPosition() * 1000) / this.pTask.getMicrosecondLength();
        if (LOG.isTraceEnabled()) LOG.trace(Long.toString(temp));
        this.bar.setValue((int) temp);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            this.bar.setValue(this.bar.getMinimum());
            LOG.error("エラー",e);
        }
    }

}
