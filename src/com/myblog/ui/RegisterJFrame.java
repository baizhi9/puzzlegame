package com.myblog.ui;


import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;

public class RegisterJFrame extends JFrame implements MouseListener {

    //提升三个输入框的变量的作用范围，让这三个变量可以在本类中所有方法里面可以使用。
    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();
    JPasswordField rePassword = new JPasswordField();

    //提升两个按钮变量的作用范围，让这两个变量可以在本类中所有方法里面可以使用。
    JButton submit = new JButton();
    JButton reset = new JButton();


    public RegisterJFrame() {
        initFrame();
        initView();
        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //点击后，需要注册或重置
        if(e.getSource() == submit){
            //获取三个文本输入框中的内容
            String usernameInput = username.getText();
            String passwordInput = password.getText();
            String repasswordInput = rePassword.getText();
            //注册时，先判断用户名或密码是否为空
            //再判断用户在数据库中是否存在。如果不存在，则可以注册
            //再判断前后输入的密码是否一样，如果一样，则成功注册，把数据添加到数据库里，并进入游戏
            if(usernameInput.length() == 0 || passwordInput.length() == 0 || repasswordInput.length() == 0){
                showDialog("用户名或密码不能为空");
            }else{
                try {
                    if(findUser(usernameInput)){
                        if(passwordInput.equals(repasswordInput)){
                            try {
                                addUser(usernameInput, passwordInput);
                            } catch (Exception classNotFoundException) {
                                classNotFoundException.printStackTrace();
                            }
                            this.setVisible(false);
                            new LoginJFrame();
                        }else{
                            showDialog("前后密码不一致");
                        }
                    }else{
                        showDialog("该用户已存在");
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            }else if(e.getSource() == reset){
            //若重置，就重回注册界面
            this.setVisible(false);
            new RegisterJFrame();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == submit) {
            submit.setIcon(new ImageIcon("image/register/注册按下.png"));
        } else if (e.getSource() == reset) {
            reset.setIcon(new ImageIcon("image/register/重置按下.png"));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == submit) {
            submit.setIcon(new ImageIcon("image/register/注册按钮.png"));
        } else if (e.getSource() == reset) {
            reset.setIcon(new ImageIcon("image/register/重置按钮.png"));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void initView() {
        //添加注册用户名的文本
        JLabel usernameText = new JLabel(new ImageIcon("image/register/注册用户名.png"));
        usernameText.setBounds(85, 135, 80, 20);

        //添加注册用户名的输入框
        username.setBounds(195, 134, 200, 30);

        //添加注册密码的文本
        JLabel passwordText = new JLabel(new ImageIcon("image/register/注册密码.png"));
        passwordText.setBounds(97, 193, 70, 20);

        //添加密码输入框
        password.setBounds(195, 195, 200, 30);

        //添加再次输入密码的文本
        JLabel rePasswordText = new JLabel(new ImageIcon("image/register/再次输入密码.png"));
        rePasswordText.setBounds(64, 255, 95, 20);

        //添加再次输入密码的输入框
        rePassword.setBounds(195, 255, 200, 30);

        //注册的按钮
        submit.setIcon(new ImageIcon("image/register/注册按钮.png"));
        submit.setBounds(123, 310, 128, 47);
        submit.setBorderPainted(false);
        submit.setContentAreaFilled(false);
        submit.addMouseListener(this);

        //重置的按钮
        reset.setIcon(new ImageIcon("image/register/重置按钮.png"));
        reset.setBounds(256, 310, 128, 47);
        reset.setBorderPainted(false);
        reset.setContentAreaFilled(false);
        reset.addMouseListener(this);

        //背景图片
        JLabel background = new JLabel(new ImageIcon("image/register/background.png"));
        background.setBounds(0, 0, 470, 390);

        this.getContentPane().add(usernameText);
        this.getContentPane().add(passwordText);
        this.getContentPane().add(rePasswordText);
        this.getContentPane().add(username);
        this.getContentPane().add(password);
        this.getContentPane().add(rePassword);
        this.getContentPane().add(submit);
        this.getContentPane().add(reset);
        this.getContentPane().add(background);
    }

    private void initFrame() {
        //对自己的界面做一些设置。
        //设置宽高
        setSize(488, 430);
        //设置标题
        setTitle("拼图の注册");
        //取消内部默认布局
        setLayout(null);
        //设置关闭模式
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //设置居中
        setLocationRelativeTo(null);
        //设置置顶
        setAlwaysOnTop(true);
    }

    //只创建一个弹框对象
    JDialog jDialog = new JDialog();
    //因为展示弹框的代码，会被运行多次
    //所以，我们把展示弹框的代码，抽取到一个方法中。以后用到的时候，就不需要写了
    //直接调用就可以了。
    public void showDialog(String content){
        if(!jDialog.isVisible()){
            //把弹框中原来的文字给清空掉。
            jDialog.getContentPane().removeAll();
            JLabel jLabel = new JLabel(content);
            jLabel.setBounds(0,0,200,150);
            jDialog.add(jLabel);
            //给弹框设置大小
            jDialog.setSize(200, 150);
            //要把弹框在设置为顶层 -- 置顶效果
            jDialog.setAlwaysOnTop(true);
            //要让jDialog居中
            jDialog.setLocationRelativeTo(null);
            //让弹框
            jDialog.setModal(true);
            //让jDialog显示出来
            jDialog.setVisible(true);
        }
    }

    //数据库——增
    public void addUser(String usernameInput, String passwordInput) throws Exception{
        String dburl = "jdbc:mysql://localhost:3306/puzzlegame?useSSL=false&characterEncoding=utf-8";
        //数据库的用户名和密码
        String user = "root";
        String password = "root";
        //注册jdbc驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //打开链接--连接mysql
        Connection connection = DriverManager.getConnection(dburl, user, password);
        //写SQL语句
        String sql = "INSERT INTO users VALUES(?,?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, usernameInput);
        pst.setString(2,passwordInput);
        int n = pst.executeUpdate();//使用update
        if(n > 0){
            showDialog("注册成功");
        }else{
            showDialog("注册失败，请重试");
        }
        connection.close();
    }

    //数据库——查
    public boolean findUser(String usernameInput) throws Exception{
        String dburl = "jdbc:mysql://localhost:3306/puzzlegame?useSSL=false&characterEncoding=utf-8";
        //数据库的用户名和密码
        String user = "root";
        String password = "root";
        //注册jdbc驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //打开链接--连接mysql
        Connection connection = DriverManager.getConnection(dburl, user, password);
        //写SQL语句
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, usernameInput);
        ResultSet result = pst.executeQuery();//注意返回值
        while(result.next()){
            String uname = result.getString(1);
            if(usernameInput.equals(uname)){
                return false;
            }
        }
        return true;
    }

}