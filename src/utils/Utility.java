package utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Scanner;

public class Utility {
    private  static Scanner scanner = new Scanner(System.in);
    public static char rendMenuSelection(){
        char c;
        for(;;){
            String str = readKeyboard(1,false);
            c = str.charAt(0);
            if(c!='1'&&c!='2'&&c!='3'&&c!='4'&&c!='5'){
                System.out.println("选择错误，请重新输入");
            }else break;
        }
        return c;
    }
    public  static char readChar(){
        String str = readKeyboard(1,false);
        return str.charAt(0);
    }
    public static char readChar(char defaultValue){
        String str = readKeyboard(1,true);
        return (str.length()==0)?defaultValue : str.charAt(0);
    }
    public static int readInt(){
        int n;
        while (true){
            String str = readKeyboard(2,false);
            try {
                n = Integer.parseInt(str);
                break;
            }catch (NumberFormatException e){
                System.out.println("数字输入错误，请输入一个整数");
            }
        }
        return n;
    }
    public static int randInt(int defaultValue){
        int n;
        while (true){
            String str = readKeyboard(2,false);
            if(str.equals("")){
                return defaultValue;
            }
            try {
                n = Integer.parseInt(str);
                break;
            }catch (NumberFormatException e){
                System.out.println("数字输入错误，请输入一个整数");
            }
        }
        return n;
    }
    public static String readString(int length){
        return readKeyboard(length,false);
    }
    public static  String readString(int length,String defaultValue){
        String str = readKeyboard(length,true);
        return  str.equals("")?defaultValue : str;
    }
    public static char readConfirmSelection(){
        System.out.println("请输入你的选择(Y/N)");
        char c;
        while (true){
            //接收字符转大写
            String str = readKeyboard(1,false).toUpperCase();
            c = str.charAt(0);
            if(c=='Y'||c=='N'){
                break;
            }else {
                System.out.println("输入错误，请重新输入");
            }
        }
        return c;
    }
    private static String readKeyboard(int length, boolean ifBlankReturn){
        String line = "";
        //判断有没有下一行
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            //如果长度为零为直接回车
            if(line.length()==0){
                if(ifBlankReturn) return line;
                else continue;
            }
            //如果长度大于length重新输入
            //如果大于零小于等于length接收
            if(line.length()<1||line.length()>length){
                System.out.println("输入长度（不能大于"+length+"）错误，请重新输入");
                continue;
            }
            break;
        }
        return line;
    }
    //文件输入转byte数组
    public static byte[] streamToByteArray(InputStream is)throws Exception{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();//输出流对象
        byte[] b = new byte[1024];
        int len;
        len = is.read(b);
        while (len!=-1){
            if(!"?????".equals(new String(b,0,len))) {
                bos.write(b, 0, len);
                len = is.read(b);
            }
            else break;
        }
        byte[] array = bos.toByteArray();
        bos.close();
        return array;
    }

}
