package service;

import common.Message;
import common.MessageType;
import common.User;
import common.UserType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Timer;

/**
 * @author 刘梓池
 * @version 1.0
 * 完成用户登录验证和注册等功能
 */
public class UserClientService {
    //socket user其他地方都使用，所有做属性
    private User user = new User();
    private Socket socket;

    public User getUser() {
        return user;
    }

    public Socket getSocket() {
        return socket;
    }
//为了以后扩展，将线程放入集合

    //验证用户是否存在
    public boolean checkUser(String username,String password) throws Exception {
        user.setUsername(username);
        user.setPassword(password);
        user.setType(UserType.LOGIN);
        //连接到服务器,先连本地,服务器9999监听
        socket = new Socket(InetAddress.getByName("127.0.0.1"),9999);
        //socket得到输出流构建对象输出流
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(user);
        //socket.shutdownOutput();
        //获得回复
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("获得回复");
        Message msg = (Message)ois.readObject();
        //判断是否成功
        if(msg.getType().equals( MessageType.MESSAGE_LOGIN_SUCCEED)){
            //启动线程持有socket，使之保持通讯,同时初始化消息列表
            ClientConnectServerThread connect = new ClientConnectServerThread(username,socket);
            connect.start();
            //放入线程集合
            ManegeClientConnectServerThread.addThread(username,connect);
            //消息列表
            ManageMessage.setUsername(username);

            //发送请求消息列表：文件请求一并发回，显示时特殊显示即可
            ObjectOutputStream oos1 = new ObjectOutputStream(socket.getOutputStream());
            Message message = new Message();
            message.setType(MessageType.MESSAGE_GET_CHAT_LIST);
            oos1.writeObject(message);
            return true;
        }else {
            socket.close();
            return false;
        }
    }

    //注册用户
    public boolean registerUser(String username,String password) throws Exception {
        User user1 = new User();
        user1.setUsername(username);
        user1.setPassword(password);
        user1.setType(UserType.REGISTER);
        //连接到服务器,先连本地,服务器9999监听
        Socket socket1 = new Socket(InetAddress.getByName("127.0.0.1"),9999);
        //socket得到输出流构建对象输出流
        ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());
        oos.writeObject(user1);
        //socket.shutdownOutput();
        //获得回复
        ObjectInputStream ois = new ObjectInputStream(socket1.getInputStream());
        System.out.println("获得回复");
        Message msg = (Message)ois.readObject();
        //判断是否成功
        if(msg.getType().equals( MessageType.MESSAGE_REGISTER_SUCCEED)){
            System.out.println("注册成功");
            socket1.close();
            return true;
        }else {
            System.out.println("注册失败");
            socket1.close();
            return false;
        }
    }

    //向服务端请求在线用户列表
    public void onlineFriendList(){
        //发送messge请求
        Message message = new Message();
        message.setSourceUsername(user.getUsername());
        message.setType(MessageType.MESSAGE_GET_ONLINE_CLIENT);
        //发送服务器
        //得到当前线程对应socket的输出流
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManegeClientConnectServerThread.getThread
                            (user.getUsername()).getSocket().getOutputStream());
            oos.writeObject(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //退出系统
    public void exit(){
        //发送messge请求
        Message message = new Message();
        message.setSourceUsername(user.getUsername());
        message.setType(MessageType.MESSAGE_CLIENT_EXIT);
        //发送服务器
        //得到当前线程对应socket的输出流
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManegeClientConnectServerThread.getThread
                            (user.getUsername()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.exit(0);
            //读取在线程中
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //添加好友
    public void addFriend(String Dname){
        Message message = new Message();
        message.setTargetUsername(Dname);
        System.out.println("请求添加"+Dname);
        message.setType(MessageType.MESSAGE_ADD_FRIEND_REQUEST);
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManegeClientConnectServerThread.getThread
                            (user.getUsername()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //获取请求列表
    public void getRequestList(){
        Message message = new Message();
        message.setSourceUsername(user.getUsername());
        System.out.println("获取请求列表");
        message.setType(MessageType.MESSAGE_GET_REQUEST_LIST);
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManegeClientConnectServerThread.getThread
                            (user.getUsername()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //获取好友列表
    public void getFriendList(){
        Message message = new Message();
        message.setSourceUsername(user.getUsername());
        System.out.println("获取好友列表");
        message.setType(MessageType.MESSAGE_GET_FRIEND_LIST);
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManegeClientConnectServerThread.getThread
                            (user.getUsername()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
