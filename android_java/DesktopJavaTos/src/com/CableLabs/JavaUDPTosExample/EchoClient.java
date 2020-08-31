package com.CableLabs.JavaUDPTosExample;

import java.io.IOException;
import java.net.*;


/*
Desktop class corresponding to EchoClient.java in com.CableLabs.AndroidJavaUdpTosExample
 */

public class EchoClient {
    private DatagramSocket socket;
    private InetAddress address;


    private byte[] buf;

    public EchoClient() throws UnknownHostException, SocketException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");

    }

    public EchoClient(String addr) throws UnknownHostException, SocketException {
        socket = new DatagramSocket();
        address = InetAddress.getByName(addr);
    }


    public int getTos() throws SocketException {
        return socket.getTrafficClass();
    }

    public void setTos(int tos) throws SocketException {
        socket.setTrafficClass(tos);

//       socket.setOption(IP_TOS, tos);
//       The above is a deprecated method for setting the tos byte.
//       Be sure the use the needed import.
    }

    public String sendEcho(String msg) throws IOException {
        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, 4445);

        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }
}
