/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template midiFile, choose Tools | Templates
 * and open the template in the editor.
 */
package playmidi.gui;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFileChooser;

import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;
import playmidi.task.MidiPlayTask;

/**
 * 再生ボタンを押すと、第一引数で指定されたパスか、あるいは選択ボタンで選んだパスのmidiファイルを延々と再生する。
 *
 * @author normal
 */
public class Main extends javax.swing.JFrame {

	private static final Logger LOG = LoggerConfigurator.getlnstance().getCallerLogger();
    private static String arg = "";

    private ScheduledExecutorService barRunner = null;
    private ScheduledExecutorService playerRunner = null;
    private MidiPlayTask pTask = null;

    /**
     * Creates new form PlayMidi
     */
    public Main() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        FileSelectButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        FilePath = new javax.swing.JLabel();
        PositionBar = new javax.swing.JProgressBar();
        PlayButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setText("File");

        FileSelectButton.setText("Select");
        FileSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileSelectButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Position");

        PositionBar.setStringPainted(true);

        PlayButton.setText("Play");
        PlayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FilePath, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PositionBar, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(FileSelectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PlayButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FileSelectButton)
                    .addComponent(FilePath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PlayButton)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(PositionBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void PlayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayButtonActionPerformed
        if (this.pTask != null) {
            barRunner = Executors.newSingleThreadScheduledExecutor();
            playerRunner = Executors.newSingleThreadScheduledExecutor();
            ProgressBarUpdater pup = new ProgressBarUpdater(this.PositionBar, this.pTask);
            barRunner.scheduleAtFixedRate(pup, 0, 1000, TimeUnit.MILLISECONDS);
            playerRunner.execute(pTask);
        }
    }//GEN-LAST:event_PlayButtonActionPerformed

    private void FileSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FileSelectButtonActionPerformed
        JFileChooser filechooser = new JFileChooser();
        filechooser.setFileFilter(new MidiFileFilter());
        int selected = filechooser.showOpenDialog(this);
        if (selected == JFileChooser.APPROVE_OPTION) {
            File midiFile = filechooser.getSelectedFile();
            FilePath.setText("");
            this.openMidiFile(midiFile);
        }
    }//GEN-LAST:event_FileSelectButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (this.pTask != null) {
            this.pTask.close();
        }
        if (this.playerRunner != null) {
            this.playerRunner.shutdownNow();
        }
        if (this.barRunner != null) {
            this.barRunner.shutdownNow();
        }
    }//GEN-LAST:event_formWindowClosing

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (arg != "") {
            File f = new File(arg);
            this.openMidiFile(f);
        }
    }//GEN-LAST:event_formWindowOpened

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            LOG.error("エラー",ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });

        if (!"".equals(args[0])) {
            arg = args[0];
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FilePath;
    private javax.swing.JButton FileSelectButton;
    private javax.swing.JButton PlayButton;
    private javax.swing.JProgressBar PositionBar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables

    private void openMidiFile(File f) {
        if (checkBeforeReadfile(f)) {

            if (this.pTask != null) {
                LOG.info("別のタスクがあるようならクローズする。");
                this.pTask.close();
                this.PositionBar.setValue(this.PositionBar.getMinimum());
            }

            try {
                FilePath.setText(f.getAbsolutePath());
                this.pTask = new MidiPlayTask(f, 1);
            } catch (IllegalArgumentException | MidiUnavailableException | IOException | InvalidMidiDataException ex) {
                this.pTask = null;
                LOG.error("エラー",ex);
            }
        } else {
            this.pTask = null;
            FilePath.setText("ファイルが見つからないか開けません");
        }
    }

    private static boolean checkBeforeReadfile(File file) {
        if (file.exists()) {
            if (file.isFile() && file.canRead()) {
                return true;
            }
        }
        return false;
    }

}
