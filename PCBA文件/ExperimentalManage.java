package 界面.PCBA文件;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import 界面.Main;
import 界面.MyTable;
import 界面.QueryItem;
import 界面.success;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static 数据库管理软件.App.password;
import static 数据库管理软件.App.url;
import static 数据库管理软件.App.user;

public class ExperimentalManage extends JFrame{
    private final int COLUMN = 9;
    private final List<String> TITLES = Arrays.asList(
            "id", "date","batch", "model", "EProject","EParameters","NoPeriments","FCT","Remarks");
    private Vector<Vector<String>> dataModel = new Vector<Vector<String>>();
    private QueryItem id = new QueryItem("序号：", 10);
    private QueryItem date = new QueryItem("日期：", 10);
    private QueryItem batch = new QueryItem("批次：", 5);
    //private QueryItem2 age = new QueryItem2("年龄自：", "到", 5);
    private QueryItem model = new QueryItem("机型：", 5);
    private QueryItem EProject = new QueryItem("实验项目：", 5);
    private QueryItem EParameters = new QueryItem("实验参数：", 10);
    private QueryItem NoPeriments = new QueryItem("试验数量：", 10);
    private QueryItem FCT = new QueryItem("重调FCT有无不良：", 10);
    private QueryItem Remarks = new QueryItem("备注：", 20);
    private JButton queryBtn = new JButton("查询");
    private JButton saveBtn = new JButton("修改");
    private JButton insertBtn = new JButton("添加");
    private JButton deleteBtn = new JButton("删除");
    private static JButton daochu  =new JButton("导出");
    private JTextArea textarea = new JTextArea(5, 5);
    private MyTable table;
    private Connection conn;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ExperimentalManage frame = new ExperimentalManage();
        frame.connectToDB();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1000, 750));
        frame.setVisible(true);
        frame.setResizable(false);
        Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
        Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
        int screenWidth = screenSize.width/2; // 获取屏幕的宽
        int screenHeight = screenSize.height/2; // 获取屏幕的高
        frame.setLocation(screenWidth-1000/2, screenHeight-750/2);
        daochu.addActionListener(new ActionListener() {
                                       @Override
                                       public void actionPerformed(ActionEvent arg0) {
                                           success main=new success();

                                       }
                                   }
        );
    }

    //构造函数，负责创建用户界面
    public ExperimentalManage() {
        //super(title);

        Vector<String> titles = new Vector<String>(TITLES);
        table = new MyTable(dataModel, titles);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4);
        table.getColumnModel().getColumn(5);
        table.getColumnModel().getColumn(6);
        table.getColumnModel().getColumn(7);
        table.getColumnModel().getColumn(8);


        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(id);
        controlPanel.add(date);
        controlPanel.add(batch);
        controlPanel.add(model);
        controlPanel.add(EProject);
        controlPanel.add(EParameters);
        controlPanel.add(NoPeriments);
        controlPanel.add(FCT);
        controlPanel.add(Remarks);
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
                if (id.isSelected()) conditions.add("(id = '" + id.getText() + "')");
                if (date.isSelected()) conditions.add("(date like '" + date.getText() + "')");
                if (batch.isSelected()) conditions.add("(batch like '" + batch.getText() + "')");
                if (model.isSelected()) conditions.add("(model = '" + batch.getText() + "')");
                //if (age.isSelected()) conditions.add("(Sage >= " + age.getText() + " AND " + "Sage <= " + age.getText2() + ")");
                if (EProject.isSelected()) conditions.add("(EProject = '" + EProject.getText() + "')");
                if (EParameters.isSelected()) conditions.add("(EParameters like '" + EParameters.getText() + "')");
                if (NoPeriments.isSelected()) conditions.add("(NoPeriments = '" + NoPeriments.getText() + "')");
                if (FCT.isSelected()) conditions.add("(FCT like '" + FCT.getText() + "')");
                if (Remarks.isSelected()) conditions.add("(Remarks like '" + Remarks.getText() + "')");

                StringBuilder sb = new StringBuilder();
                sb.append("select * from PCBA_QA_ExperimentalManage");
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
                String sql = "update PCBA_QA_ExperimentalManage set " + TITLES.get(column) + " = ? where id = ?;";

                //在文本框显示 SQL 命令
                String cmd = "update PCBA_QA_ExperimentalManage set " + TITLES.get(column) + " = ";
                cmd += (TITLES.get(column) == "date") ? val : "'" + val + "'";
                cmd += " where id  = '" + sid + "';";
                textarea.setText(cmd);

                PreparedStatement ps;
                try {
                    ps = conn.prepareStatement(sql);
                    if (TITLES.get(column) == "date") ps.setInt(1, Integer.valueOf(val));
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
                String sql = "insert into PCBA_QA_ExperimentalManage values (?,?,?,?,?,?,?,?,?);";
                String sid = id.getText();
                String sdate = date.getText();
                String sbatch = batch.getText();
                //String sage = age.getText();
                String smodel = model.getText();
                String sEProject = EProject.getText();
                String sEParameters = EParameters.getText();
                String sNoPeriments = NoPeriments.getText();
                String sFCT = FCT.getText();
                String sRemarks = Remarks.getText();

                //在文本框显示 SQL 命令
                String cmd = "insert into PCBA_QA_ExperimentalManage values ('" + sid + "', '" + sdate + "', '" +
                        sbatch + "','" + smodel + "','" + sEParameters + "','" + sEProject + "','" + sNoPeriments + "','" + sFCT + "','" + sRemarks + "');";
                textarea.setText(cmd);

                PreparedStatement ps;
                try {
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, sid);
                    ps.setString(2, sdate);
                    ps.setString(3, sbatch);
                    ps.setString(4, smodel);
                    //ps.setInt(4, Integer.valueOf(sage));
                    ps.setString(5, sEProject);
                    ps.setString(6, sEParameters);
                    ps.setString(7, sNoPeriments);
                    ps.setString(8, sFCT);
                    ps.setString(9, sRemarks);
                    //ps.executeUpdate();
                    ps.execute();
                    dataModel.add(new Vector<String>(Arrays.asList(
                            sid, sdate, sbatch,smodel,sEProject,sEParameters,sNoPeriments,sFCT,sRemarks)));

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
                String sql = "delete from PCBA_QA_ExperimentalManage where id = '" + sid + "';";

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
            String Table_Name = "PCBA_QA_ExperimentalManage";
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
