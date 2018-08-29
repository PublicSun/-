package 界面;

import 数据库管理软件.App;
import 界面.PCBA文件.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class mainLayout extends JFrame {
    private static JFrame jf;

    private static JLabel jl;

    private static JButton bt_open;

    private static JButton bt_close;

    private static JButton bt_sysInfo;

    private static JButton bt_back;

    private static JButton bt_4;
    private static JButton bt_5;
    private static JButton bt_6;
    public mainLayout(){

        jf=new JFrame();
        jf.setVisible(true);
        jf.setSize(800,600);
        bt_open=new JButton("PCBA QA实验管理台");
        jf.setLayout(null);
        bt_open.setBounds(100,59,200,50);
        bt_close=new JButton("极米批次抽检");
        bt_close.setBounds(400,59,200,50);
        bt_sysInfo=new JButton("纽力达批次抽检");
        bt_sysInfo.setBounds(100,159,200,50);
        bt_back=new JButton("ODM抽查明细");
        bt_back.setBounds(400,159,200,50);
        bt_4=new JButton("二部走货批次抽检");
        bt_4.setBounds(100,259,200,50);
        bt_5=new JButton("过程主要，批量性问题");
        bt_5.setBounds(400,259,200,50);
        bt_6=new JButton("反馈及重大问题");
        bt_6.setBounds(100,359,200,50);
        jf.add(bt_open);
        jf.add(bt_close);
        jf.add(bt_sysInfo);
        jf.add(bt_back);
        jf.add(bt_4);
        jf.add(bt_5);
        jf.add(bt_6);
        Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
        Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
        int screenWidth = screenSize.width/2; // 获取屏幕的宽
        int screenHeight = screenSize.height/2; // 获取屏幕的高
        jf.setLocation(screenWidth-800/2, screenHeight-600/2);
    }
    public static void main(String[] args) {
        mainLayout ml=new mainLayout();
        bt_close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JiMi main=new JiMi();
                try {
                    main.main(args);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ml.dispose();
                }
            }
        );
        bt_open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                ExperimentalManage main=new ExperimentalManage();
                try {
                    main.main(args);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ml.dispose();
            }
        }
        );
        bt_sysInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                NiuLiDa main=new NiuLiDa();
                try {
                    main.main(args);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ml.dispose();
            }
        }
        );
        bt_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                ODM main=new ODM();
                try {
                    main.main(args);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ml.dispose();
            }
        }
        );
        bt_4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Two_parts_check main=new Two_parts_check();
                try {
                    main.main(args);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ml.dispose();
            }
        }
        );
        bt_5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Problem main=new Problem();
                try {
                    main.main(args);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ml.dispose();
            }
        }
        );
        bt_6.addActionListener(new ActionListener() {
                                   @Override
                                   public void actionPerformed(ActionEvent arg0) {
                                       Big_Problem main=new Big_Problem();
                                       try {
                                           main.main(args);
                                       } catch (ClassNotFoundException e) {
                                           e.printStackTrace();
                                       } catch (SQLException e) {
                                           e.printStackTrace();
                                       }
                                       ml.dispose();
                                   }
                               }
        );
        //bt_open.addActionListener(bt1_ls);
        //mainLayout ml=new mainLayout();
    }

}

