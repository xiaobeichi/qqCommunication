package common;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 刘梓池
 * @version 1.0
 * 用于通信的user信息包
 */
public class User implements Serializable {
    //增强串行化兼容性
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
