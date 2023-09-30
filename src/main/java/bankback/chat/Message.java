package bankback.chat;

import java.io.Serializable;

public class Message implements Serializable {
    String message;
    int destinataire;
    int expediteur;
    String time;
    String date ;

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", destinataire=" + destinataire +
                ", expediteur=" + expediteur +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public Message(String message, int destinataire, int expediteur, String time, String date) {
        this.message = message;
        this.destinataire = destinataire;
        this.expediteur = expediteur;
        this.time = time;
        this.date = date;
    }
}
