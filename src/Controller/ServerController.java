package Controller;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Exception.MethodNotWhitelistedException;

/**
 * Created by mgoo on 11/02/17.
 */
public class ServerController {
    private static ServerController instance = new ServerController();
    public static ServerController getInstance(){
        return instance;
    }

    private int port = 80;


    private ServerController(){

    }

    public void startServer(){
        boolean success = false;
        while(!success){
            try{
                ServerSocket serverSocket = new ServerSocket(this.port);
                success = true;
                new Listener(serverSocket).start();
            } catch (IOException e) {
                System.out.println("Port: "+this.port+" is busy");
                this.port++;
            }
        }
        System.out.println("The ServerController was started");
        System.out.println("Access the server on: localhost:"+this.port);
    }

    private class Listener extends Thread{
        private ServerSocket serverSocket;

        private Listener(ServerSocket serverSocket){
            this.serverSocket = serverSocket;
        }

        public void run(){
            while(true) {
                try {
                    Socket clientSocket = this.serverSocket.accept();
                    new Client(clientSocket, this.serverSocket).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Client extends Thread{
        private Socket clientSocket;
        private ServerSocket serverSocket;

        public Client(Socket clientSocket, ServerSocket serverSocket){
            this.clientSocket = clientSocket;
            this.serverSocket = serverSocket;
        }

        public void run(){
            System.out.println("A client Connected");
            try {
                OutputStream out = this.clientSocket.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
                String line = "";
                while((line = in.readLine()) != null){
//                    System.out.println(line);
                    if (line.contains("GET")){ // If it is the first line
                        String[] items = line.split(" ");
                        byte[] bytes = new byte[0];
                        try {
                            if (items[1].equals("/")){
                                items[1] = "/index.html";
                            }
                            items[1] = items[1].replaceAll("/", File.separator);
                            System.out.println("Asked for file: "+items[1]);
                            Path path = Paths.get("view" + items[1]);
                            bytes = Files.readAllBytes(path);
                        } catch (NoSuchFileException e){
                            System.out.println("The client asked for 'view"+items[1]+"' Which doesnt exist");
                            System.out.println("trying to run method");
                            bytes = this.runMethod(items[1]);
                        }
                        out.write(this.buildResponseHeaders(items[1], bytes).getBytes());
                        out.write(bytes);
                        out.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("A client Disconnected");
        }

        private byte[] runMethod(String data){
            Map<String, String> arguments = new HashMap<>();
            String[] parts = data.split("/");
            String classname = parts[1];
            String[] methodname_parts = parts[2].split("\\?");
            String methodname = methodname_parts[0];
            if (methodname_parts.length > 1){
                String arguments_string = methodname_parts[1];
                String[] arguments_pairs = arguments_string.split("&");
                for (String arguments_pair : arguments_pairs) {
                    String[] pair = arguments_pair.split("=");
                    arguments.put(pair[0], pair[1]);
                    System.out.println(pair[1]);
                }
            }
            byte[] bytes = new byte[0];
            try {
                if (!Whitelist.getInstance().isWhitelisted(classname, methodname) && !Whitelist.getInstance().isGreylisted(classname, methodname)){
                    throw new MethodNotWhitelistedException("Controller."+classname+methodname);
                }
                Class instance = Class.forName("Controller."+classname);
                Constructor constructor = instance.getConstructor();
                Object instanceOfClass = constructor.newInstance();
                Method method;
                try {
                    method = instance.getMethod(methodname);
                    bytes = ((String)method.invoke(instanceOfClass)).getBytes();
                } catch (NoSuchMethodException e3){
                    try {
                        method = instance.getMethod(methodname, Object.class);
                        bytes = ((String) method.invoke(instanceOfClass, arguments.values().toArray())).getBytes();
                    } catch (IllegalArgumentException | NoSuchMethodException e4){
                        method = instance.getMethod(methodname, Object[].class);
                        bytes = ((String) method.invoke(instanceOfClass, arguments.values().toArray())).getBytes();
                    }
                }

                System.out.println("Method: '"+methodname+"' of class: 'sample."+classname+"' ran successfully");
            } catch (InstantiationException |
                    InvocationTargetException |
                    NoSuchMethodException |
                    IllegalAccessException |
                    ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (MethodNotWhitelistedException e2){
                e2.printStackTrace();
            }
            return bytes;
        }

        private String buildResponseHeaders(String file, byte[] bytes){
            String response = "HTTP/1.1 200 OK\n" +
            "Date: Sat, 11 Feb 2017 01:30:21 GMT\n" +
            "ServerController: CustomJava\n" +
            "Last-Modified: Sat, 11 Feb 2017 14:35:20 GMT\n" +
            "Accept-Ranges: bytes\n" +
            "Content-Length: @@LENGTH@@\n" +
            "Cache-control: public, max-age=86400\n" +
            "Keep-Alive: timeout=30, max=99\n" +
            "Connection: keep-alive\n" +
            "Content-Type: @@TYPE@@\n\n";
            int responseSize = 0;
            responseSize = bytes.length;
            response = this.setContent(response, file);
            response = response.replace("@@LENGTH@@", ""+responseSize);
            return response;
        }

        private String setContent(String response, String filename){
            if (filename.contains(".html")){
                response = response.replace("@@TYPE@@", "text/html");
            } else if (filename.contains(".css")) {
                response = response.replace("@@TYPE@@", "text/css");
            } else if (filename.contains(".js")) {
                response = response.replace("@@TYPE@@", "text/javascript");
            } else if (filename.contains(".png")) {
                response = response.replace("@@TYPE@@", "image/png");
            } else if (filename.contains(".woff2")) {
                response = response.replace("@@TYPE@@", "font/woff2");
            } else if (filename.contains(".ico")) {
                response = response.replace("@@TYPE@@", "image/icon");
            } else {
                response = response.replace("@@TYPE@@", "text/html");
            }
            return response;
        }
    }
}
