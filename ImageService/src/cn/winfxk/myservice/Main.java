package cn.winfxk.myservice;

import sun.java2d.pipe.OutlineTextRenderer;

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
            Initialize.start();
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
                String hostPath = host.getusePath(true);
                File file;
                switch (appKey.toLowerCase(Locale.ROOT)) {
                    case "file":
                        file = new File(Initialize.FilePath, hostPath);

                        break;
                    case "error":
                    default:
                        String errorKey = host.getUseKey();
                        if (errorKey == null) errorKey = "404";
                        switch (errorKey.toLowerCase(Locale.ROOT)) {
                            case "404":
                            default:
                                file = new File(Initialize.errorFile, host.getusePath(true));
                                if (!file.isFile())
                                    file = new File(file, "index.html");
                                sendFile(file, output);
                                break;
                        }
                        break;
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendFile(File file, PrintStream output) {
        sendFile(file, output, 200);
    }

    public static void sendFile(File file, PrintStream output, int sendID) {
        try {
            long lnegth = file.length();
            output.println("HTTP/1.1 " + sendID + " OK");
            output.println("MIME_version: 1.0");
            output.println("Content-Type: " + Content.getContentType(file));
            output.println("Content_Length: " + lnegth);
            output.println("Cache-Control: must-revalidate");
            output.println("Cache-Control: no-cache");
            output.println("Cache-Control: no-store");
            output.println("Access-Control-Allow-Credentials: true");
            output.println("Access-Control-Allow-Methods: GET");
            output.println("Access-Control-Max-Age: 360");
            output.println("Content-Language: en-US");
            output.println("Access-Control-Allow-Headers: Content-Type");
            output.println("Access-Control-Allow-Headers: API-Authorization");
            output.println("Access-Control-Allow-Headers: ADMIN-Authorization");
            output.println("Allow: GET");
            output.println("");
            FileInputStream input = new FileInputStream(file);
            DataInputStream stream = new DataInputStream(input);
            for (long i = 0; i < stream.available(); i++)
                output.write(stream.read());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
