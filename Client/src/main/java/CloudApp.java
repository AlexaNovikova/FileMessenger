import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class CloudApp extends Application {

    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("MyStage.fxml"));
        primaryStage.setScene(new Scene(parent));
        primaryStage.setTitle("Cloud");
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(r-> {
            try{
                Network.getInstance().write("/quit");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}
