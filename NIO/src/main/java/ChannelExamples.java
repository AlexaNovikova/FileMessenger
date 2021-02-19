import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelExamples {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer=ByteBuffer.allocate(5);
        buffer.put((byte)'a');
        buffer.put((byte)'b');
        buffer.put((byte)'c');
        buffer.flip();
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());
        buffer.rewind();
        buffer.clear();
        System.out.println((char)buffer.get());
        RandomAccessFile file = new RandomAccessFile("1.txt", "rw");
        FileChannel channel = file.getChannel();
        int read = 0;
        while ((read= channel.read(buffer) )!=-1){
            buffer.flip();
            while(buffer.hasRemaining()){
                System.out.print((char)buffer.get());
            }
            buffer.clear();
        }

    }
}
