package Controller;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DockerController {
    private Runtime rt;

    public DockerController(){
        this.rt = Runtime.getRuntime();
    }

    public void exit(){
        Platform.exit();
    }

    public String getRunning(){
        return this.tablefy(this.getCommandOutput("docker ps"));
    }

    public String getAllContainers(){
        return this.tablefy(this.getCommandOutput("docker ps -a"));
    }

    public String getVersion(){
        return this.getCommandOutput("docker version");
    }

    public String getInfo() {
        return this.getCommandOutput("docker info");
    }

    public String getQuickVersion(){
        return this.getCommandOutput("docker -v");
    }

    public String getImages(){
        return this.tablefy(this.getCommandOutput("docker images"));
    }

    public String startContainer(Object args){
        return this.getCommandOutput("docker start "+args);
    }

    public String stopContainer(Object args){
        return this.getCommandOutput("docker stop "+args);
    }

    private String getCommandOutput(String command){
        try {
            Process p = this.rt.exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String result = "";
            String line = "";
            while ((line = reader.readLine())!= null) {
                result += line+"<br>";
            }
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String tablefy(String notformatted){
        notformatted = notformatted.replaceAll("\\s{2,}", "</td><td>");
        notformatted = notformatted.replaceAll("(<br>)", "</td></tr><tr><td>");
        notformatted = "<table><tr><td>" + notformatted.substring(0, notformatted.length()-6) + "</td></tr></table>";
        return notformatted;
    }

}
