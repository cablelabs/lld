package com.CableLabs.AndroidJavaUdpTosExample;

import android.widget.TextView;

import java.io.IOException;
import java.net.UnknownHostException;


/*
This is a driver class to send client traffic
it is triggered with the bottom button of the ui
the traffic output from this client should be of each assending DSCP

tested devices: pixel 4 xl
 */

public class ClientController implements Runnable {

    private volatile boolean running = true;
    private String addr;
    private static volatile String output = "";
    private TextView outputView;

    public ClientController(TextView outputView, String addr) {

        this.outputView = outputView;
        this.addr = addr;
    }

    public boolean isRunning(){return running;}

    public static String getOutput() {return output;}

    public void run() {

        int[] DSCPVals = new int[]{0,8,10,12,14,16,18,20,22,24,26,28,30,32,
            34,36,38,40,46,48,56};

        EchoClient client;


        try {
            client = new EchoClient(addr);


        String echo = "";

        // send 5 packets of a given DSCP value

        // Note we iterate through, and send, DSCP values in the payload
        // but we use that value << 2 to set the TOS value
        for (int i:DSCPVals) { 
            for (int j = 0; j < 5; j++){
                System.out.println("Sending: " + Integer.toString(i));
                output = output + "\nSending: " + Integer.toString(i);
                client.setTos(i<<2);
                echo = client.sendEcho(Integer.toString(i));
                System.out.println("Received: " + echo);
                output = output + "\nReceived: " + echo;
            }
        }


        try {
            client.sendEcho("end");
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();

        outputView.post(new Runnable() {
            @Override
            public void run() {
           outputView.setText(outputView.getText() + output);
            }
        });
        
        running = false;

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
