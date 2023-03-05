package service;

import common.Message;
import common.MessageType;
import org.junit.jupiter.api.Test;
import utils.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnectServerThread extends Thread{
    private Socket socket;
    private String username;
    public boolean isAlive = true;

    public ClientConnectServerThread(String username,Socket socket) {
        this.username = username;
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
    JFrame frame;
    @Override
    public void run() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(false);
        //因为一直要在后台和服务器通信，用while
        while(isAlive){
            System.out.println("======客户端等待服务器消息======");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果没有回阻塞
                Message msg = (Message) ois.readObject();
                //判断msg，根据类型做相应处理
                if(msg.getType().equals(MessageType.MESSAGE_RETURN_ONLINE_CLIENT)){//如果是回送用户列表
                    //取出信息并显示,假定形式为空格隔开字符串
                    String[] Friends = msg.getContent().split(" ");
                    System.out.println("========在线用户列表========");
                    for (int i = 0; i < Friends.length; i++) {
                        System.out.println("\t 用户："+Friends[i]);
                    }
                }else if(msg.getType().equals(MessageType.MESSAGE_COMMON_MES)){
                    //存入消息集合
                    System.out.println("收到一条消息");
                    ManageMessage.addMessage(msg);
                }else if(msg.getType().equals(MessageType.MESSAGE_ADD_FRIEND_SUCCEED)){
                    System.out.println("好友存在，发送请求");
                    JOptionPane.showMessageDialog(frame,
                        "发出添加请求，等待回应", "提示",JOptionPane.PLAIN_MESSAGE);
                }else if(msg.getType().equals(MessageType.MESSAGE_ADD_FRIEND_FAIL)){
                    System.out.println("查找好友不存在");
                    JOptionPane.showMessageDialog(frame,
                            "查找好友不存在，请重新输入用户名", "警告",JOptionPane.WARNING_MESSAGE);
                }else if(msg.getType().equals(MessageType.MESSAGE_ADD_FRIEND_EXIST)){
                    System.out.println("好友已存在");
                    JOptionPane.showMessageDialog(frame,
                            "好友已经添加过", "警告",JOptionPane.WARNING_MESSAGE);
                }else if(msg.getType().equals(MessageType.MESSAGE_RETURN_REQUEST_LIST)){
                    requestListView(msg);
                }else if(msg.getType().equals(MessageType.MESSAGE_RETURN_FRIEND_LIST)){
                    friendListView(msg);
                }else if(msg.getType().equals(MessageType.MESSAGE_SEND_FILE)){
                    System.out.println("收到文件："+msg.getContent());
                    String parentPath = "DownLoad/"+username;
                    String filePath = "DownLoad/"+username+"/"+msg.getContent();
                    File file = new File(parentPath);
                    if(!file.exists()) file.mkdirs();
                    File file1 = new File(filePath);
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1)));
                    while (true) {
                        // (读取)接收客户端传输过来的数据
                        String content = dis.readUTF();
                        // 如果数据内容为表示传输结束的内容,则停止接收
                        if ("nanjkvuiabjkbvja18631".equals(content)) {
                            break;
                        }
                        // 将接收到的数据写入到文件中
                        bw.write(content);
                        // 换行,因为客户端用的是readLine
                        bw.newLine();
                    }
                    System.out.println("接收完成");
                    bw.close();
//                    BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
//                    byte[] bytes = Utility.streamToByteArray(bis);
                    //输出
//                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
//                    bos.write(bytes);
//                    bos.close();
                    //bis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void requestListView(Message msg){
        //返回请求列表，显示界面
        String[] sender = msg.getContent().split(",");
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int w = 400;
        int h = 600;
        frame.setSize(w, h);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        frame.setLocation((width-w)/2,(height-h)/2);
        frame.setLayout(null);

        JPanel contentPane = new JPanel();
        contentPane.setBounds(0,0,300,100*sender.length);//100*sender.length
        JScrollPane scrollPane = new JScrollPane(contentPane,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.setLayout(null);
        frame.setContentPane(scrollPane);
        JPanel[] panels = new JPanel[sender.length];
        //添加申请者
        for (int i = 0; i < sender.length; i++) {
            panels[i] = new JPanel();
            panels[i].setBounds(0,50*i,350,50);
            panels[i].setLayout(null);
            JButton label = new JButton();
            String senderName = sender[i].substring(2);
            if(sender[i].charAt(0)=='f'){
                label.setText("申请好友:"+sender[i].substring(2));
            }else {
                label.setText("申请群:"+sender[i].substring(2));
            }
            label.setFont(new Font("宋体", 1, 14));
            panels[i].add(label);
            label.setBounds(0,5,160,40);
            JButton agree = new JButton("同意");
            agree.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ObjectOutputStream oos = null;
                    try {
                        Message m = new Message();
                        m.setTargetUsername(senderName);
                        m.setType(MessageType.MESSAGE_ADD_FRIEND_ACCEPT);
                        oos = new ObjectOutputStream
                                (socket.getOutputStream());
                        oos.writeObject(m);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            JButton disagree = new JButton("拒绝");
            disagree.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ObjectOutputStream oos = null;
                    try {
                        Message m = new Message();
                        m.setTargetUsername(senderName);
                        m.setType(MessageType.MESSAGE_ADD_FRIEND_REJECT);
                        oos = new ObjectOutputStream
                                (socket.getOutputStream());
                        oos.writeObject(m);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            panels[i].add(agree);
            agree.setBounds(170,5,80,40);
            panels[i].add(disagree);
            disagree.setBounds(260,5,80,40);
            frame.add(panels[i]);
        }
        frame.setVisible(true);
    }

    public void friendListView(Message msg){
        //返回请求列表，显示界面
        String[] friends=null;
        if(msg.getContent()!=null) {
            friends = msg.getContent().split(",");
        }
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int w = 250;
        int h = 500;
        frame.setSize(w, h);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        frame.setLocation((width-w)/2,(height-h)/2);
        frame.setLayout(null);

        JPanel contentPane = new JPanel();
        contentPane.setBounds(0,0,250,100*friends.length);//100*sender.length
        JScrollPane scrollPane = new JScrollPane(contentPane,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.setLayout(null);
        frame.setContentPane(scrollPane);

        for (int i = 0; i < friends.length; i++) {
            JPanel panel = new JPanel();
            panel.setBounds(0,60*i,220,60);
            panel.setLayout(null);
            JButton friend = new JButton();
            String friendName = friends[i];
            friend.setText(friends[i]);
            friend.setFont(new Font("宋体", 1, 16));
            panel.add(friend);
            friend.setBounds(20,5,180,50);
            friend.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //先发一个消息返回聊天记录，显示聊天记录并且每当记录数有变化时添加新消息
                    chatView(friendName);
                }
            });

            frame.add(panel);
            frame.setVisible(true);
        }
    }
    //聊天界面
    @Test
    public void chatView(String friend){
        //jframe里有一个splitpane，setpane为这个，splitepane上方是滑动的消息显示窗口，下面是输入窗口
        frame = new JFrame("好友："+friend);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int w = 400;
        int h = 600;
        frame.setSize(w, h);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        frame.setLocation((width-w)/2,(height-h)/2);
        frame.setLayout(null);

        JPanel scrollPanel = new JPanel();
        scrollPanel.setBounds(0,0,400,400);
        JScrollPane jScrollPane = new JScrollPane(scrollPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        //jScrollPane.setLayout(null);
        jScrollPane.setBounds(0,0,400,400);
        scrollPanel.setLayout(null);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        jPanel.setBounds(0,400,400,200);
        //从HashMap中读取消息显示，设一个按钮控制消息刷新

        JLabel[] jLabels = new JLabel[8];
        for (int i = 0; i < jLabels.length; i++) {
            jLabels[i] = new JLabel();
            jLabels[i].setBounds(10,50*i+5,300,40);
            //jLabels[i].setText("hello "+i);
            jLabels[i].setFont(new Font("宋体",1,16));
            scrollPanel.add(jLabels[i]);
        }
        createChatView(jLabels,friend);

        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,jScrollPane,jPanel);
        jSplitPane.setDividerLocation(400);

        JTextArea jTextArea = new JTextArea(8,20);
        jTextArea.setLineWrap(true);
        jPanel.add(jTextArea);
        jTextArea.setBounds(5,10,300,100);
        JButton send = new JButton("发送");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!jTextArea.getText().equals("")) {
                    //消息不为空时，发送message即可，顺便清空输入，理论上上面还要显示
                    ObjectOutputStream oos = null;
                    try {
                        System.out.println("向"+friend+"发送："+jTextArea.getText());
                        Message m = new Message();
                        m.setSourceUsername(username);
                        m.setTargetUsername(friend);
                        m.setType(MessageType.MESSAGE_COMMON_MES);
                        m.setContent(jTextArea.getText());
                        oos = new ObjectOutputStream
                                (socket.getOutputStream());
                        oos.writeObject(m);
                        //加入message集合，调用显示函数
                        ManageMessage.addMessage(m);
                        createChatView(jLabels,friend);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    jTextArea.setText(null);
                }
            }
        });
        send.setBounds(310,10,80,30);
        jPanel.add(send);

        JButton flush = new JButton("刷新");
        flush.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createChatView(jLabels,friend);
            }
        });
        flush.setBounds(310,45,80,30);
        jPanel.add(flush);

        JButton file = new JButton("发送文件");
        file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jf = new JFrame();
                jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                int w = 600;
                int h = 500;
                jf.setSize(w, h);
                Toolkit kit = Toolkit.getDefaultToolkit();
                Dimension screenSize = kit.getScreenSize();
                int width = screenSize.width;
                int height = screenSize.height;
                jf.setLocation((width-w)/2,(height-h)/2);
                jf.setLayout(null);

                JFileChooser jFileChooser = new JFileChooser();
                jf.add(jFileChooser);
                jFileChooser.setBounds(0,0,580,450);
                jFileChooser.setApproveButtonText("发送");
                jFileChooser.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int returnValue = jFileChooser.getDialogType();
                        if(returnValue==JFileChooser.APPROVE_OPTION) {
                            File file1 = jFileChooser.getSelectedFile();
                            //先发送一个开始标志，并发送文件名
                            Message startFile = new Message();
                            startFile.setTargetUsername(friend);
                            startFile.setType(MessageType.MESSAGE_SEND_FILE);
                            startFile.setContent(file1.getName());
                            System.out.println("开始发送文件："+file1.getName());
                            try {
                                ObjectOutputStream oos = new ObjectOutputStream
                                        (socket.getOutputStream());
                                oos.writeObject(startFile);
                                //再开始读进文件并发送
                                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
                                // 要传输的文件内容
                                String content = null;
                                // 每次读取文件中的一行内容,然后发送到服务器端
                                while ((content = br.readLine()) != null) {
                                    dos.writeUTF(content);
                                    // 清空缓存
                                    dos.flush();
                                }
                                // 额外输出一句非常规的话给服务器,表示文件传输完毕
                                dos.writeUTF("nanjkvuiabjkbvja18631");
                                dos.flush();

//                                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file1));
//                                byte[] bytes = Utility.streamToByteArray(bis);
//                                //输出流发送
//                                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
//                                bos.write(bytes);
//                                System.out.println("发送结束，传输结束标记");
//                                bos.write("?????".getBytes());
                                //socket.shutdownOutput();
                                //bis.close();
                                //bos.close();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            jf.dispose();
                        }else {
                            jf.dispose();
                        }
                    }
                });
                jf.setVisible(true);
            }
        });
        file.setBounds(305,80,90,30);
        jPanel.add(file);

        frame.setContentPane(jSplitPane);
        frame.setVisible(true);
    }
    public void createChatView(JLabel[] jLabels,String friend){
        //添加message到JPanel里
        ArrayList<Message> messages = ManageMessage.getMessage(friend);
        if(messages!=null) {
            if (messages.size() <= 8) {
                for (int i = 0; i < messages.size(); i++) {
                    jLabels[i].setText(messages.get(i).getSourceUsername() + " : "
                            + messages.get(i).getContent());
                }
            } else {
                for (int i = 0; i < jLabels.length; i++) {
                    jLabels[i].setText(messages.get(messages.size() - jLabels.length + i).getSourceUsername() + " : "
                            + messages.get(messages.size() - jLabels.length + i).getContent());
                }
            }
        }
    }
}
