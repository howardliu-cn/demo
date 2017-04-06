package cn.howardliu.demo.nio;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * <br>created at 17-3-29
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Client {
    private Socket socket;
    private String ip;
    private int port;
    private String id;
    DataOutputStream dos;
    DataInputStream dis;

    public Client(String ip, int port, String id) {
        try {
            this.ip = ip;
            this.port = port;
            this.id = id;
            this.socket = new Socket(ip, port);
            this.socket.setKeepAlive(true);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            new Thread(new HeartThread()).start();
            new Thread(new MsgThread()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Object content) {
        try {
            System.out.println("send msg : " + content);
            byte[] bytes = objectToByte(content);
            int len = bytes.length;
            ByteBuffer dataLenBuf = ByteBuffer.allocate(4);
            dataLenBuf.order(ByteOrder.LITTLE_ENDIAN);
            dataLenBuf.putInt(0, len);
            dos.write(dataLenBuf.array(), 0, 4);
            dos.flush();
            dos.write(bytes);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket() {
        try {
            socket.close();
            dos.close();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] objectToByte(Object obj) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();
            bo.close();
            oo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }


    private Object byteToObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    class HeartThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    sendMsg("Client " + id + "," + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MsgThread implements Runnable {
        @Override
        public void run() {
            int temp;
            while (true) {
                try {
                    if (socket.getInputStream().available() > 0) {
                        byte[] bytes = new byte[1024];
                        int len = 0;
                        while ((char) (temp = dis.read()) != '\n') {
                            bytes[len] = (byte) temp;
                            len++;
                        }
                        System.out.println(byteToObject(bytes));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Client c1 = new Client("127.0.0.1", 12345, "1");
        c1.sendMsg("test");
    }
}
