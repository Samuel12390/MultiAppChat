package edu.sla;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.sun.tools.internal.xjc.addon.sync.SynchronizedMethodAddOn;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;

public class GUI {

    private SynchronizedQueue inputQueue;
    private SynchronizedQueue outputQueue;
    private ComboBox SENDER_comboBox;
    private ComboBox RECEIVER_comboBox;
    private String name;
    public int numOfClients;

    // networking needed by GUI to respond to GUI usage
    private BufferedReader reader;
    private PrintWriter writer;
    private OutputStream out;

    GUI(String s, SynchronizedQueue in, SynchronizedQueue out, int n) {
        name = s;
        inputQueue = in;
        outputQueue = out;
        numOfClients = n;
    }

    GUI(String s, SynchronizedQueue in, SynchronizedQueue out) {
        name = s;
        inputQueue = in;
        outputQueue = out;
    }

    public void setClientNetworking(BufferedReader r, PrintWriter w, OutputStream stream) {
        reader = r;
        writer = w;
        out = stream;
    }

         public void run(final Stage primaryStage) {
             RECEIVER_comboBox = new ComboBox();

             GUIUpdater updater = new GUIUpdater(inputQueue, RECEIVER_comboBox);
             Thread updaterThread = new Thread(updater);
             updaterThread.start();


             // Create and start the sender thread
             TextSender sender = new TextSender(outputQueue, writer, out);
             Thread senderThread = new Thread(sender);
             senderThread.start();

             primaryStage.setTitle("My Box Chat");
             SENDER_comboBox = new ComboBox();

             SENDER_comboBox.setEditable(true);
             RECEIVER_comboBox.setEditable(true);

             primaryStage.setTitle(name + " Box Chat" + numOfClients);


             Button SENDER_sendButton = new Button("Send to the Receiving ChoiceBox");
             SENDER_sendButton.setOnAction((event) -> {
                 String sendersSelection = SENDER_comboBox.getValue().toString();
                 System.out.println("SENDER_sendButton 1 " + sendersSelection);
                 while (!outputQueue.put(sendersSelection)) {
                     Thread.currentThread().yield();
                 }
                 System.out.println("SENDER_sendButton 2 " + sendersSelection);
             });

             VBox vertical = new VBox(40);

             vertical.getChildren().addAll(SENDER_comboBox, SENDER_sendButton, RECEIVER_comboBox);

             primaryStage.setScene(new Scene(vertical));
             primaryStage.show();
             if (numOfClients < 10) {
                 numOfClients++;
             }
         }

    }

