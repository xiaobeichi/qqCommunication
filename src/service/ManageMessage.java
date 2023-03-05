package service;

import common.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageMessage {
    //存放所有消息
    private static HashMap<String, ArrayList<Message>> messageHashmap = new HashMap<>();
    private static String username;

    public static void setUsername(String name){
        username = name;
    }

    public static void addMessage(Message message){
        String friend;
        if(!message.getSourceUsername().equals(username))friend=message.getSourceUsername();
        else friend=message.getTargetUsername();
        if(messageHashmap.containsKey(friend)){
            ArrayList<Message> lms = messageHashmap.get(friend);
            lms.add(message);
            messageHashmap.put(friend,lms);
        }else {
            ArrayList<Message> messages = new ArrayList<>();
            messages.add(message);
            messageHashmap.put(friend,messages);
        }
    }
    //返回消息列表
    public static ArrayList<Message> getMessage(String friend){
        return messageHashmap.get(friend);
    }
}
