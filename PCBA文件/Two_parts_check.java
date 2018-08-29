package 界面.PCBA文件;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import 界面.MyTable;
import 界面.QueryItem;

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

public class Two_parts_check extends JFrame{
    private final int COLUMN = 14;
    private final List<String> TITLES = Arrays.asList(
            "date", "production_line", "batch_number", "model","plate","batch","check_number","test_number","bad_content","bad_num","unqalified_judgment","result","remarks","spot_check");
    private Vector<Vector<String>> dataModel = new Vector<Vector<String>>();
    private QueryItem date = new QueryItem("日期：", 10);
    private QueryItem production_line = new QueryItem("生产线：", 10);
    //private QueryItem2 age = new QueryItem2("年龄自：", "到", 5);
    private QueryItem batch_number = new QueryItem("批次号：", 5);
    private QueryItem model = new QueryItem("机型/机芯：", 5);
    private QueryItem plate = new QueryItem("板别：", 10);
    //private QueryItem weight_of_components = new QueryItem("组件净重：", 5);
    private QueryItem batch = new QueryItem("批量：", 10);
    private QueryItem check_number = new QueryItem("交验数：", 10);
    private QueryItem test_number = new QueryItem("检验数：", 20);
    private QueryItem bad_content = new QueryItem("抽检不良内容：", 20);
    private QueryItem bad_num = new QueryItem("不良数：", 10);
    private QueryItem unqalified_judgment = new QueryItem("不良合格判定：", 10);
    private QueryItem result = new QueryItem("结果：", 10);
    private QueryItem remarks = new QueryItem("备注：", 20);
    private QueryItem spot_check = new QueryItem("抽检人：", 20);

    private JButton queryBtn = new JButton("查询");
    private JButton saveBtn = new JButton("修改");
    private JButton insertBtn = new JButton("添加");
    private JButton deleteBtn = new JButton("删除");
    private JButton daochu  =new JButton("导出");
    private JTextArea textarea = new JTextArea(5, 5);
    private MyTable table;
    private Connection conn;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Two_parts_check frame = new Two_parts_check();
        frame.connectToDB();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1500, 950));
        frame.setVisible(true);
        frame.setResizable(false);
        Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
        Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
        int screenWidth = screenSize.width/2; // 获取屏幕的宽
        int screenHeight = screenSize.height/2; // 获取屏幕的高
        frame.setLocation(screenWidth-1500/2, screenHeight-950/2);
    }

    //构造函数，负责创建用户界面
    public Two_parts_check() {
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
        table.getColumnModel().getColumn(9);
        table.getColumnModel().getColumn(10);
        table.getColumnModel().getColumn(11);
        table.getColumnModel().getColumn(12);
        table.getColumnModel().getColumn(13);
        //table.getColumnModel().getColumn(14);


        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(date);
        controlPanel.add(production_line);
        controlPanel.add(batch_number);
        controlPanel.add(model);
        controlPanel.add(plate);
        //controlPanel.add(weight_of_components);
        controlPanel.add(batch);
        controlPanel.add(check_number);
        controlPanel.add(test_number);
        controlPanel.add(bad_content);
        controlPanel.add(bad_num);
        controlPanel.add(unqalified_judgment);
        controlPanel.add(result);
        controlPanel.add(remarks);
        controlPanel.add(spot_check);


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
                if (date.isSelected()) conditions.add("(date like '" + date.getText() + "')");
                if (production_line.isSelected()) conditions.add("(production_line like '" + production_line.getText() + "')");
                //if (age.isSelected()) conditions.add("(Sage >= " + age.getText() + " AND " + "Sage <= " + age.getText2() + ")");
                if (batch_number.isSelected()) conditions.add("(batch_number = '" + batch_number.getText() + "')");
                if (model.isSelected()) conditions.add("(model like '" + model.getText() + "')");
                if (plate.isSelected()) conditions.add("(plate = '" + plate.getText() + "')");
                //if (weight_of_components.isSelected()) conditions.add("(weight_of_components = '" + weight_of_components.getText() + "')");
                if (batch.isSelected()) conditions.add("(batch like '" + batch.getText() + "')");
                if (check_number.isSelected()) conditions.add("(check_number like '" + check_number.getText() + "')");
                if (test_number.isSelected()) conditions.add("(test_number like '" + test_number.getText() + "')");
                if (bad_content.isSelected()) conditions.add("(bad_content like '" + bad_content.getText() + "')");
                if (bad_num.isSelected()) conditions.add("(bad_num like '" + bad_num.getText() + "')");
                if (unqalified_judgment.isSelected()) conditions.add("(unqalified_judgment like '" + unqalified_judgment.getText() + "')");
                if (result.isSelected()) conditions.add("(result like '" + result.getText() + "')");
                if (remarks.isSelected()) conditions.add("(remarks like '" + remarks.getText() + "')");
                if (spot_check.isSelected()) conditions.add("(spot_check like '" + spot_check.getText() + "')");


                StringBuilder sb = new StringBuilder();
                sb.append("select * from two_parts_check");
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
                String sql = "update two_parts_check set " + TITLES.get(column) + " = ? where date = ?;";

                //在文本框显示 SQL 命令
                String cmd = "update two_parts_check set " + TITLES.get(column) + " = ";
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
                String sql = "insert into two_parts_check values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                String sdate = date.getText();
                String sproduction_line = production_line.getText();
                //String sage = age.getText();
                String sbatch_number = batch_number.getText();
                String smodel = model.getText();
                String splate = plate.getText();
                //String sweight_of_components = weight_of_components.getText();
                String sbatch = batch.getText();
                String scheck_number = check_number.getText();
                String stest_number = test_number.getText();
                String sbad_content = bad_content.getText();
                String sbad_num = bad_num.getText();
                String sunqalified_judgment = unqalified_judgment.getText();
                String sresult = result.getText();
                String sremarks = remarks.getText();
                String sspot_check = spot_check.getText();

                //在文本框显示 SQL 命令
                String cmd = "insert into two_parts_check values ('" + sdate + "', '" + sproduction_line + "','" + sbatch_number + "','" + smodel + "','" + splate + "','" + sbatch + "','" + scheck_number + "','" + stest_number + "','" + sbad_content + "','" + sbad_num + "'," +
                        ",'" + sunqalified_judgment + "','" + sresult + "','" + sremarks + "','" + sspot_check + "');";
                textarea.setText(cmd);

                PreparedStatement ps;
                try {
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, sdate);
                    ps.setString(2, sproduction_line);
                    //ps.setInt(4, Integer.valueOf(sage));
                    ps.setString(3, sbatch_number);
                    ps.setString(4, smodel);
                    ps.setString(5, splate);
                    //ps.setString(6, sweight_of_components);
                    ps.setString(7, sbatch);
                    ps.setString(8, scheck_number);
                    ps.setString(9, stest_number);
                    ps.setString(10, sbad_content);
                    ps.setString(11, sbad_num);
                    ps.setString(12, sunqalified_judgment);
                    ps.setString(13, sresult);
                    ps.setString(14, sremarks);
                    ps.setString(15, sspot_check);

                    //ps.executeUpdate();
                    ps.execute();
                    dataModel.add(new Vector<String>(Arrays.asList(
                            sdate, sproduction_line,sbatch_number,smodel,splate,sbatch,scheck_number,stest_number,sbad_content,sbad_num,sunqalified_judgment,sresult,sremarks,sspot_check)));

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
                String sql = "delete from two_parts_check where date = '" + sid + "';";

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

            String Table_Name = "two_parts_check";

            Sheet sheet = book.createSheet(Table_Name);
            Statement st = (Statement) con.createStatement();
            // 创建sql语句，对team进行查询所有数据
            String sql = "select * from " + Table_Name;

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
