package common;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 刘梓池
 * @version 1.0
 * 用于通信的信息包
 */
public class Message implements Serializable {
    //增强串行化兼容性
    @Serial
    private static final long serialVersionUID = 1L;

    private String sourceUsername;
    private  String targetUsername;
    private String content;
    private String sendTime;//发送时间

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type; //消息类型

    public String getSourceUsername() {
        return sourceUsername;
    }

    public void setSourceUsername(String sourceUsername) {
        this.sourceUsername = sourceUsername;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
