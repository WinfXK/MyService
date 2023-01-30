package cn.winfxk.myservice;

import cn.winfxk.myservice.tool.MyMap;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

public class Main extends Thread {
    private static final int Port = 19131;
    private final Socket socket;
    private final PrintStream output;
    private final BufferedReader input;
    public Host host;

    public static void main(String[] args) {
        try {
            //  Initialize.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("无法释放数据文件！请检查系统配置！！");
            System.exit(-1);
            return;
        }
        new Thread(() -> {
            try {
                ServerSocket socket = new ServerSocket(Port);
                while (true)
                    new Main(socket.accept()).start();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("遇到问题！");
            }
        }).start();
    }

    private Main(Socket socket) throws Exception {
        this.socket = socket;
        output = new PrintStream(this.socket.getOutputStream());
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            host = new Host(input);
            String appKey = host.appKey();
            if (appKey != null && !appKey.isEmpty()) {
                switch (appKey.toLowerCase(Locale.ROOT)) {
                    case "file":

                        break;
                }
            }
            System.out.println(host.appKey());
            System.out.println(host.getUseKey());
            System.out.println(host.getusePath(true));
            System.out.println();
            input.close();
            output.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendFile(File file, PrintStream output) {
        try {
            long lnegth = file.length();
            output.println("HTTP/1.0 200 OK");
            output.println("MIME_version:1.0");
            output.println("Content_Type:" + Content.getContentType(file));
            output.println("Content_Length:" + lnegth);
            output.println("Accept-Language: zh-CN,zh;q=0.9");
            output.println();
            byte[] b = new byte[(int) lnegth];
            DataInputStream input = new DataInputStream(new FileInputStream(file));
            input.readFully(b);
            output.write(b, 0, b.length);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
