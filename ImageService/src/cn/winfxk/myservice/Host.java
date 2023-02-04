package cn.winfxk.myservice;

import cn.winfxk.myservice.tool.MyMap;
import cn.winfxk.myservice.tool.Tool;

import java.io.BufferedReader;
import java.util.Map;

public class Host {
    public String Host;
    public String Path = null;
    public int Port = -1;
    private String useKey = null, appKey = null;
    private final Map<String, Object> map = new MyMap<>();

    public static void main(String[] args) {
        try {
            System.out.println(Tool.getHttp("http://winfxk.cn/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Host(BufferedReader input) throws Exception {
        Path = input.readLine();
        if (Path == null || Path.isEmpty()) return;
        Path = Path.substring(Path.indexOf(" ") + 1, Path.lastIndexOf(" "));
        Host = input.readLine();
        if (Host == null || Host.isEmpty()) return;
        Host = Host.substring(6);
        if (Host.contains(":")) {
            String[] strings = Host.split(":");
            Host = strings[0];
            if (strings.length > 1)
                Port = Tool.ObjToInt(strings[1]);
        }
        if (Port == -1) Port = 80;
        if (Path.contains("?")) {
            String[] strings = Path.split("\\?");
            if (strings.length > 1) {
                String[] Data = strings[1].split("&");
                for (String s : Data)
                    map.put(s.substring(0, s.indexOf("=")), s.substring(s.indexOf("=") + 1));
            }
        }
    }

    /**
     * 返回连接附带的路径<这个路径在连接中位置在APPKey后方><br>
     * 如连接地址为http://localhost:19131/error/404/index.html ，则返回/index.html
     *
     * @return
     */
    public String getusePath() {
        return getusePath(false);
    }

    /**
     * 返回连接附带的路径<这个路径在连接中位置在APPKey后方><br>
     * 如连接地址为http://localhost:19131/error/404/index.html ，则返回/index.html
     * 当参数为true时返回/404/index.html
     *
     * @param isSystem 是否是系统应用，如果是将会忽略APPKey
     * @return
     */
    public String getusePath(boolean isSystem) {
        String string = appKey() + (isSystem ? "" : "/" + getUseKey());
        String Path = this.Path.substring(this.Path.indexOf(string) + string.length());
        if (Path.contains("?"))
            Path = Path.split("\\?")[0];
        return Path;
    }

    /**
     * 返回连接附带的数据
     *
     * @return
     */
    public Map<String, Object> getData() {
        return map;
    }

    @Override
    public String toString() {
        return "Host=" + Host + ", Path=" + Path + ", Port=" + Port;
    }

    public String appKey() {
        if (appKey == null) {
            String[] strings = Path.split("/");
            for (String a : strings) {
                if (a == null || a.isEmpty() || a.equals("/")) continue;
                appKey = a;
                break;
            }
        }
        return appKey;
    }

    public String getUseKey() {
        if (useKey == null) {
            boolean ok = false;
            String[] strings = Path.split("/");
            for (String a : strings) {
                if (a == null || a.isEmpty() || a.equals("/")) continue;
                if (!ok) {
                    ok = true;
                    continue;
                }
                useKey = a;
                break;
            }
        }
        return useKey;
    }
}
