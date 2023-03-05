package client.view;

import common.Message;
import common.MessageType;
import org.junit.jupiter.api.Test;
import service.ClientConnectServerThread;
import service.ManegeClientConnectServerThread;
import service.UserClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;

public class QQViewMain {
    private UserClientService userClientService;
    private JFrame frame;
    public QQViewMain(UserClientService userClientService) {
        this.userClientService = userClientService;
    }
    @Test
    public void show(){
        // 创建 JFrame 实例
        frame = new JFrame("qq "+userClientService.getUser().getUsername());
        // Setting the width and height of frame
        int w = 300;
        int h = 550;
        frame.setSize(w, h);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//自定义
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //需要通知服务器，关闭socket并从集合中删除
                userClientService.exit();
            }
        });
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        frame.setLocation((width-w)/2,(height-h)/2);
        frame.setResizable(false);
        frame.setLayout(null);

        JPanel head = new JPanel();
        head.setLayout(null);
        head.setBackground(Color.cyan);
//        ImageIcon img = new ImageIcon("src/photo/default.png");
//        img.setImage(img.getImage().getScaledInstance(80,80,Image.SCALE_DEFAULT));
        JLabel jb = new JLabel();
        jb.setText(userClientService.getUser().getUsername());
        jb.setFont(new Font("宋体", 1, 20));
        head.add(jb);
        jb.setBounds(0,0,50,80);
        frame.add(head);
        head.setBounds(0,0,300,80);
        JButton jb1 = new JButton("申请列表");
        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userClientService.getRequestList();
            }
        });
        head.add(jb1);
        jb1.setBounds(180,20,100,40);
        //第二栏：添加好友，加入群，建群
        JPanel two = new JPanel();
        two.setLayout(null);
        two.setBackground(Color.orange);
        frame.add(two);
        two.setBounds(0,80,300,60);
        JButton jb2 = new JButton("添加好友");
        two.add(jb2);
        jb2.setBounds(5,15,90,30);
        jb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFriendView();
            }
        });
        JButton jb3 = new JButton("加入群聊");
        two.add(jb3);
        jb3.setBounds(100,15,90,30);
        JButton jb4 = new JButton("新建群聊");
        two.add(jb4);
        jb4.setBounds(195,15,90,30);
        //两个菜单控制群聊还是好友
        JLabel Menu = new JLabel();
        frame.add(Menu);
        Menu.setBounds(0,140,300,360);
        JButton Menu1 = new JButton();
        Menu1.setBounds(0,0,300,180);
        Menu1.setText("好友列表");
        Menu1.setSelected(true);
        Menu1.setFont(new Font("宋体", 1, 24));
        Menu.add(Menu1);
        Menu1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userClientService.getFriendList();
            }
        });
        JButton Menu2 = new JButton();
        Menu2.setBounds(0,180,300,180);
        Menu2.setText("群聊列表");
        Menu2.setFont(new Font("宋体", 1, 24));
        Menu.add(Menu2);
        frame.setVisible(true);
    }

    public void addFriendView(){
        // 创建 JFrame 实例
        frame = new JFrame("qq "+userClientService.getUser().getUsername());
        // Setting the width and height of frame
        int w = 400;
        int h = 300;
        frame.setSize(w, h);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        frame.setLocation((width-w)/2,(height-h)/2);
        frame.setLayout(null);

        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setBounds(0,0,400,300);
        panel.setLayout(null);
        JLabel label = new JLabel();
        label.setText("请输入想添加好友用户名:");
        label.setFont(new Font("宋体", 1, 16));
        label.setBounds(100,40,300,40);
        panel.add(label);

        JTextField userText = new JTextField(20);
        userText.setBounds(120,100,160,25);
        panel.add(userText);

        JButton addButton = new JButton("添加好友");
        label.setFont(new Font("宋体", 1, 16));
        addButton.setBounds(150,160,100,30);
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userClientService.addFriend(userText.getText());
            }
        });

        frame.setVisible(true);
    }

}
