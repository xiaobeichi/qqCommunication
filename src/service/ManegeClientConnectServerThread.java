package service;

import java.util.HashMap;

public class ManegeClientConnectServerThread {
    private static HashMap<String,ClientConnectServerThread> hashMap =
            new HashMap<>();

    public static void addThread(String username,ClientConnectServerThread clientConnectServerThread){
        hashMap.put(username,clientConnectServerThread);
    }
    //通过username得到线程
    public static ClientConnectServerThread getThread(String username){
        return hashMap.get(username);
    }
    public static void deleteThread(String username){
        hashMap.remove(username);
    }
}
