import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;

public class FilesExample {
    public static void main(String[] args) throws IOException {
        StandardCopyOption co;
        StandardOpenOption oo;
        StandardCharsets ch;
        // CREATE_NEW исключение при уэе существующум файле
     //   Files.write(Paths.get("3.txt"), "Hello, World!".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE_NEW);

        Files.copy(Paths.get("3.txt"), Paths.get("1.txt"), StandardCopyOption.REPLACE_EXISTING);

//        Files.walkFileTree(Paths.get("./"), new HashSet<>(), 2,
//        new SimpleFileVisitor<Path>(){
//                    @Override
//                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                        System.out.println(file);
//                        return super.visitFile(file, attrs);
//                    }
//                });


    }
}
