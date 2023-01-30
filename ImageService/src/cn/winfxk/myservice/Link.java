package cn.winfxk.myservice;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;

public class Link {
    private final Socket socket;
    private final PrintStream output;
    private final BufferedReader input;
    private final Host host;
    protected Link(Socket socket, PrintStream output, BufferedReader input, Host host) {
        this.socket = socket;
        this.output = output;
        this.input = input;
        this.host = host;
    }

    public Host getHost() {
        return host;
    }

    public BufferedReader getInput() {
        return input;
    }

    public PrintStream getOutput() {
        return output;
    }

    public Socket getSocket() {
        return socket;
    }
}
