package 界面.PCBA文件;

import com.sun.imageio.plugins.common.ImageUtil;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import 界面.MyTable;
import 界面.QueryItem;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


import static 数据库管理软件.App.password;
import static 数据库管理软件.App.url;
import static 数据库管理软件.App.user;

public class Big_Problem extends JFrame{
    private final int COLUMN = 18;
    private static FileDialog diaOpen;
    private final List<String> TITLES = Arrays.asList(
            "id", "customer","date", "batch_num", "model","problem","batch","sampling","bad_num","adverse_rate","bad_picture","reason","solution","presentation","processing_scheme",
            "verification_record","result","person_liable");
    private Vector<Vector<String>> dataModel = new Vector<Vector<String>>();
    private QueryItem id = new QueryItem("序号：", 10);
    private QueryItem customer = new QueryItem("客户：", 10);
    private QueryItem date = new QueryItem("日期：", 5);
    //private QueryItem2 age = new QueryItem2("年龄自：", "到", 5);
    private QueryItem batch_num = new QueryItem("批次：", 5);
    private QueryItem model = new QueryItem("机型/机芯：", 5);
    private QueryItem problem = new QueryItem("问题点：", 10);
    private QueryItem batch = new QueryItem("批量：", 10);
    private QueryItem sampling = new QueryItem("抽样：", 10);
    private QueryItem bad_num = new QueryItem("不良数：", 20);
    private QueryItem adverse_rate = new QueryItem("不良率：", 20);
    private QueryItem bad_picture = new QueryItem("不良图片：", 10);
    private QueryItem reason = new QueryItem("原因分析：", 10);
    private QueryItem solution = new QueryItem("改善措施：", 10);
    private QueryItem presentation = new QueryItem("报告：", 20);
    private QueryItem processing_scheme = new QueryItem("处理方案：", 20);
    private QueryItem verification_record = new QueryItem("验证记录：", 20);
    private QueryItem result = new QueryItem("结果：", 20);
    private QueryItem person_liable = new QueryItem("责任人：", 20);


    private JButton queryBtn = new JButton("查询");
    private JButton saveBtn = new JButton("修改");
    private JButton insertBtn = new JButton("添加");
    private JButton deleteBtn = new JButton("删除");
    private JButton daochu  =new JButton("导出");
    private static JButton xuanze=new JButton("选择图片");
    private JTextArea textarea = new JTextArea(5, 5);
    private MyTable table;
    private Connection conn;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Big_Problem frame = new Big_Problem();
        frame.connectToDB();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1500, 950));
        frame.setVisible(true);
        frame.setResizable(false);

        xuanze.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    diaOpen=new FileDialog(frame,"打开",FileDialog.LOAD);
                    diaOpen.setVisible(true);
                    //获得所选取文件的目录
                    String dirPath=diaOpen.getDirectory();
                    //获得选取的文件的文件名字，如果没有选取，则返回null（即点击的是取消按钮），这要处理一下
                    String fileName=diaOpen.getFile();
                    frame.bad_picture.getText1(dirPath+fileName);
                    if(dirPath==null && fileName==null){
                        return;
                    }
                    InputStream in= getImageByte(dirPath);
                    //frame.bad_picture.getText(dirPath);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
        Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
        int screenWidth = screenSize.width/2; // 获取屏幕的宽
        int screenHeight = screenSize.height/2; // 获取屏幕的高
        frame.setLocation(screenWidth-1500/2, screenHeight-950/2);
    }
    public static FileInputStream getImageByte(String infile) throws FileNotFoundException {
        File file=null;
        FileInputStream imageByte=null;
        file=new File(infile);
        imageByte=new FileInputStream(file);
        return imageByte;
    }

    //构造函数，负责创建用户界面
    public Big_Problem() {
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
        table.getColumnModel().getColumn(14);
        table.getColumnModel().getColumn(15);
        table.getColumnModel().getColumn(16);
        table.getColumnModel().getColumn(17);



        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(id);
        controlPanel.add(customer);
        controlPanel.add(date);
        controlPanel.add(batch_num);
        controlPanel.add(model);
        controlPanel.add(problem);
        controlPanel.add(batch);
        controlPanel.add(sampling);
        controlPanel.add(bad_num);
        controlPanel.add(adverse_rate);
        controlPanel.add(bad_picture);;
        controlPanel.add(reason);
        controlPanel.add(solution);
        controlPanel.add(presentation);
        controlPanel.add(processing_scheme);
        controlPanel.add(verification_record);
        controlPanel.add(result);
        controlPanel.add(person_liable);


        controlPanel.add(queryBtn);
        controlPanel.add(saveBtn);
        controlPanel.add(insertBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(daochu);
        controlPanel.add(xuanze);
        controlPanel.setPreferredSize(new Dimension(0, 150));

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
                if (customer.isSelected()) conditions.add("(customer like '" + customer.getText() + "')");
                if (date.isSelected()) conditions.add("(date = '" + date.getText() + "')");
                //if (age.isSelected()) conditions.add("(Sage >= " + age.getText() + " AND " + "Sage <= " + age.getText2() + ")");
                if (batch_num.isSelected()) conditions.add("(batch_num = '" + batch_num.getText() + "')");
                if (model.isSelected()) conditions.add("(model like '" + model.getText() + "')");
                if (problem.isSelected()) conditions.add("(problem = '" + problem.getText() + "')");
                if (batch.isSelected()) conditions.add("(batch like '" + batch.getText() + "')");
                if (sampling.isSelected()) conditions.add("(sampling like '" + sampling.getText() + "')");
                if (bad_num.isSelected()) conditions.add("(bad_num like '" + bad_num.getText() + "')");
                if (adverse_rate.isSelected()) conditions.add("(adverse_num like '" + adverse_rate.getText() + "')");
                if (bad_picture.isSelected()) conditions.add("(bad_picture like '" + bad_picture.getText() + "')");
                if (reason.isSelected()) conditions.add("(reason like '" + reason.getText() + "')");
                if (solution.isSelected()) conditions.add("(solution like '" + solution.getText() + "')");
                if (presentation.isSelected()) conditions.add("(presentation like '" + presentation.getText() + "')");
                if (processing_scheme.isSelected()) conditions.add("(processing_scheme like '" + processing_scheme.getText() + "')");
                if (verification_record.isSelected()) conditions.add("(verification_record like '" + verification_record.getText() + "')");
                if (result.isSelected()) conditions.add("(result like '" + result.getText() + "')");
                if (person_liable.isSelected()) conditions.add("(person_liable like '" + person_liable.getText() + "')");


                StringBuilder sb = new StringBuilder();
                sb.append("select * from big_problem");
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
                String sql = "update big_problem set " + TITLES.get(column) + " = ? where date = ?;";

                //在文本框显示 SQL 命令
                String cmd = "update big_problem set " + TITLES.get(column) + " = ";
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
                String sql = "insert into big_problem values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                String sid = id.getText();
                String scustomer = customer.getText();
                //String sage = age.getText();
                String sdate = date.getText();
                String sbatch_num = batch_num.getText();
                String smodel = model.getText();
                String sproblem = problem.getText();
                String sbatch = batch.getText();
                String ssampling = sampling.getText();
                String sbad_num = bad_num.getText();
                String sadverse_rate = adverse_rate.getText();
                String sbad_picture = bad_picture.getText();
                String sreason = reason.getText();
                String ssolution = solution.getText();
                String spresentation = presentation.getText();
                String sprocessing_scheme = processing_scheme.getText();
                String sverification_record = verification_record.getText();
                String sresult = result.getText();
                String sperson_liable = person_liable.getText();

                //在文本框显示 SQL 命令
                String cmd = "insert into big_problem values ('" + sid + "', '" + scustomer + "', '" +
                        sdate + "','" + sbatch_num + "','" + smodel + "','" + sproblem + "','" + sbatch + "','" + ssampling + "','" + sbad_num + "','" + sadverse_rate + "','" + sbad_picture + "'," +
                        ",'" + sreason + "','" + ssolution + "','" + spresentation + "','" + sprocessing_scheme + "','" + sverification_record + "','" + sresult + "','" + sperson_liable + "');";
                textarea.setText(cmd);

                PreparedStatement ps;
                try {
                    InputStream in= getImageByte(sbad_picture);
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, sid);
                    ps.setString(2, scustomer);
                    ps.setString(3, sdate);
                    //ps.setInt(4, Integer.valueOf(sage));
                    ps.setString(4, sbatch_num);
                    ps.setString(5, smodel);
                    ps.setString(6, sproblem);
                    ps.setString(7, sbatch);
                    ps.setString(8, ssampling);
                    ps.setString(9, sbad_num);
                    ps.setString(10, sadverse_rate);
                    //ps.setString(11, sbad_picture);
                    ps.setBinaryStream(11, in, in.available());
                    ps.setString(12, sreason);
                    ps.setString(13, ssolution);
                    ps.setString(14, spresentation);
                    ps.setString(15, sprocessing_scheme);
                    ps.setString(16, sverification_record);
                    ps.setString(17, sresult);
                    ps.setString(18, sperson_liable);

                    //ps.executeUpdate();
                    ps.execute();
                    dataModel.add(new Vector<String>(Arrays.asList(
                            sid, scustomer,sdate,sbatch_num,smodel,sproblem,sbatch,ssampling,sbad_num,sadverse_rate,sbad_picture,
                            sreason,ssolution,spresentation,sprocessing_scheme,sverification_record,sresult,sperson_liable)));

                    //更新表格
                    table.validate();
                    table.updateUI();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }catch  (IOException e2){
                    e2.printStackTrace();
                }
            }

        });

        //将用户当前选中的记录从数据库中删除
        deleteBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                String sid = dataModel.get(row).get(0);
                String sql = "delete from big_problem where date = '" + sid + "';";

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
    public static BufferedImage imgChangeBuffer(Blob blob) throws IOException {
        byte[] data = null;
        try {
            InputStream inStream = blob.getBinaryStream();
            long nLen = blob.length();
            int nSize = (int) nLen;
            data = new byte[nSize];
            inStream.read(data);
            inStream.close();
        } catch (SQLException e) {
            e.getMessage();
        }
        BufferedImage bis = ImageIO.read(new ByteArrayInputStream(data));
        return bis;
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
            String Table_Name = "big_problem";
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
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();
            //HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            while (rs.next()) {
                // 行
                Row row = sheet.createRow(idx++);
                for (int i = 1; i <= colnum; i++) {
                    if (i != 11) {
                        String str = rs.getString(i);
                        // 单元格
                        Cell cell = row.createCell(i - 1);
                        // 写入数据
                        cell.setCellValue(str);
                    } else {
                        Blob picture = rs.getBlob(11);//得到Blob对象
                        //开始读入文件
                        InputStream in = picture.getBinaryStream();
                        OutputStream out = new FileOutputStream("3.jpg");
                        long nLen = picture.length();
                        int nSize = (int) nLen;
                        //data = new byte[nSize];
                        byte[] buffer = new byte[nSize];
                        int len = 0;
                        while((len = in.read(buffer)) != -1){
                            out.write(buffer, 0, len);
                        }
//                        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
//                        Blob image = (Blob) rs.getBlob("bad_picture");
//                        BufferedImage bufferImg = imgChangeBuffer(image);
//                        ImageIO.write(bufferImg, "jpg", byteArrayOut);
//                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255, (short) i, idx - 1, (short) i, idx - 1);
//                        patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));

                    }
                }
            }
            // 保存
            book.write(new FileOutputStream("D://" + Table_Name + ".xls"));
        }catch (Exception e){

            }

        }
    }

