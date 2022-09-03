import java.net.*;

import javax.swing.JOptionPane;

import java.io.*;

class Server {

    ServerSocket server;
    Socket socket;

    BufferedReader br; // For reading input
    PrintWriter out; // For writing data

    public Server() {
        // Specify port
        try {
            server = new ServerSocket(7777);

            System.out.println("Server is ready to accept connection");

            System.out.println("Waiting...");

            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startReading() {
        // thread will read data

        Runnable r1 = () -> {

            System.out.println("Reader started...");
            try {
                while (true) {
                    String msg;

                    msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client termianted the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client:" + msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        };

        new Thread(r1).start();

    }

    public void startWriting() {
        // thread will take data and send it user
        Runnable r2 = () -> {
            System.out.println("Writer Started...");

            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        };

        new Thread(r2).start();

    }

    public static void main(String args[]) {
        System.out.println("This is going to start server...");
        // for calling constructor
        new Server();
    }
}