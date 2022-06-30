package vttp2022.mainapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkIO {
    public InputStream is;
    public InputStreamReader isr;
    public BufferedReader br;
    public OutputStream os;
    public Socket socket;

    public NetworkIO(Socket sock)throws IOException{
        socket = sock;
        is = socket.getInputStream();
        os = socket.getOutputStream();
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
    }

    public String read() throws IOException{
        return br.readLine();
    }

    public void write(byte[] byteArr) throws IOException{
        os.write(byteArr);
        //os.close();// cannot close here
    }

    public void flushie() throws IOException {
        os.flush();
    }

    public void close() throws IOException{
        isr.close();
        br.close();
        os.close();
        is.close();
    }
}
