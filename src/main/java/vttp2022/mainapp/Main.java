package vttp2022.mainapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 *
 */
public class Main 
{

    public static void main( String[] args )
    {
        int defaultPort = 3000;
        String defaultdocRoot = "./static";
        boolean stop = false;
        for(int i=0; i<args.length;i++){
            System.out.println(args[i]);
            if (args[i].equals("--port")){
                defaultPort = Integer.parseInt(args[i+1]);
            }
            if(args[i].equals("--docRoot")){
                defaultdocRoot = args[i+1];
            }
        }
        System.out.printf("Requesting for port %s and docRoot(s) %s.\n",defaultPort, defaultdocRoot);
        HttpServer server = new HttpServer(defaultPort, defaultdocRoot);
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try{
            ServerSocket serverSocket = new ServerSocket(defaultPort);
            while(!stop){
                System.out.println("Waiting for connections...");
                Socket socket = serverSocket.accept();
                HttpClientConnection clientHandlingThread = new HttpClientConnection(server.getDocRootList(), socket);
                threadPool.submit(clientHandlingThread);    
            }
            //serverSocket.close();
        }catch (IOException e){
            System.out.println("IOexception met in main app.");
            System.exit(1);
        }
    }
}
