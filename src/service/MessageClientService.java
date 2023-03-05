package service;

import common.Message;
import common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @author 刘梓池
 * @version 1.0
 * 提供消息相关的服务
 */
public class MessageClientService {
    public static void sendMessageToOne(String content, String sourceName, String targetName){
        Message message = new Message();
        message.setType(MessageType.MESSAGE_COMMON_MES);
        message.setContent(content);
        message.setSourceUsername(sourceName);
        message.setTargetUsername(targetName);
        message.setSendTime(new Date().toString());
        //如果有这个人
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManegeClientConnectServerThread.getThread
                            (sourceName).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
