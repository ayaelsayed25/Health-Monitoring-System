package UDP;

import MessagesHandler.MessageHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class AServer
{
    final static int BUFFER_SIZE = 65535;
    final static int PORT = 8080;

    public static void main(String[] args) throws IOException
    {
        DatagramSocket ds = new DatagramSocket(PORT);
        byte[] receive = new byte[BUFFER_SIZE];
        System.out.println("Im waiting");
        DatagramPacket DpReceive;
        MessageHandler msg = new MessageHandler();
        while(true)
        {
            DpReceive = new DatagramPacket(receive, receive.length);
            ds.receive(DpReceive);
            System.out.println("Client:-" + data(receive).toString());
            msg.saveMessage(data(receive).toString());
            receive = new byte[BUFFER_SIZE];
        }
    }
  
    // A utility method to convert the byte array
    // data into a string representation.
    public static StringBuilder data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
}
