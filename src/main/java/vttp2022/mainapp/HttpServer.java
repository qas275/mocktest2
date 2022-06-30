package vttp2022.mainapp;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class HttpServer {
    public int port;
    public List<String> docRootList = new LinkedList<>();

    public List<String> getDocRootList() {
        return docRootList;
    }

    public void setDocRootList(List<String> docRootList) {
        this.docRootList = docRootList;
    }

    public HttpServer(int portInput, String docRootInput){
        port = portInput;
        String[] docRootSplit = docRootInput.split(":");
        for (int i=0; i<docRootSplit.length;i++){
            docRootList.add(docRootSplit[i]);
        }
        start();
    }

    public void start(){
        for(int i=0; i<docRootList.size();i++){
            String docRootS = docRootList.get(i);
            File docRoot = new File(docRootS);
            if(!docRoot.exists()){
                System.out.printf("docRoot %s does not exist.\n",docRootS);
                System.exit(1);
            }
            if(!docRoot.isDirectory()){
                System.out.printf("docRoot %s is not a directory.\n",docRootS);
                System.exit(1);
            }
            if(!docRoot.canRead()){
                System.out.printf("docRoot %s cannot be read.\n",docRootS);
                System.exit(1);
            }
        }
    }

}
