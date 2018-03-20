package edu.sla;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

    public class HandleThatCommunication implements Runnable{
        private InputStream in;
        private ArrayList clientOutputStreams;
        private BufferedReader reader;
        private boolean isServer;
        private SynchronizedQueue inputQueue;
        private OutputStream out;


        public HandleThatCommunication(Socket sock, SynchronizedQueue inQueue, ArrayList streams) {
            inputQueue = inQueue;
            isServer = true;

            try {
                in = sock.getInputStream();
                InputStreamReader incomingDataReader = new InputStreamReader(in);
                reader = new BufferedReader(incomingDataReader);
                clientOutputStreams = streams;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("MessageChat HandleThatCommunication: Server creation failed");
            }
        }

        public HandleThatCommunication(Socket sock, SynchronizedQueue inQueue, BufferedReader r, OutputStream stream) {
            isServer = false;
            reader = r;
            inputQueue = inQueue;
            out = stream;

            try {
                in = sock.getInputStream();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("MessageChat HandleThatCommunication: Client creation failed");
            }
        }

        public void run() {
            try {
                while(true) {
                    String message;
                    System.out.println("HandleThatCommunication Run 1");
                    while ((message = reader.readLine()) == null) {
                        Thread.currentThread().yield();
                    }
                    System.out.println("HandleThatCommunication: read " + message);
                    while (inputQueue.put(message) == false) {
                        Thread.currentThread().yield();
                    }
                    System.out.println("HandleThatCommunication Run 2");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("MessageChat HandleThatCommunication: reading failed");
            }

        }

        public void tellAllClients(String message) {
            Iterator allClients = clientOutputStreams.iterator();
            while (allClients.hasNext()) {
                try {
                    PrintWriter writer = (PrintWriter) allClients.next();
                    writer.println(message);
                    writer.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("MessageChat HandleThatCommunication: telling all clients failed");
                }
            }
        }
    }
