package vttp2022.mainapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HttpClientConnection implements Runnable{
    public Socket socket;
    public List<String> docRootList = new LinkedList<>();
    public String docRootOfResource;
    public String resourceToSend;
    public InputStream is;
    public InputStreamReader isr;
    public BufferedReader br;
    public OutputStream os;

    public HttpClientConnection(List<String> docRootListInput, Socket socketInput){
        socket = socketInput;
        docRootList = docRootListInput;
    } 

    @Override public void run(){
        try{
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            os = socket.getOutputStream();
            String req = br.readLine();
            System.out.println(req);
            String[] reqList = req.split(" ");
            String method = reqList[0];
            String resource = reqList[1];
            String response = "";
            boolean resourceExist = false;
            System.out.println(method);
            System.out.println(resource);
            if (!method.equals("GET")){
                response = "HTTP/1.1 405 Method Not Allowed\r\n\r\n"+method+" not supported\r\n";
                os.write(resource.getBytes());
                os.flush();
                os.close();
            }else{
                if (resource.equals("/")){
                    resource = "/index.html";
                }
                resource = resource.replace("/", "");
                System.out.println("resource is "+resource);
                for (int i=0; i<docRootList.size();i++){
                    resourceExist =false;
                    System.out.println("asd");
                    File docRootF = new File(docRootList.get(i));
                    List<String> resourceList = Arrays.asList(docRootF.list());
                    for(int j=0;j<resourceList.size();j++){
                        System.out.println(resourceList.get(j));
                    }
                    if(resourceList.contains(resource)){
                        System.out.println(resource);
                        resourceExist = true;
                        docRootOfResource = docRootList.get(i)+"/"+resource;
                        System.out.println("found!");
                        break;
                    }
                }
                if(!resourceExist){
                    response = "HTTP/1.1 404 Not Found\r\n\r\n"+resource+" not found\r\n";
                    os.write(response.getBytes());
                    os.flush();
                    os.close();
                }else{
                    response = "HTTP/1.1 200 OK\r\n\r\n";
                    if (resource.contains(".png")){
                        response = "HTTP/1.1 200 OK\r\n Content-Type: image/png\r\n\r\n";
                        FileInputStream image = new FileInputStream(docRootOfResource);
                        os.write(response.getBytes());
                        os.write(image.readAllBytes());
                        os.flush();
                        os.close();
                        image.close();
                    }else{
                        System.out.println(docRootOfResource);
                        StringBuilder strbuild = new StringBuilder();
                        String holder;
                        FileReader fr = new FileReader(docRootOfResource);
                        System.out.println("asdas");
                        BufferedReader brF = new BufferedReader(fr);
                        while((holder = brF.readLine())!=null){
                            strbuild.append(holder);
                        }
                        System.out.println("123");
                        resourceToSend = strbuild.toString();
                        os.write(response.getBytes());
                        os.write(resourceToSend.getBytes());
                        os.flush();
                        os.close();
                        brF.close();
                        fr.close();
                        System.out.println("456");
                        socket.close();

                    }

                }
            }
        }catch(Exception Err){
            System.out.println("error met");
            Err.printStackTrace();
            System.exit(1);
        }
    }
}
