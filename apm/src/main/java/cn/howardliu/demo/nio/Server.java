package cn.howardliu.demo.nio;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <br>created at 17-3-29
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Server {
    private Map<String, Long> hearTimeMap = new HashMap<>();

    public static void main(String[] args) {
        new Server(55555);
    }

    public Server(int port) {
        Selector selector = null;
        ServerSocketChannel serverChannel = null;
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port));
            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (selector.select() > 0) {
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey readyKey = it.next();
                    it.remove();
                    if (readyKey.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) readyKey.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    } else if (readyKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) readyKey.channel();
                        Object obj = receiveData(socketChannel);
                        String msg = "Server back: ";
                        if (obj instanceof String) {
                            String id = obj.toString().split(",")[0];
                            if (hearTimeMap.get(id) != null
                                    && System.currentTimeMillis() - hearTimeMap.get(id) > 5000) {
                                socketChannel.socket().close();
                            } else {
                                hearTimeMap.put(id, System.currentTimeMillis());
                            }
                            long time = System.currentTimeMillis();
                            msg += time + "\n";
                            sendData(socketChannel, msg);
                        } else if (obj instanceof Pojo) {
                            msg += ((Pojo) obj).getName() + "\n";
                            sendData(socketChannel, msg);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (selector != null) {
                    selector.close();
                }
                if (serverChannel != null) {
                    serverChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Object receiveData(SocketChannel socketChannel) {
        Object obj = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteBuffer intBuffer = ByteBuffer.allocate(4);
        ByteBuffer objBuffer = ByteBuffer.allocate(1024);
        int size = 0;
        int sum = 0;
        int objLen = 0;
        byte[] bytes = null;
        try {
            while ((size = socketChannel.read(intBuffer)) > 0) {
                intBuffer.flip();
                bytes = new byte[size];
                intBuffer.get(bytes);
                baos.write(bytes);
                intBuffer.clear();
                if (bytes.length == 4) {
                    objLen = bytesToInt(bytes, 0);
                }
                if (objLen > 0) {
                    byte[] objByte = new byte[0];
                    while (sum != objLen) {
                        size = socketChannel.read(objBuffer);
                        if (size > 0) {
                            objBuffer.flip();
                            bytes = new byte[size];
                            objBuffer.get(bytes, 0, size);
                            baos.write(bytes);
                            objBuffer.clear();
                            objByte = ArrayUtils.addAll(objByte, bytes);
                            sum += bytes.length;
                        }
                    }
                    obj = byteToObject(objByte);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    private void sendData(SocketChannel socketChannel, Object obj) {
        byte[] bytes = objectToByte(obj);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int bytesToInt(byte[] ary, int offset) {
        return (ary[offset] & 0xFF)
                | (ary[offset + 1] & 0xFF00)
                | (ary[offset + 2] & 0xFF0000)
                | (ary[offset + 3] & 0xFF000000);
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

    private byte[] objectToByte(Object obj) {
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
}
