package com.CableLabs.AndroidJavaUdpTosExample;

import android.widget.TextView;

import java.io.IOException;
import java.net.UnknownHostException;

/*
This is a driver class to loop traffic on the device locally
it is triggered with the top button of the ui
if this runs without errors or exceptions the android device supports DSCP

tested devices: pixel 4 xl
 */
public class UDPLoop implements Runnable {

    private volatile boolean running = true;
    private static volatile String output = "";
    private TextView outputView;

    public UDPLoop(TextView outputView) {
        this.outputView = outputView;
    }

    public boolean isRunning(){return running;}

    public static String getOutput() {return output;}

    public void run() {

        int[] DSCPVals = new int[]{0,8,10,12,14,16,18,20,22,24,26,28,30,32,
            34,36,38,40,46,48,56};


        EchoClient client;

        EchoServer server = null;
        try {
            server = new EchoServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(server);
        thread.start();

        try {
            client = new EchoClient();


        String echo = "";

	// Note we iterate through, and send, DSCP values in the payload
	// but we use that value << 2 to set the TOS value
        for (int i:DSCPVals) { 
            for (int j = 0; j < 5; j++){
                System.out.println("Sending: " + Integer.toString(i));
                output = output + "\nSending: " + Integer.toString(i);
                client.setTos(i<<2);
                echo = client.sendEcho(Integer.toString(i));
                System.out.println("Received: " + echo);
                output = output + server.getOutput() + "\nReceived: " + echo;
            }
        }


        try {
            client.sendEcho("end");
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();

        if (!server.isRunning()){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        outputView.setText(outputView.getText() + output);
        running = false;

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
