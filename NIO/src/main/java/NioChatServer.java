import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NioChatServer implements Runnable {
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private final ByteBuffer buffer = ByteBuffer.allocate(256);
    private int acceptedClientIndex = 1;

    public NioChatServer() throws IOException {

        this.serverChannel = ServerSocketChannel.open();
        selector = Selector.open();
        serverChannel.bind(new InetSocketAddress(8189));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel)key.channel();
        StringBuilder sb = new StringBuilder();
        int read = 0;
        while ((read = channel.read(buffer)) > 0) {
            buffer.flip();
            byte[] bytes = new byte[buffer.limit()];
            buffer.get(bytes);
            sb.append(new String(bytes));

            buffer.clear();
            String msg;
                if (sb.toString().replaceAll("[\r\n ]", "").startsWith("/private")) {
                    msg = key.attachment() + ": " + sb.toString().split(" ", 3)[2] + "\n";
                    String name = sb.toString().split(" ", 3)[1];
                    System.out.println(msg);
                    broadcastPrivateMessage(msg, name);
                } else {
                    msg = key.attachment() + ": " + sb.toString();
                    System.out.println(msg);
                    broadcastMessage(msg);
                }
        }
                if (read < 0) {
                  String    msg = key.attachment() + " покинул чат\n";
                    channel.close();
                    System.out.println(msg);
                    broadcastMessage(msg);

        }

    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel)key.channel()).accept();
        String clientName = "Клиент #" + acceptedClientIndex;
        acceptedClientIndex++;
       // welcomeBuf.rewind();
        System.out.println("Подключился новый клиент " + clientName);
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ, clientName);
        buffer.put("Добро пожаловать в чат!\n".getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        socketChannel.write(buffer);
        buffer.flip();
    }

    private void broadcastMessage(String msg) throws IOException {
        ByteBuffer msgBuf = ByteBuffer.wrap(msg.getBytes());
        for (SelectionKey key : selector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                SocketChannel sch = (SocketChannel) key.channel();
                sch.write(msgBuf);
                msgBuf.rewind();
            }
        }
    }

    // для отправки личных сообщений необходимо указать /private и номер клиента (/private 1 )
    private void broadcastPrivateMessage(String msg, String number) throws IOException {
        ByteBuffer msgBuf = ByteBuffer.wrap(msg.getBytes());
        for (SelectionKey key : selector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel && key.attachment().toString().equals("Клиент #"+number)) {
                SocketChannel sch = (SocketChannel) key.channel();
                sch.write(msgBuf);
                msgBuf.rewind();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new NioChatServer()).start();
    }

    @Override
    public void run() {
        try {
            System.out.println("Сервер запущен (Порт: 8189)");
            Iterator<SelectionKey> iter;
            SelectionKey key;
            while (this.serverChannel.isOpen()) {
                selector.select();
                iter = this.selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    key = iter.next();
                    iter.remove();
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }}
}
