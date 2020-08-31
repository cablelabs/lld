package com.CableLabs.JavaUDPTosExample;

import java.io.IOException;


/*
Desktop class corresponding to ServerController.java in com.CableLabs.AndroidJavaUdpTosExample
 */

public class ServerTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        EchoServer server = new EchoServer();
        Thread thread = new Thread(server);
        thread.start();

        if (!server.isRunning()){thread.join();}

    }
}