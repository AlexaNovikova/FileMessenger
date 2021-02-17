import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CloudController implements Initializable {


    public TextField input;
    public TextArea output;
    private Network network;

    public void send(ActionEvent actionEvent) throws IOException {
        String in = input.getText();
        input.clear();
        network.write(in);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            network= Network.getInstance();
            new Thread(()->{
                try{
                while (true) {

                    String message = network.read();
                    Platform.runLater(()->output.appendText(message));
                    if (message.startsWith("get ")){
                       String fileName = message.split(" ", 4)[1];
                       int fileSize = Integer.parseInt(message.split(" ", 4)[2]);
                        network.getFile(fileName, fileSize, this);
                    }
                    if (message.equals("/quit")){
                        network.close();
                        break;
                    }
                }}
                catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
