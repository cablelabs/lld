package com.CableLabs.JavaUDPTosExample;


import java.io.IOException;

/*
Desktop class corresponding to UDPLoop.java in com.CableLabs.AndroidJavaUdpTosExample
 */

public class UDPLoop {

    public static void main(String[] args) throws IOException, InterruptedException {

        int[] DSCPVals = new int[]{0,8,32,40,48,56,64,72,80,88,96,104,112,120,128,
                136,144,152,160,184,192,224};

        EchoClient client;

        EchoServer server = new EchoServer();
        Thread thread = new Thread(server);
        thread.start();

        client = new EchoClient();

        String echo = "";

        for (int i:DSCPVals) { for (int j = 0; j < 5; j++){
            System.out.println("Sending on tos: " + Integer.toString(i));
            client.setTos(i);
            echo = client.sendEcho(Integer.toString(i));
            System.out.println("Received: " + echo);
        }}


        client.sendEcho("end");
        client.close();

        if (!server.isRunning()){thread.join();}

    }
}