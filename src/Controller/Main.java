package Controller;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.File;
import java.net.URL;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Docker Controller");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(500);
        Scene scene = new Scene(new Group());
        VBox root = new VBox();

        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();

//        URL url = getClass().getResource("../index.html");
        URL url = getClass().getResource(File.separator+"view"+File.separator+"index.html");

        webEngine.load(url.toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("DockerController", new DockerController());
            }
        });

        root.getChildren().add(browser);
        scene.setRoot(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("server")){
            ServerController.getInstance().startServer();
        } else if (args.length > 0 && args[0].equals("app")){
            launch(args);
        } else {
            ServerController.getInstance().startServer();
            launch(args);
        }
    }
}
