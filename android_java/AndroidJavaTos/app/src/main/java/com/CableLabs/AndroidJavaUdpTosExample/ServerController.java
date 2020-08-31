package com.CableLabs.AndroidJavaUdpTosExample;

import android.widget.TextView;

import java.io.IOException;


/*
This is a driver class to run an echo server which reflects client traffic
it is triggered with the middle button of the ui

tested devices: pixel 4 xl
 */

public class ServerController implements Runnable {

    private volatile boolean running = true;
    private static volatile String fullOuput = "";
    private TextView outputView;

    public ServerController(TextView outputView) {
        this.outputView = outputView;
    }


    public String getFullOuput() {
        return fullOuput;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        EchoServer server = null;
        try {
            server = new EchoServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread serverThread = new Thread(server);
        serverThread.start();

        while (server.isRunning()){
            if (server.isNewOuput()){
                 fullOuput = fullOuput + server.getOutput();

            }
        }

        if (!server.isRunning()){

            outputView.post(new Runnable() {
                @Override
                public void run() {
                    outputView.setText(outputView.getText() + fullOuput);
                }
            });

            try {
                serverThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(fullOuput);
        running = false;
    }
}
