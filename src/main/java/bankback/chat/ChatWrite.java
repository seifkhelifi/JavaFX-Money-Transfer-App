package bankback.chat;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ChatWrite extends Thread {

    ChatRemote r;

    public ChatWrite(ChatRemote r){
        this.r = r;
    }

    public void run() {
        // code to be executed in this thread
        while(true){
            try {
                ArrayList<Message> mes = r.getMessageById(29477481,12345678);

                for (Message me : mes) {
                    System.out.println(me);
                }

            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

}