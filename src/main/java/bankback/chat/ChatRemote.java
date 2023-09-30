package bankback.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ChatRemote extends Remote {
    void createNewMessage(Message m) throws RemoteException; // gere les exeption du réseau
    ArrayList<Message> getMessageById(int sender, int receiver) throws RemoteException;
}
