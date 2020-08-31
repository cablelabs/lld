package com.CableLabs.AndroidJavaUdpTosExample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView outputView = (TextView) findViewById(R.id.editTextMultiLineOutput);
        final EditText addrView = (EditText) findViewById(R.id.editTextAddr);


        UDPLoop loop = new UDPLoop(outputView);
        Thread loopthread = new Thread(loop);

        ClientController clientController = new ClientController(outputView, addrView.getText().toString());
        Thread clientControllerThread = new Thread(clientController);

        ServerController serverController = new ServerController(outputView);
        Thread serverContorllerThread = new Thread(serverController);


        Button loopButton = findViewById(R.id.LoopButton);
        loopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UDPLoop loop = new UDPLoop(outputView);
                Thread loopthread = new Thread(loop);

                outputView.setText(outputView.getText() + "\ntestloop:");

                System.out.println("testLoop");

                loopthread.start();


            }
        });

        Button serverButton = findViewById(R.id.ServerButton);
        serverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("testServer");


                ServerController serverController = new ServerController(outputView);
                Thread serverContorllerThread = new Thread(serverController);
                serverContorllerThread.start();



            }
        });

        Button clientButton = findViewById(R.id.ClientButton);
        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("testClient");

                ClientController clientController = new ClientController(outputView, addrView.getText().toString());
                Thread clientControllerThread = new Thread(clientController);
                clientControllerThread.start();


            }
        });

        Button clearButton = findViewById(R.id.ClearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputView.setText("Output:");
            }
        });

        if (!loop.isRunning()){
            try {
                loopthread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        if (!clientController.isRunning()){

            try {
                clientControllerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        if (!serverController.isRunning()){
            try {
                serverContorllerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}