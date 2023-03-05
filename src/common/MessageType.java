package common;

import java.io.Serializable;

public class MessageType implements Serializable {
    public static final String MESSAGE_LOGIN_SUCCEED = "1"; //登录成功
    public static final String MESSAGE_LOGIN_FAIL = "2"; //登录失败
    public static final String MESSAGE_COMMON_MES = "3";//普通信息
    public static final String MESSAGE_GET_ONLINE_CLIENT = "4";//获取在线用户列表请求
    public static final String MESSAGE_RETURN_ONLINE_CLIENT = "5";//返回在线用户列表
    public static final String MESSAGE_CLIENT_EXIT = "6";//客户端请求退出
    public static final String MESSAGE_TO_ALL = "7";//群发消息
    public static final String REGISTER = "8";//请求注册
    public static final String LOGIN = "9";//请求登录
    public static final String MESSAGE_REGISTER_SUCCEED = "10"; //注册成功
    public static final String MESSAGE_REGISTER_FAIL = "11"; //注册失败
    public static final String MESSAGE_ADD_FRIEND_REQUEST = "12"; //请求添加好友
    public static final String MESSAGE_ADD_FRIEND_SUCCEED = "13"; //添加成功
    public static final String MESSAGE_ADD_FRIEND_FAIL = "14"; //请求失败
    public static final String MESSAGE_ADD_FRIEND_ACCEPT = "15"; //结束请求
    public static final String MESSAGE_ADD_FRIEND_REJECT = "16"; //拒绝添加
    public static final String MESSAGE_ADD_FRIEND_EXIST = "17"; //好友已添加
    public static final String MESSAGE_GET_REQUEST_LIST = "18"; //获取请求（好友、群组）列表
    public static final String MESSAGE_RETURN_REQUEST_LIST = "19"; //返回请求（好友、群组）列表
    public static final String MESSAGE_GET_FRIEND_LIST = "20"; //请求好友列表
    public static final String MESSAGE_RETURN_FRIEND_LIST = "21"; //返回好友列表
    public static final String MESSAGE_GET_CHAT_LIST = "22"; //请求消息列表
    public static final String MESSAGE_SEND_FILE = "23"; //开始发送文件的标志
}
