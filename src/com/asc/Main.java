/*
 *   Abduhu S. Chauhdury
 *
 *   NIST Time Synchronization Application
 *
 *   Utilizes the NIST DAYTIME Protocol to retrieve the current Internet Time and converts to
 *   EST and updates the system time. Windows only.
 */

package com.asc;

import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        String hostname = "time.nist.gov";
        int port = 13;
        boolean dst = true;
        String time;

        System.out.println();

        try(Socket socket = new Socket(hostname, port)) {
            InputStreamReader input = new InputStreamReader(socket.getInputStream());

            int charID;
            StringBuilder data = new StringBuilder();
            String output;

            while ((charID = input.read()) != -1) {
                data.append((char)(charID));
            }

            output = String.valueOf(data);

            String hourStrUTC = output.substring(16, 18);
            String minsec = output.substring(19, 24);

            int hour = Integer.parseInt(hourStrUTC);

            if(dst)
                hour -= 4;
            else
                hour -= 5;

            if(hour < 0)
                hour = 24 + hour;

            String hourStrEST;

            if(hour < 12)
                hourStrEST = ("0" + hour);
            else
                hourStrEST = ("" + hour);

            time = ("" + hourStrEST + ":" + minsec);

            Runtime.getRuntime().exec("cmd /C time " + time);

            System.out.println("Set system time to: " + time);
        }

        catch(UnknownHostException ex) {
            System.out.println("Invalid or down server: " + ex.getMessage());
        }

        catch(IOException ex) {
            System.out.println("Input/output exception: " + ex.getMessage());
        }
    }
}
