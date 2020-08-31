package com.CableLabs.JavaUDPTosExample;

import java.io.IOException;
import java.util.Scanner;


/*
Desktop class corresponding to ClientController.java in com.CableLabs.AndroidJavaUdpTosExample
 */

public class ClientTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        int[] DSCPVals = new int[]{0,8,32,40,48,56,64,72,80,88,96,104,112,120,128,
            136,144,152,160,184,192,224};

        EchoClient client;

//      can be run with target ip as argument or will ask and read in from console
        try {
            client = new EchoClient(args[0]);
        }
         catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Server address not passed as argument.");
            System.out.println("Enter server Address");
            Scanner in = new Scanner(System.in);
            client = new EchoClient(in.nextLine());
        }

        String echo = "";

        for (int i:DSCPVals) {
            for (int j = 0; j < 5; j++){
                System.out.println("Sending on tos: " + Integer.toString(i));
                client.setTos(i);
                echo = client.sendEcho(Integer.toString(i));
                System.out.println("Received: " + echo);
            }
        }


        client.sendEcho("end");
        System.out.println("end");
        client.close();


    }
}