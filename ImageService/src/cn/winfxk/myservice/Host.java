package cn.winfxk.myservice;

import cn.winfxk.myservice.tool.MyMap;
import cn.winfxk.myservice.tool.Tool;

import java.io.BufferedReader;
import java.util.Map;

public class Host {
    public String Host;
    public String Path;
    public int Port = -1;
    private String useKey = null, appKey = null;
    private final Map<String, Object> map = new MyMap<>();

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

    public String getusePath() {
        return getusePath(false);
    }

    public String getusePath(boolean isSystem) {
        System.out.println(Path.indexOf(appKey()));
        if (isSystem)
            return Path.substring(Path.indexOf(appKey()) + appKey.length() + 1);
        String s = appKey() + "/" + getUseKey();
        return Path.substring(Path.indexOf(s) + s.length() + 1);
    }

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
