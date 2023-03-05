import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class Try {
    public static void main(String[] args) {
        byte[] bytes = "?".getBytes();
        System.out.println("?".equals(new String(bytes,0,1)));
    }
}
