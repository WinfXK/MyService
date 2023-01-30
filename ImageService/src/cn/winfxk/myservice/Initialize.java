package cn.winfxk.myservice;

import cn.winfxk.myservice.tool.Config;
import cn.winfxk.myservice.tool.MyMap;
import cn.winfxk.myservice.tool.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Initialize {
    private static final String[] DirName = {"MyService"};
    public static File JarFile, WorkingPath, PluginFile, FilePath, ImageFile, errorFile;
    private static final MyMap<String, MyMap<String, BasePlugin>> Plugin = new MyMap<>();

    protected static void start() throws IOException {
        String path = Initialize.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = java.net.URLDecoder.decode(path, "utf-8");
        Initialize.JarFile = new File(path);
        Initialize.WorkingPath = JarFile.getParentFile();
        Initialize.FilePath = new File(Initialize.WorkingPath, "System/File");
        Initialize.ImageFile = new File(Initialize.WorkingPath, "System/Image");
        Initialize.errorFile = new File(Initialize.WorkingPath, "System/error");
        JarFile jarFile = new JarFile(path);
        Enumeration<JarEntry> jarEntrys = jarFile.entries();
        JarEntry jarEntry;
        String JarEntryName;
        File file;
        System.out.println("释放数据中...");
        while (jarEntrys.hasMoreElements()) {
            jarEntry = jarEntrys.nextElement();
            JarEntryName = jarEntry.getName();
            if (!isNeed(JarEntryName)) continue;
            file = new File(WorkingPath, JarEntryName);
            if (jarEntry.isDirectory()) {
                if (!file.exists()) file.mkdirs();
                continue;
            }
            try {
                write(JarEntryName, file);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("无法释放数据：" + JarEntryName);
            }
        }
        System.out.println("数据释放完成");
        PluginFile = new File(WorkingPath, "Plugins");
        File[] files = PluginFile.listFiles((dir, name) -> {
            File file1 = new File(dir, name);
            if (!file1.isFile()) return false;
            String s = file1.getName().substring(file1.getName().lastIndexOf("."));
            if (s.isEmpty()) return false;
            return s.equals("jar") || s.equals(".jar");
        });
        if (files != null) {
            URL[] Jars = new URL[files.length];
            java.util.jar.JarFile pluginJarFile;
            JarEntry pluginJarEntry;
            List<MyMap<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < Jars.length; i++) {
                Jars[i] = new URL("file:" + files[i].getAbsolutePath());
                pluginJarFile = new JarFile(files[i]);
                pluginJarEntry = pluginJarFile.getJarEntry("plugin.yml");
                list.add(Config.yaml.loadAs(Utils.readFile(pluginJarFile.getInputStream(pluginJarEntry)), MyMap.class));
            }
            URLClassLoader classLoader = new URLClassLoader(Jars);
            Class<?> pluginClass;
            BasePlugin plugin;
            Map<String, Object> bases;
            String host, appKey;
            for (MyMap<String, Object> map : list) {
                try {
                    pluginClass = classLoader.loadClass(map.getString("main"));
                    plugin = (BasePlugin) pluginClass.newInstance();
                    plugin.onEnabling();
                    host = map.getString("host");
                    if (host == null || host.isEmpty()) host = "All";
                    appKey = map.getString("appKey");
                    if (appKey == null || appKey.isEmpty()) throw new RuntimeException("无法加载未定义appKey的Jar包！");
                    if (!Plugin.containsKey(host))
                        Plugin.put(host, new MyMap<>());
                    bases = Plugin.getMap(host);
                    bases.put(appKey, plugin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void write(String JarFile, File file) throws Exception {
        FileOutputStream stream = new FileOutputStream(file);
        InputStream inputStream = Initialize.class.getResourceAsStream("/" + JarFile);
        byte[] b = new byte[1024];
        while (inputStream.read(b) != -1)
            stream.write(b);
        stream.close();
        inputStream.close();
    }

    private static boolean isNeed(String Name) {
        for (String s : DirName)
            if (Name != null && Name.startsWith(s)) return true;
        return false;
    }
}
