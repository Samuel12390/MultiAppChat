package edu.sla;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import javafx.application.Application;
import javafx.stage.Stage;

public final class Client extends Application {
        static BufferedReader reader;
        static PrintWriter writer;
        static OutputStream out;
        static SynchronizedQueue inputQueue;
        static SynchronizedQueue outputQueue;

        @Override
        public void start(final Stage stage) {
            GUI gui = new GUI("Client", inputQueue, outputQueue);
            gui.setClientNetworking(reader, writer, out);
            gui.run(stage);

        }

        public static void main(String[] args) {
            try {
                // Set up client-side networking
                Socket sock = new Socket("127.0.0.1", 5000);
                // reader will be receive text from Server and put them into the SynchronizedQueue
                InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamReader);
                // writer will send text to Server as soon as they are gotten from SynchronizedQueue
                out = sock.getOutputStream();
                writer = new PrintWriter(out);

                // Create the queues that will be used for communication
                // inputQueue communicates text from Server to GUIUpdater
                inputQueue = new SynchronizedQueue();
                // outputQueue communicates text from GUI to Server
                outputQueue = new SynchronizedQueue();

                // for every new client, run an IncomingDataReceiver on a new thread to receive data from it
                HandleThatCommunication handlerPlz = new HandleThatCommunication(sock, inputQueue, reader, out);
                Thread handlerThread = new Thread(handlerPlz);
                handlerThread.start();
                System.out.println("TextInput client: created input communication handler");

                // Start the GUI thread
                System.out.println("TextInput client: networking is ready... starting the GUI");
                Application.launch(args);

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("TextInput client: no PictureChat server available.  Exiting....");
            }
        }
}
