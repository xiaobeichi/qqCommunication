package client.view;

import common.Message;
import common.MessageType;
import jdk.swing.interop.SwingInterOpUtils;
import service.ManegeClientConnectServerThread;
import service.MessageClientService;
import service.UserClientService;
import utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author 刘梓池
 * @version 1.0
 * 客户端菜单界面，之后做UI界面
 */
public class QQView {
    private boolean loop = true;//控制是否显示主菜单
    private String key = "";//用于接收用户输入
    private UserClientService userClientService = new UserClientService();//用于登录服务器和注册

    public static void main(String[] args) throws Exception {
        new QQView().mainMenu();
        System.out.println("客户端退出系统");
    }
    //显示主菜单
    private void mainMenu() throws Exception {
        while(loop){
            System.out.println("===========请登录网络通讯系统===========");
            System.out.println("\t\t 1 登录");
            System.out.println("\t\t 2 注册");
            System.out.println("\t\t 3 退出系统");
            System.out.println("请输入");
            key = Utility.readString(1);
            switch (key){
                case "1":
                    //System.out.println("请登录");
                    System.out.println("请输入用户名：");
                    String username = Utility.readString(50);//用户名不太长
                    System.out.println("请输入密码：");
                    String password = Utility.readString(50);
                    //这里应该查询数据库，这里先默认合法
                    boolean flag = userClientService.checkUser(username,password);
                    if(flag){
                        System.out.println("========="+username+"登录成功=========");
                        //二级菜单
                        loop = AfterLoginMenu(username);
                    }else {
                        System.out.println("=========用户不存在？密码错误？==========");
                    }
                    break;
                case "2":
                    System.out.println("请注册");
                    break;
                case "3":
                    System.out.println("退出系统");
                    loop = false;
                    break;
            }
        }
    }
    //登录菜单略
    //注册菜单略
    //登录成功菜单，后期换UI
    private boolean AfterLoginMenu(String username){
        System.out.println("============欢迎（用户"+username+"）============");
        boolean loop=true;
        while (loop){
            System.out.println("\t\t 1 显示在线用户列表");
            System.out.println("\t\t 2 群发消息");
            System.out.println("\t\t 3 私聊消息");
            System.out.println("\t\t 4 发送文件");
            System.out.println("\t\t 5 退出系统");
            System.out.println("请输入选择");
            key = Utility.readString(1);
            switch (key){
                case "1":
                    userClientService.onlineFriendList();
                    //System.out.println("显示在线用户列表");//新函数
                    break;
                case "2":
                    System.out.println("群发消息");
                    break;
                case "3":
                    System.out.print("请输入想聊天的在线用户名：");
                    String targetName = Utility.readString(50);
                    System.out.print("请输入想说的画（100字符以内）：");
                    String content = Utility.readString(100);
                    //再写一个service类
                    MessageClientService.sendMessageToOne(content,username,targetName);
                    //System.out.println("私聊消息");
                    break;
                case "4":
                    System.out.println("发送文件");
                    break;
                case "5":
                    //需要通知服务器，关闭socket并从集合中删除
                    Message message = new Message();
                    message.setSourceUsername(username);
                    message.setType(MessageType.MESSAGE_CLIENT_EXIT);
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream
                                (ManegeClientConnectServerThread.getThread
                                        (username).getSocket().getOutputStream());
                        oos.writeObject(message);
                        //关闭socket，删除集合，退出线程
                        ManegeClientConnectServerThread.getThread(username).getSocket().close();
                        ManegeClientConnectServerThread.getThread(username).isAlive=false;
                        ManegeClientConnectServerThread.deleteThread(username);
                        System.out.println(username+"退出");
                        System.exit(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    loop = false;
            }
        }
        return loop;
    }
}
