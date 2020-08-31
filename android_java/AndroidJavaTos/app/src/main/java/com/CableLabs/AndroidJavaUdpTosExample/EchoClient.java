package com.CableLabs.AndroidJavaUdpTosExample;

import java.io.IOException;
import java.net.*;

// The following import is needed for the deprecated Socket.setOptions()
// import static java.net.StandardSocketOptions.IP_TOS;

/*
This class is an interface for the client side of an echo server client pair with the ability to set the tos byte or DSCP value of a
 */

public class EchoClient {
    private DatagramSocket socket;
    private InetAddress address;

//  data array for messages
    private byte[] buf;

//  default constructor for localhost traffic
    public EchoClient() throws UnknownHostException, SocketException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");

    }

//  constructor that takes the server IPv4 addr of the EchoServer
    public EchoClient(String addr) throws UnknownHostException, SocketException {
        socket = new DatagramSocket();
        address = InetAddress.getByName(addr);
    }

//  getter method for the current tos value
    public int getTos() throws SocketException {
        return socket.getTrafficClass();
    }

//  setter for tos value
    public void setTos(int tos) throws SocketException {

        /*
        Java implementations vary in their method of setting the tos byte and in effect.
        The following are general recommendations for android compatibility

        - The DatagramSocket and Socket classes both have a setTrafficClass() method
        --- The value of the tos byte can be set to any value from 0 to 255 inclusive
        --- If an attempt is made to set the tos out of range behavior varies

        - Current recommendation is to use DSCP CS5 for universal compatibly
        --- to do this pass int 160 or a0 in hexadecimal to socket.setTrafficClass()
        --- in a traffic moniter (like wireshark) check the dscp classifacation in the ip header
        */

        socket.setTrafficClass(tos);

        //   The below is a deprecated method for setting the traffic class still supported on some non android platforms
        //   socket.setOption(IP_TOS, tos);
    }

//  Method that sends udp packet to EchoServer and receives a reflection of that packet
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
