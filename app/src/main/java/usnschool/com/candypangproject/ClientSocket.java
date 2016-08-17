package usnschool.com.candypangproject;

import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by it on 2016-08-01.
 */
public class ClientSocket {
    private Socket socket;
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;

    public ClientSocket() {
        new Thread(){
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.0.24", 7777);
                    Log.e("socket","접속되었습니다.");
                    ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    Log.e("server로부터",""+ois.readUTF());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();

        SystemClock.sleep(1000);

        new Thread(){
            @Override
            public void run() {
                try {
                    oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    oos.writeUTF("클라이언트로부터 보내는 메세지입니다.");
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public static ObjectInputStream getObjectInputStream(){
        return ois;
    }
    public static ObjectOutputStream getObjectOutputStream(){
        return oos;
    }
}
