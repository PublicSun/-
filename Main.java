package 界面;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.net.IDN;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

        import javax.swing.*;

import static 数据库管理软件.App.url;
import static 数据库管理软件.App.user;
import static 数据库管理软件.App.password;
/**
 * @author: Wray Zheng
 * @date: 2018-03-25
 * @description: 可对特定数据库表进行增删改查的图形界面程序
 */
public class Main extends JFrame {
    private final int COLUMN = 4;
    private final List<String> TITLES = Arrays.asList(
            "IDno", "name", "idname", "location");
    private Vector<Vector<String>> dataModel = new Vector<Vector<String>>();
    private QueryItem IDno = new QueryItem("编号：", 10);
    private QueryItem name = new QueryItem("姓名：", 10);
    private QueryItem idname = new QueryItem("性别：", 5);
    private QueryItem location = new QueryItem("地址：", 10);
    private JButton queryBtn = new JButton("查询");
    private JButton saveBtn = new JButton("修改");
    private JButton insertBtn = new JButton("添加");
    private JButton deleteBtn = new JButton("删除");
    private JButton daochu  =new JButton("导出");
    private JTextArea textarea = new JTextArea(5, 5);
    private MyTable table;
    private Connection conn;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Main frame = new Main();
        frame.connectToDB();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(750, 500));
        frame.setVisible(true);
        frame.setResizable(false);
    }

    //构造函数，负责创建用户界面
    public Main() {
        //super(title);

        Vector<String> titles = new Vector<String>(TITLES);
        table = new MyTable(dataModel, titles);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(IDno);
        controlPanel.add(name);
        controlPanel.add(idname);
        //controlPanel.add(age);
        //controlPanel.add(class_);
        //controlPanel.add(dept);
        controlPanel.add(location);
        controlPanel.add(queryBtn);
        controlPanel.add(saveBtn);
        controlPanel.add(insertBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(daochu);
        controlPanel.setPreferredSize(new Dimension(0, 130));

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        tablePanel.add(table.getTableHeader());
        tablePanel.add(new JScrollPane(table));

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(textarea, BorderLayout.NORTH);
        container.add(tablePanel, BorderLayout.CENTER);

        this.add(controlPanel, BorderLayout.NORTH);
        this.add(container, BorderLayout.CENTER);
        this.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.WEST);
        this.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.EAST);
        this.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.SOUTH);

        setActionListener();
    }

    //程序启动时，需调用该方法连接到数据库
    //之所以不放在构造函数中，是因为这些操作可能抛出异常，需要单独处理
    public void connectToDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        final String URL = "jdbc:mysql://localhost:3306/WenJian";
        conn = DriverManager.getConnection(URL, "root", "123456");
    }

    private void setActionListener() {
        //根据指定条件，列出数据库中满足条件的记录
        queryBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> conditions = new ArrayList<String>();
                if (IDno.isSelected()) conditions.add("(IDno = '" + IDno.getText() + "')");
                if (name.isSelected()) conditions.add("(name like '" + name.getText() + "')");
                if (idname.isSelected()) conditions.add("(idname = '" + idname.getText() + "')");
                //if (age.isSelected()) conditions.add("(Sage >= " + age.getText() + " AND " + "Sage <= " + age.getText2() + ")");
                //if (class_.isSelected()) conditions.add("(Sclass = '" + class_.getText() + "')");
                //if (dept.isSelected()) conditions.add("(Sdept = '" + dept.getText() + "')");
                if (location.isSelected()) conditions.add("(location like '" + location.getText() + "')");

                StringBuilder sb = new StringBuilder();
                sb.append("select * from departments");
                int length = conditions.size();
                if (length != 0) sb.append(" where ");
                for (int i = 0; i < length; i++) {
                    sb.append(conditions.get(i));
                    if (i != length - 1) sb.append(" AND ");
                }
                sb.append(";");
                String queryString = sb.toString();
                textarea.setText(queryString);

                dataModel.clear();
                Statement stmt;
                try {
                    stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(queryString);
                    Vector<String> record;
                    while (rs.next()) {
                        record = new Vector<String>();
                        for (int i = 0; i < COLUMN; i++) {
                            record.add(rs.getString(i + 1));
                        }
                        dataModel.add(record);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                //更新表格
                table.validate();
                table.updateUI();
            }

        });

        //根据用户当前选中的单元格，修改数据库中对应记录的对应字段
        saveBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                if (row == -1 || column == 0) return;

                String val = dataModel.get(row).get(column);
                String sid = dataModel.get(row).get(0);
                String sql = "update departments set " + TITLES.get(column) + " = ? where IDno = ?;";

                //在文本框显示 SQL 命令
                String cmd = "update departments set " + TITLES.get(column) + " = ";
                cmd += (TITLES.get(column) == "location") ? val : "'" + val + "'";
                cmd += " where IDno  = '" + sid + "';";
                textarea.setText(cmd);

                PreparedStatement ps;
                try {
                    ps = conn.prepareStatement(sql);
                    if (TITLES.get(column) == "location") ps.setInt(1, Integer.valueOf(val));
                    else ps.setString(1, val);
                    ps.setString(2, sid);
                    ps.executeUpdate();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //往数据库中插入一条新的记录
        insertBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = "insert into departments values (?,?,?,?);";
                String sid = IDno.getText();
                String sname = name.getText();
                String ssex = idname.getText();
                //String sage = age.getText();
                //String sclass = class_.getText();
                //String sdept = dept.getText();
                String saddr = location.getText();

                //在文本框显示 SQL 命令
                String cmd = "insert into departments values ('" + sid + "', '" + sname + "', '" +
                        ssex + "','" + saddr + "');";
                textarea.setText(cmd);

                PreparedStatement ps;
                try {
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, sid);
                    ps.setString(2, sname);
                    ps.setString(3, ssex);
                    //ps.setInt(4, Integer.valueOf(sage));
                    //ps.setString(5, sclass);
                    //ps.setString(6, sdept);
                    ps.setString(4, saddr);
                    //ps.executeUpdate();
                    ps.execute();
                    dataModel.add(new Vector<String>(Arrays.asList(
                            sid, sname, ssex,saddr)));

                    //更新表格
                    table.validate();
                    table.updateUI();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

        });

        //将用户当前选中的记录从数据库中删除
        deleteBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                String sid = dataModel.get(row).get(0);
                String sql = "delete from departments where IDno = '" + sid + "';";

                //在文本框显示 SQL 命令
                textarea.setText(sql);

                Statement stmt;
                try {
                    stmt = conn.createStatement();
                    if (stmt.executeUpdate(sql) == 0) return;
                    dataModel.remove(row);

                    //更新表格
                    table.validate();
                    table.updateUI();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

        });
        daochu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dao();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    private void dao()throws Exception {
        try {
            Workbook book = new HSSFWorkbook();
            //connectToDB();
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection(url, user,
                    password);
            // DatabaseMetaData dbmt = con.getMetaData();
            // 获得blog数据库
            //TODO 需要将blog修改为你指定的数据库
            // ResultSet rs = dbmt.getTables("blog", "blog", null, new String[] {
            // "TABLE", "VIEW" });
            // 设置要转化为Excel的表
            //TODO 需要将Table_Name修改为当前数据库中你想导出的数据表
            String Table_Name = "departments";
            // while (rs.next()) {
            // if ("team".equals(rs.getString("TABLE_NAME"))) {
            // Table_Name = "team";
            // break;
            // }
            // }
            // 在当前Excel创建一个子表
            Sheet sheet = book.createSheet(Table_Name);
            Statement st = (Statement) con.createStatement();
            // 创建sql语句，对team进行查询所有数据
            String sql = "select * from " + Table_Name;
            //String sql = "select * from blog." + Table_Name;
            ResultSet rs = st.executeQuery(sql);
            // 设置表头信息（写入Excel左上角是从(0,0)开始的）
            Row row1 = sheet.createRow(0);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colnum = rsmd.getColumnCount();
            for (int i = 1; i <= colnum; i++) {
                String name = rsmd.getColumnName(i);
                // 单元格
                Cell cell = row1.createCell(i - 1);
                // 写入数据
                cell.setCellValue(name);
            }
            // 设置表格信息
            int idx = 1;
            while (rs.next()) {
                // 行
                Row row = sheet.createRow(idx++);
                for (int i = 1; i <= colnum; i++) {
                    String str = rs.getString(i);
                    // 单元格
                    Cell cell = row.createCell(i - 1);
                    // 写入数据
                    cell.setCellValue(str);
                }
            }
            // 保存
            book.write(new FileOutputStream("D://" + Table_Name + ".xls"));
        }catch (Exception e){

        }
    }

}

