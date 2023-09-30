package bankback.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static final int PORT = 5000;
    private static String name;

    public  void  startChat() throws Exception {
        System.out.println("Welcome to the chat room!");
        System.out.print("Please enter your name: ");
        Scanner scanner = new Scanner(System.in);
        name = scanner.nextLine();

        Socket socket = new Socket("localhost", PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println(name);

        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        String message = in.readLine();
                        if (message == null) {
                            System.out.println("Disconnected from the chat room.");
                            System.exit(0);
                        }
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    String message = scanner.nextLine();
                    if (message.equalsIgnoreCase("/exit")) {
                        System.out.println("Leaving the chat room...");
                        break;
                    }
                    out.println(message);
                }
            }
        }).start();



        socket.close();
    }
}
