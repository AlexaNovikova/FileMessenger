import java.io.*;
import java.net.Socket;

public class Handler implements Runnable{

    private String serverDir = "Server/src/ServerFiles";
    private String username;

    private final IoFileCommandServer server;
    private final Socket socket;

    public Handler(IoFileCommandServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        username="user";
    }

    @Override
    public void run() {
   try(DataOutputStream os= new DataOutputStream(socket.getOutputStream());
       DataInputStream is = new DataInputStream(socket.getInputStream())){
       while (true){
        String message = is.readUTF();
           System.out.println("Received message: "+ message);
        if (message.equals("ls")){
            File dir = new File(serverDir);
            StringBuilder sb = new StringBuilder(username).append("files ->  \n");
            File[] files = dir.listFiles();
            if (files!=null){
                for (File file:files){
                    sb.append(file.getName()).append(" ");
                    if (file.isFile()){
                        sb.append("[FILE} | ").append(file.length()).append(" bytes.\n");
                    }
                    else {
                        sb.append("[DIR]\n");
                    }
                }
            }
            os.writeUTF(sb.toString());
            os.flush();
        }
        else if(message.startsWith("cd ")){
            String path = message.split(" ", 2)[1];
            File dir = new File(path);
            File dirAcc = new File (serverDir+"/"+path);
//            if (dir.exists()){
//                serverDir=path;
//            }
           if (dirAcc.exists()){
                serverDir=serverDir+"/"+path;
            }
            else if(path.equals("..")){
                serverDir=new File(serverDir).getParent();
            }
            else {
                os.writeUTF(" user: wrong path\n");
                os.flush();
            }

        }
        else if (message.startsWith("get ")){
            String fileName = message.split(" ", 2)[1];
            File fileFromServer= new File("Server/src/ServerFiles/"+fileName);
            //File fileToClient = new File("data/2.png");
//            System.out.println(f1.exists());
//            System.out.println(copy.exists());
            if(fileFromServer.exists()){
                os.writeUTF("get "+fileName+ " "+ fileFromServer.length()+ " \n");
                InputStream fis = new FileInputStream(fileFromServer);
                int ptr=0;
                byte[] buffer = new byte[8192];
                while ((ptr=fis.read(buffer))!=-1){
                    os.write(buffer,0,ptr);
                    os.flush();
                }
              fis.close();
            }
            else {
                os.writeUTF("File doesn't exit.\n");
            }

        }
        else if(message.equals("/quit")){
            os.writeUTF(message);
            os.flush();
            break;
        }
        else{
            os.writeUTF("UNKNOWN COMMAND\n");
            os.flush();
        }
       }

   }
   catch (IOException ioException){
       ioException.printStackTrace();
   }
   finally {
       {
           try{
               socket.close();
           }
           catch (Exception e){
               e.printStackTrace();
           }
       }
   }
    }
}
