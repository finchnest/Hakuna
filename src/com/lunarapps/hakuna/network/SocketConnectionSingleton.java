package com.lunarapps.hakuna.network;

import java.io.*;
import java.net.Socket;

public class SocketConnectionSingleton {

    private static SocketConnectionSingleton socketConnectionSingleton = null;
    private Socket socket;

    public ObjectOutputStream objectOutputStream;
    public ObjectInputStream objectInputStream;
    public InputStreamReader inputStreamReader;
    public PrintWriter printWriter;

    private SocketConnectionSingleton() {
        /*
         * the above 4 streams are chain streams and they serialize and de-serialize objects upon streaming
         * they also have to be chained to connection streams--they take connection stream objects in their
         * constructor upon their creation--that take care of low-level details like writing bytes to a file.
         * We call low level methods on the connection stream and the high-level ones
         * like reading or writing objects in the chain stream*/
        try {
            socket = new Socket("127.0.0.1", 44444);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //a singleton connector because it's not necessary to create a new stream every time the
    //client wants to contact the server
    public static SocketConnectionSingleton getInstance() {
        if (socketConnectionSingleton == null) {
            socketConnectionSingleton = new SocketConnectionSingleton();
        }
        return socketConnectionSingleton;
    }


}
