import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class PathExample {
    public static void main(String[] args) throws IOException {
        String p ="1.txt";
        Path path = Paths.get( "");
        System.out.println(path.getParent());
        System.out.println(path.toAbsolutePath().getParent());
        WatchService service = FileSystems.getDefault().newWatchService();
        new Thread(()->{
        while (true) {
            try{
            WatchKey key = service.take();
            List<WatchEvent<?>> events = key.pollEvents();
            if(key.isValid()){
            for (WatchEvent<?> event : events) {
                System.out.println(event.count() + " " + event.kind() + " " + event.context());
            }}
            key.reset();}
            catch (Exception e){
                e.printStackTrace();
            }

        }

            }).start();
        path.register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY,StandardWatchEventKinds.ENTRY_DELETE);

    }
}
