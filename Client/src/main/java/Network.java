import java.io.*;
import java.net.Socket;

public class Network implements Closeable {

    private static final int BUFFER_SIZE = 8189;
    private static final String CLIENT_PATH = "Client/src/UserFiles/";
    private final DataInputStream is;
    private final DataOutputStream os;
    private final Socket socket;
    public static Network instance;
    private static byte[] buffer;

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
        buffer= new byte[BUFFER_SIZE];
    }

    public void write(String message) throws IOException {
        os.writeUTF(message);
        os.flush();
    }

    public void getFile(String fileName, long size, CloudController controller) throws IOException {
        int ptr=0;
        long fileSize =size;
        File newFile = new File(CLIENT_PATH+fileName);
      try(  FileOutputStream fos = new FileOutputStream(newFile,false)) {
          if (fileSize > buffer.length) {
              while (fileSize > ptr) {
                  ptr = is.read(buffer);
                  fos.write(buffer, 0, ptr);
                  fileSize -= ptr;

              }
          }
          byte[] bufferLast = new byte[(int) fileSize];
          while (fileSize > 0) {
              ptr = is.read(bufferLast);
              fos.write(bufferLast, 0, ptr);
              fileSize -= ptr;
          }
          controller.output.appendText("File successfully received from server.\n");
      }

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
