package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTestGround {
    public static void main(String[] args) throws IOException {
        try (ServerSocket sender = new ServerSocket(4444);
        ServerSocket listener = new ServerSocket(4445)) {

        }

    }

    class senderThread implements Runnable {

        Socket senderSocket;

        senderThread(Socket socket) {
            senderSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader bfr = new BufferedReader(new InputStreamReader(senderSocket.getInputStream()));
                 PrintWriter pw = new PrintWriter(senderSocket.getOutputStream())) {
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
