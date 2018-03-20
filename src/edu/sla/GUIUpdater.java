package edu.sla;

import javafx.scene.control.ComboBox;

public class GUIUpdater implements Runnable {
    private SynchronizedQueue originalQueue;
    private ComboBox GUIchoiceSystem;

    GUIUpdater(SynchronizedQueue queue, ComboBox comboBox) {
        originalQueue = queue;
        GUIchoiceSystem = comboBox;
    }

    public void run() {
        while (!Thread.interrupted()) {
            // Ask queue for a file to open
            String next = originalQueue.get();
            System.out.println("GUIUpdater run 1");
            while (next == null) {
                Thread.currentThread().yield();
                next = originalQueue.get();
            }
            System.out.println("GUIUpdater run 2");
            // FINALLY I have a file to do something with
            GUIchoiceSystem.getItems().add(next);

        }
    }
}