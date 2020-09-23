package com.CableLabs.JavaUDPTosExample;

import java.io.IOException;
import java.util.Scanner;


/*
Desktop class corresponding to ClientController.java in com.CableLabs.AndroidJavaUdpTosExample
 */

public class ClientTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        int[] DSCPVals = new int[]{0,8,10,12,14,16,18,20,22,24,26,28,30,32,
            34,36,38,40,46,48,56};

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
        System.out.println("end");
        client.close();


    }
}
