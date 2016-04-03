/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmidi.gui;

import javax.swing.JProgressBar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import playmidi.task.MidiPlayTask;

/**
 *
 * @author normal
 */
public class ProgressBarUpdater implements Runnable {

    private static final Log LOG = LogFactory.getLog(ProgressBarUpdater.class);

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
        LOG.trace(temp);
        this.bar.setValue((int) temp);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            this.bar.setValue(this.bar.getMinimum());
            LOG.error(e);
        }
    }

}
