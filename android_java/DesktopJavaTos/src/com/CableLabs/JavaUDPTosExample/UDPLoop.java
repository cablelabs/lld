package com.CableLabs.JavaUDPTosExample;


import java.io.IOException;

/*
Desktop class corresponding to UDPLoop.java in com.CableLabs.AndroidJavaUdpTosExample
 */

public class UDPLoop {

    public static void main(String[] args) throws IOException, InterruptedException {

        int[] DSCPVals = new int[]{0,8,10,12,14,16,18,20,22,24,26,28,30,32,
            34,36,38,40,46,48,56};

        EchoClient client;

        EchoServer server = new EchoServer();
        Thread thread = new Thread(server);
        thread.start();

        client = new EchoClient();

        String echo = "";

	// Note we iterate through, and send, DSCP values in the payload
	// but we use that value << 2 to set the TOS value
        for (int i:DSCPVals) { 
	    for (int j = 0; j < 5; j++){
                System.out.println("Sending on DSCP: " + Integer.toString(i));
                client.setTos(i<<2);
                echo = client.sendEcho(Integer.toString(i));
                System.out.println("Received: " + echo);
            }
	}


        client.sendEcho("end");
        client.close();

        if (!server.isRunning()){thread.join();}

    }
}
