import java.io.*;
import java.net.URISyntaxException;

public class IoUtils {

    public static void main(String[] args) throws URISyntaxException, IOException {
        File f1= new File("drop.png");
        File copy = new File("data/2.png");
        System.out.println(f1.exists());
        System.out.println(copy.exists());
        InputStream is = new FileInputStream(f1);
        OutputStream os = new FileOutputStream(copy, true);
        int ptr=0;
        byte[] buffer = new byte[8192];
        while ((ptr=is.read(buffer))!=-1){
            os.write(buffer,0,ptr);
        }
        os.close();
        is.close();
    }
}
