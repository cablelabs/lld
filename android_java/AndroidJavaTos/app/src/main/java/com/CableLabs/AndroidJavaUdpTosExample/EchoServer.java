package com.CableLabs.AndroidJavaUdpTosExample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// The following import is needed for the deprecated Socket.setOptions()
// import static java.net.StandardSocketOptions.IP_TOS;

/*
The following Class is a runnable reflection server which receives a DatagramPacket and sends back a copy of that packet to the sender.

If the packet data when decode from ascii is the String "end" the server closes the DatagramSocket and sets the isRunning bool to false
If the packet is an int, the Socket.setTrafficClass() method is set to that int
- for a full description of this functionality see EchoClient.java
 */

public class EchoServer implements Runnable {

    private DatagramSocket socket;
    private volatile boolean running = true;
    private volatile boolean newOuput = false;
    private byte[] buf = new byte[256];
    private String end = "end";
    private static volatile String output = "";

    public EchoServer() throws IOException {
        socket = new DatagramSocket(4445);
    }

//  Getters for triggering retival of thread output and join
    public boolean isRunning(){return running;}

    public boolean isNewOuput() {return newOuput;}

//  getter for ouput
    public String getOutput() {
        newOuput = false;
        return output;}

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
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received = "";
            received = new String(packet.getData(), 0, len, "ASCII");

            System.out.println("server sees: " + received);

            output = "\nserver sees: " + received;
            newOuput = true;

//          check for an end command
            if (received.equals(end)) {
                running = false;
            }
/*
            sets traffic class to the value of the data received in packet

            There is not support for inspecting the IP Header of packet in java

            if you send a message to this server other than "end" or a valid DSCP value it will throw an exception
 */
            if (!(received.equals("end"))) {

                socket.setTrafficClass(Integer.valueOf(received));
            }

            socket.send(packet);
        }
        }
        catch (IOException e) {
            System.out.println("IOException occurred on send or receive");
            output = "\nIOException occurred on send or receive";

            e.printStackTrace();
        }
        socket.close();
    }
}