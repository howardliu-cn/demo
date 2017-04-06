package cn.howardliu.demo.heart;

/**
 * <br>created at 17-3-30
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Main {
    public static void main(String[] args) throws Exception {
        HeartBeatClient client = new HeartBeatClient("127.0.0.1", 12345);
        client.start();
        client.sendData();
    }
}
