package 界面;

import 界面.PCBA文件.JiMi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class success extends JFrame {
    private static JFrame jf;
    private static JButton bt_success;
    private static JButton bt_filed;
    private static JLabel jl;
    private static JLabel jLabel;
    public success(){
        Font font =new Font("黑体", Font.PLAIN, 20);//设置字体
        jf=new JFrame();
        jf.setVisible(true);
        jf.setSize(300,150);

        jLabel=new JLabel();
        jl=new JLabel("保存成功");
        jl.setBounds(100, 10, 120, 50);
        jl.setFont(font);

        bt_success=new JButton("确定");
        jf.setLayout(null);
        bt_success.setBounds(50,70,60,30);
        bt_filed=new JButton("退出");
        bt_filed.setBounds(180,70,60,30);

        //jLabel.add(jl);
        jf.add(bt_success);
        jf.add(bt_filed);
       jf.add(jl);
        Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
        Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
        int screenWidth = screenSize.width/2; // 获取屏幕的宽
        int screenHeight = screenSize.height/2; // 获取屏幕的高
        jf.setLocation(screenWidth-100/2, screenHeight-50/2);
    }
    public static void main(String[] args){
        success success=new success();
        bt_success.addActionListener(new ActionListener() {
                                       @Override
                                       public void actionPerformed(ActionEvent arg0) {
                                           success.dispose();
                                       }
                                   }
        );
    }
}
