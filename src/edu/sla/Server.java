package edu.sla;

import javafx.application.Application;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class Server extends Application {
    private static SynchronizedQueue inputQueue;
    private static SynchronizedQueue outputQueue;
    private ComboBox GUIchoiceSystem;


    @Override
    public void start(final Stage stage) {
        GUI gui = new GUI("Server", inputQueue, outputQueue, 1);
        gui.run(stage);
    }

    public static void main(String[] args) {
        // inputQueue communicates images from Server to GUIUpdater
        inputQueue = new SynchronizedQueue();
        // outputQueue communicates images from GUI to Server
        outputQueue = new SynchronizedQueue();

        // Create a thread that creates a ServerSocket and handles incoming client Sockets
        ServerNetworking serverNetworking = new ServerNetworking(inputQueue, outputQueue);
        Thread serverNetworkingThread = new Thread(serverNetworking);
        serverNetworkingThread.setName("serverNetworkingThread");
        serverNetworkingThread.start();

        // Start the Server's GUI thread
        Application.launch(args);
    }

//    public void run() {
//        while (!Thread.interrupted()) {
//            // Ask queue for a file to open
//            String next = outputQueue.get();
//            System.out.println("HI 3");
//            while (next == null) {
//                Thread.currentThread().yield();
//                next = outputQueue.get();
//            }
//            System.out.println("HI 2");
//            // FINALLY I have a file to do something with
//            GUIchoiceSystem.getItems().add(next);
//        }
//    }
}
