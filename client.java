import java.net.*;
import java.net.*;
import java.io.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.awt.BorderLayout;

public class client extends JFrame {

    Socket socket;

    BufferedReader br; // For reading input
    PrintWriter out; // For writing data

    // For GUI
    private JLabel heading = new JLabel("Client");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    // Constructor for Client
    public client() {

        try {
            System.out.println("Sending request to server");

            socket = new Socket("192.168.29.189", 7777);

            System.out.println("Connection Done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            // startWriting();

        } catch (Exception e) {
        }
    }

    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener() { // Interface

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

                // System.out.println("Key released" + e.getKeyCode()); // For keycode of enter

                if (e.getKeyCode() == 10) {
                    // System.out.println("You have pressed enter");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me : " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText(""); // After sending msg, to set the field blank
                    messageInput.requestFocus(); // To set focus back to msg

                }

            }

        });
    }

    private void createGUI() {
        // GUI code
        this.setTitle("Client Message[END]");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null); // Basically sets the window to center
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Closes the prog on pressing close button

        // Component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        // Image icon
        // heading.setIcon(new ImageIcon("a2.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        // heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        // Message
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // Layout of our frame
        this.setLayout(new BorderLayout());// Devides frame into parts - 5 parts

        // adding components to frame
        this.add(heading, BorderLayout.NORTH);
        this.add(messageArea, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);

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
                        System.out.println("server termianted the chat");
                        JOptionPane.showMessageDialog(this, "server termianted the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("server:" + msg);

                    messageArea.append("server:" + msg + "\n");
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

        System.out.println("This is client side...");
        new client();
    }
}
