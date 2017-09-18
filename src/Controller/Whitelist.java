package Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mgoo on 11/02/17.
 */
public class Whitelist {
    private static Whitelist instance = new Whitelist();
    public static Whitelist getInstance(){
        return instance;
    }

    private Map<String, String[]> whitelist = new HashMap<>();
    private Map<String, String[]> greylist = new HashMap<>();

    private Whitelist(){
        this.whitelist.put("DockerController", new String[]{"exit", "getRunning", "getAllContainers", "getVersion", "getInfo", "getQuickVersion", "getImages"});
        this.greylist.put("DockerController", new String[]{"startContainer", "stopContainer"});
    }

    public boolean isWhitelisted(String classname, String methodname){
        String[] methodnames = this.whitelist.get(classname);
        return (methodnames != null && Arrays.asList(methodnames).contains(methodname));
    }

    public boolean isGreylisted(String classname, String methodname){
        String[] methodnames = this.greylist.get(classname);
        return (methodnames != null && Arrays.asList(methodnames).contains(methodname));
    }

    // TODO write code that will escape strings to be run on the command line

}
