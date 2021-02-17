import java.io.*;
import java.net.Socket;

public class Network implements Closeable {

    private final DataInputStream is;
    private final DataOutputStream os;
    private final Socket socket;
    public static Network instance;

    public static Network getInstance() throws IOException {
        if (instance==null){
            instance= new Network();
        }
        return instance;
    }
    private Network() throws IOException {
        socket= new Socket("localhost", 8189);
        os= new DataOutputStream(socket.getOutputStream());
        is= new DataInputStream(socket.getInputStream());
    }

    public void write(String message) throws IOException {
        os.writeUTF(message);
        os.flush();
    }

    public void getFile(String fileName, int size, CloudController controller) throws IOException {
        int ptr=0;
        int fileSize =size;
        byte[] buffer;
        File newFile = new File("Client/src/UserFiles/"+fileName);
        FileOutputStream fos = new FileOutputStream(newFile,false);
               buffer= new byte[8189];
               if (fileSize> buffer.length){
                while (fileSize>ptr){
                    ptr=is.read(buffer);
                    fos.write(buffer,0,ptr);
                    fileSize-=ptr;

                }}
                byte[] bufferLast = new byte[fileSize];
                    while(fileSize>0) {
                        ptr = is.read(bufferLast);
                        fos.write(bufferLast, 0, ptr);
                        fileSize-=ptr;
                       }
                controller.output.appendText("File successfully received from server.\n");
                fos.close();

    }

    public String read() throws IOException {
        return is.readUTF();
    }

    public void close() throws IOException {
        is.close();
        os.close();
        socket.close();
    }
}
