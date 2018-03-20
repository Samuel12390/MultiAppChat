package edu.sla;

import javafx.scene.control.ComboBox;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;

public class TextSender implements Runnable {
    private SynchronizedQueue outputQueue;
    private PrintWriter writer;
    private OutputStream out;

    TextSender(SynchronizedQueue queue, PrintWriter w, OutputStream stream) {
        outputQueue = queue;
        out = stream;
        writer = w;
    }

    public void run() {
        while(true) {
            String message;
            System.out.println("TextSender Run 1");
            while ((message = outputQueue.get()) == null) {
                Thread.currentThread().yield();
            }
            System.out.println("MessageChat TextSender: will send " + message + "from");
            writer.println(message);
            writer.flush();
        }

    }

}