package com.CableLabs.JavaUDPTosExample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/*
Desktop class corresponding to EchoServer.java in com.CableLabs.AndroidJavaUdpTosExample
 */

public class EchoServer implements Runnable {

    private DatagramSocket socket;
    private volatile boolean running = true;
    private byte[] buf = new byte[256];
    private String end = "end";

    public EchoServer() throws IOException {
        socket = new DatagramSocket(4445);
    }

    public boolean isRunning(){
        return running;
    }

    public void run() {

        try{
        while (running) {
            buf = new byte[256];
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            int len = 0;
            while (!(buf[len] == 0)){
                len++;
            }


            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, len, address, port);
            String received = "";
            received = new String(packet.getData(), 0, len, "ASCII");


            System.out.println("server sees: " + received);
//            System.out.println("received len: " + Integer.toString(received.length()));

            if (received.equals(end)) {
                running = false;

            }

//            System.out.println("Running  = " + Boolean.toString(running));
            if (!(received.equals("end"))) {
               try {
                   int tos = Integer.valueOf(received);
                   socket.setTrafficClass(tos);
//
//                socket.setOption(IP_TOS, Integer.valueOf(received));
//                The above is a deprecated method for setting the tos byte.
//                Be sure the use the needed import.
               } catch (SocketException e) {
                   e.printStackTrace();
               } catch (NumberFormatException e) {
                   e.printStackTrace();
               }
            }

            socket.send(packet);
        }
        }
        catch (IOException e) {
            System.out.println("IOException occurred on send or receive");
            e.printStackTrace();
        }
        socket.close();
    }
}