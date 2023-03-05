package client.view;

import org.junit.jupiter.api.Test;
import service.UserClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QQViewLogin {
    private UserClientService userClientService = new UserClientService();//用于登录服务器和注册
    private JFrame frame;
    @Test
    public void show() {
        // 创建 JFrame 实例
        frame = new JFrame("qq Login");
        // Setting the width and height of frame
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        frame.setLocation((width-600)/2,(height-400)/2);

        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */
        JPanel panel = new JPanel();
        // 添加面板
        frame.add(panel);
        /*
         * 调用用户定义的方法并添加组件到面板
         */
        placeComponents(panel);

        // 设置界面可见
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);

        // 创建 JLabel
        JLabel userLabel = new JLabel("User:");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        userLabel.setBounds(150,80,120,40);
        panel.add(userLabel);

        /*
         * 创建文本域用于用户输入
         */
        JTextField userText = new JTextField(20);
        userText.setBounds(250,80,200,30);
        panel.add(userText);

        // 输入密码的文本域
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(150,120,120,40);
        panel.add(passwordLabel);

        /*
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         */
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(250,120,200,30);
        panel.add(passwordText);

        // 创建注册按钮
        JButton registerButton = new JButton("register");
        registerButton.setBounds(180,250,100,30);
        panel.add(registerButton);
        // 创建登录按钮
        JButton loginButton = new JButton("login");
        loginButton.setBounds(320,250,100,30);
        panel.add(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = String.valueOf(passwordText.getPassword());
                try {
                    boolean flag = userClientService.checkUser(username,password);
                    if(flag){
                        //关闭当前窗口并开启新窗口
                        frame.dispose();
                        new QQViewMain(userClientService).show();
                    }
                    else {
                        JOptionPane.showMessageDialog(panel,
                                "登录失败", "提示",JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = String.valueOf(passwordText.getPassword());
                try {
                    boolean flag = userClientService.registerUser(username,password);
                    if(flag){
                        JOptionPane.showMessageDialog(panel,
                                "注册成功", "提示",JOptionPane.PLAIN_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(panel,
                                "注册失败，用户存在", "提示",JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}

