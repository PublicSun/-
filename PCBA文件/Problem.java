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

public class Problem extends JFrame{
    private final int COLUMN = 22;
    private final List<String> TITLES = Arrays.asList(
            "id", "factory","date", "production_line","batch", "model","machine_core","bad_phenomenon","number","already_produce_no","bad_quantity","adverse_rate","reason",
            "solution","badtype","person_liable", "processing_results","concession","Concession_no","verification_effect","off_date","confir_person");
    private Vector<Vector<String>> dataModel = new Vector<Vector<String>>();
    private QueryItem id = new QueryItem("编号：", 10);
    private QueryItem factory = new QueryItem("工厂：", 10);
    private QueryItem date = new QueryItem("发生日期：", 10);
    private QueryItem production_line = new QueryItem("生产线：", 5);
    //private QueryItem2 age = new QueryItem2("年龄自：", "到", 5);
    private QueryItem batch = new QueryItem("批次：", 5);
    private QueryItem model = new QueryItem("机型：", 5);
    private QueryItem machine_core = new QueryItem("机芯：", 10);
    private QueryItem bad_phenomenon = new QueryItem("不良现象：", 10);
    private QueryItem number = new QueryItem("批量：", 10);
    private QueryItem already_produce_no = new QueryItem("以生产数：", 20);
    private QueryItem bad_quantity = new QueryItem("不良数量：", 20);
    private QueryItem adverse_rate = new QueryItem("不良比率：", 10);
    private QueryItem reason = new QueryItem("原因分析：", 10);
    private QueryItem solution = new QueryItem("解决措施：", 10);
    private QueryItem badtype = new QueryItem("不良类型：", 20);
    private QueryItem person_liable = new QueryItem("责任人：", 20);
    private QueryItem processing_results = new QueryItem("处理结果：", 20);
    private QueryItem concession = new QueryItem("让步人员：", 20);
    private QueryItem Concession_no = new QueryItem("让步数量：", 20);
    private QueryItem verification_effect = new QueryItem("验证效果/关闭依据：", 20);
    private QueryItem off_date = new QueryItem("关闭日期：", 20);
    private QueryItem confir_person = new QueryItem("确认人：", 20);


    private JButton queryBtn = new JButton("查询");
    private JButton saveBtn = new JButton("修改");
    private JButton insertBtn = new JButton("添加");
    private JButton deleteBtn = new JButton("删除");
    private JButton daochu  =new JButton("导出");
    private JTextArea textarea = new JTextArea(5, 5);
    private MyTable table;
    private Connection conn;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Problem frame = new Problem();
        Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
        Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
        frame.connectToDB();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(screenSize.width, screenSize.height));
        frame.setVisible(true);
        frame.setResizable(false);
        //Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
        //Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
        int screenWidth = screenSize.width/2; // 获取屏幕的宽
        int screenHeight = screenSize.height/2; // 获取屏幕的高
        //frame.setLocation(screenWidth-1500/2, screenHeight-950/2);
    }

    //构造函数，负责创建用户界面
    public Problem() {
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
        table.getColumnModel().getColumn(15);
        table.getColumnModel().getColumn(16);
        table.getColumnModel().getColumn(17);
        table.getColumnModel().getColumn(18);
        table.getColumnModel().getColumn(19);
        table.getColumnModel().getColumn(20);
        table.getColumnModel().getColumn(21);


        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(id);
        controlPanel.add(factory);
        controlPanel.add(date);
        controlPanel.add(production_line);
        controlPanel.add(batch);
        controlPanel.add(model);
        controlPanel.add(machine_core);
        controlPanel.add(bad_phenomenon);
        controlPanel.add(number);
        controlPanel.add(already_produce_no);
        controlPanel.add(bad_quantity);
        controlPanel.add(adverse_rate);
        controlPanel.add(reason);
        controlPanel.add(solution);
        controlPanel.add(badtype);
        controlPanel.add(person_liable);
        controlPanel.add(processing_results);
        controlPanel.add(concession);
        controlPanel.add(Concession_no);
        controlPanel.add(verification_effect);
        controlPanel.add(off_date);
        controlPanel.add(confir_person);


        controlPanel.add(queryBtn);
        controlPanel.add(saveBtn);
        controlPanel.add(insertBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(daochu);
        controlPanel.setPreferredSize(new Dimension(0, 200));

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
                if (id.isSelected()) conditions.add("(id like '" + id.getText() + "')");
                if (factory.isSelected()) conditions.add("(factory like '" + factory.getText() + "')");
                if (date.isSelected()) conditions.add("(date like '" + date.getText() + "')");
                if (production_line.isSelected()) conditions.add("(production_line like '" + production_line.getText() + "')");
                if (batch.isSelected()) conditions.add("(batch = '" + batch.getText() + "')");
                //if (age.isSelected()) conditions.add("(Sage >= " + age.getText() + " AND " + "Sage <= " + age.getText2() + ")");
                if (model.isSelected()) conditions.add("(model = '" + model.getText() + "')");
                if (machine_core.isSelected()) conditions.add("(machine_core like '" + machine_core.getText() + "')");
                if (bad_phenomenon.isSelected()) conditions.add("(bad_phenomenon = '" + bad_phenomenon.getText() + "')");
                if (number.isSelected()) conditions.add("(number like '" + number.getText() + "')");
                if (already_produce_no.isSelected()) conditions.add("(already_produce_no like '" + already_produce_no.getText() + "')");
                if (bad_quantity.isSelected()) conditions.add("(bad_quantity like '" + bad_quantity.getText() + "')");
                if (adverse_rate.isSelected()) conditions.add("(adverse_rate like '" + adverse_rate.getText() + "')");
                if (reason.isSelected()) conditions.add("(reason like '" + reason.getText() + "')");
                if (solution.isSelected()) conditions.add("(solution like '" + solution.getText() + "')");
                if (badtype.isSelected()) conditions.add("(badtype like '" + badtype.getText() + "')");
                if (person_liable.isSelected()) conditions.add("(person_liable like '" + person_liable.getText() + "')");
                if (processing_results.isSelected()) conditions.add("(processing_results like '" + processing_results.getText() + "')");
                if (concession.isSelected()) conditions.add("(concession like '" + concession.getText() + "')");
                if (Concession_no.isSelected()) conditions.add("(Concession_no like '" + Concession_no.getText() + "')");
                if (verification_effect.isSelected()) conditions.add("(verification_effect like '" + verification_effect.getText() + "')");
                if (off_date.isSelected()) conditions.add("(off_date like '" + off_date.getText() + "')");
                if (confir_person.isSelected()) conditions.add("(confir_person like '" + confir_person.getText() + "')");



                StringBuilder sb = new StringBuilder();
                sb.append("select * from problem");
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
                String sql = "update problem set " + TITLES.get(column) + " = ? where id = ?;";

                //在文本框显示 SQL 命令
                String cmd = "update problem set " + TITLES.get(column) + " = ";
                cmd += (TITLES.get(column) == "id") ? val : "'" + val + "'";
                cmd += " where id  = '" + sid + "';";
                textarea.setText(cmd);

                PreparedStatement ps;
                try {
                    ps = conn.prepareStatement(sql);
                    if (TITLES.get(column) == "id") ps.setInt(1, Integer.valueOf(val));
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
                String sql = "insert into problem values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                String sid = id.getText();
                String sfactory = factory.getText();
                String sdate = date.getText();
                String sproduction_line = production_line.getText();
                //String sage = age.getText();
                String sbatch = batch.getText();
                String smodel = model.getText();
                String smachine_core = machine_core.getText();
                String sbad_phenomenon = bad_phenomenon.getText();
                String snumber = number.getText();
                String salready_produce_no = already_produce_no.getText();
                String sbad_quantity = bad_quantity.getText();
                String sadverse_rate = adverse_rate.getText();
                String sreason = reason.getText();
                String ssolution = solution.getText();
                String sbadtype = badtype.getText();
                String sperson_liable = person_liable.getText();
                String sprocessing_results = processing_results.getText();
                String sconcession = concession.getText();
                String sConcession_no = Concession_no.getText();
                String sverification_effect = verification_effect.getText();
                String soff_date = off_date.getText();
                String sconfir_person = confir_person.getText();


                //在文本框显示 SQL 命令
                String cmd = "insert into problem values ('" + sid + "', '" + sfactory + "', '" + sdate + "', '" +
                        sproduction_line + "','" + sbatch + "','" + smodel + "','" + smachine_core + "','" + sbad_phenomenon + "','" + snumber + "','" + salready_produce_no + "','" + sbad_quantity + "'," +
                        ",'" + sadverse_rate + "','" + sreason + "','" + ssolution + "','" + sbadtype + "',,'" + sperson_liable + "''" + sprocessing_results + "','" + sconcession + "','" + sConcession_no + "','" + sverification_effect + "'" +
                        ",'" + soff_date + "','" + sconfir_person + "');";
                textarea.setText(cmd);

                PreparedStatement ps;
                try {
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, sid);
                    ps.setString(2, sfactory);
                    ps.setString(3, sdate);
                    ps.setString(4, sproduction_line);
                    ps.setString(5, sbatch);
                    //ps.setInt(4, Integer.valueOf(sage));
                    ps.setString(6, smodel);
                    ps.setString(7, smachine_core);
                    ps.setString(8, sbad_phenomenon);
                    ps.setString(9, snumber);
                    ps.setString(10, salready_produce_no);
                    ps.setString(11, sbad_quantity);
                    ps.setString(12, sadverse_rate);
                    ps.setString(13, sreason);
                    ps.setString(14, ssolution);
                    ps.setString(15, sbadtype);
                    ps.setString(16, sperson_liable);
                    ps.setString(17, sprocessing_results);
                    ps.setString(18, sconcession);
                    ps.setString(19, sConcession_no);
                    ps.setString(20, sverification_effect);
                    ps.setString(21, soff_date);
                    ps.setString(22, sconfir_person);

                    //ps.executeUpdate();
                    ps.execute();
                    dataModel.add(new Vector<String>(Arrays.asList(
                            sid,sfactory,sdate, sproduction_line,sbatch,smodel,smachine_core,sbad_phenomenon,snumber,
                            salready_produce_no,sbad_quantity,sadverse_rate,sreason,ssolution,sbadtype,sperson_liable,sprocessing_results,sconcession,sConcession_no,
                            sverification_effect,soff_date,sconfir_person )));

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
                String sql = "delete from problem where id = '" + sid + "';";

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
            String Table_Name = "problem";
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
