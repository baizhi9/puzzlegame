package com.myblog.ui;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;

public class LoginJFrame extends JFrame implements MouseListener {


    JButton login = new JButton();
    JButton register = new JButton();

    JTextField username = new JTextField();
    //JTextField password = new JTextField();
    JPasswordField password = new JPasswordField();
    JTextField code = new JTextField();

    //正确的验证码
    JLabel rightCode = new JLabel();


    public LoginJFrame() {
        //初始化界面
        initJFrame();

        //在这个界面中添加内容
        initView();


        //让当前界面显示出来
        this.setVisible(true);
    }


    public void initView() {
        //1. 添加用户名文字
        JLabel usernameText = new JLabel(new ImageIcon("image/login/用户名.png"));
        usernameText.setBounds(116, 135, 47, 17);
        this.getContentPane().add(usernameText);

        //2.添加用户名输入框

        username.setBounds(195, 134, 200, 30);
        this.getContentPane().add(username);

        //3.添加密码文字
        JLabel passwordText = new JLabel(new ImageIcon("image/login/密码.png"));
        passwordText.setBounds(130, 195, 32, 16);
        this.getContentPane().add(passwordText);

        //4.密码输入框
        password.setBounds(195, 195, 200, 30);
        this.getContentPane().add(password);


        //验证码提示
        JLabel codeText = new JLabel(new ImageIcon("image/login/验证码.png"));
        codeText.setBounds(133, 256, 50, 30);
        this.getContentPane().add(codeText);

        //验证码的输入框
        code.setBounds(195, 256, 100, 30);
        this.getContentPane().add(code);


        String codeStr = CodeUtil.getCode();
        //设置内容
        rightCode.setText(codeStr);
        //绑定鼠标事件
        rightCode.addMouseListener(this);
        //位置和宽高
        rightCode.setBounds(300, 256, 50, 30);
        //添加到界面
        this.getContentPane().add(rightCode);

        //5.添加登录按钮
        login.setBounds(123, 310, 128, 47);
        login.setIcon(new ImageIcon("image/login/登录按钮.png"));
        //去除按钮的边框
        login.setBorderPainted(false);
        //去除按钮的背景
        login.setContentAreaFilled(false);
        //给登录按钮绑定鼠标事件
        login.addMouseListener(this);
        this.getContentPane().add(login);

        //6.添加注册按钮
        register.setBounds(256, 310, 128, 47);
        register.setIcon(new ImageIcon("image/login/注册按钮.png"));
        //去除按钮的边框
        register.setBorderPainted(false);
        //去除按钮的背景
        register.setContentAreaFilled(false);
        //给注册按钮绑定鼠标事件
        register.addMouseListener(this);
        this.getContentPane().add(register);


        //7.添加背景图片
        JLabel background = new JLabel(new ImageIcon("image/login/background.png"));
        background.setBounds(0, 0, 470, 390);
        this.getContentPane().add(background);

    }


    public void initJFrame() {
        this.setSize(488, 430);//设置宽高
        this.setTitle("拼图の登录");//设置标题
        this.setLocationRelativeTo(null);//居中
        this.setAlwaysOnTop(true);//置顶
        this.setLayout(null);//取消内部默认布局
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    //点击
    @Override
    public void mouseClicked(MouseEvent e) {
        //点登录按钮
        if (e.getSource() == login) {
            /*登录时，先判断用户名、密码、验证码是否为空。如果不为空，则继续
            再判断验证码输入是否正确。如果正确，则继续
            再判断用户是否存在。如果存在，则可以登录
            再判断用户的密码是否正确，如果正确，则开始玩游戏*/

            //获取两个文本输入框中的内容
            String usernameInput = username.getText();
            String passwordInput = password.getText();
            //获取用户输入的验证码
            String codeInput = code.getText();

            //用ans变量存储正确与否
            int ans = 0;
            try {
                ans = findUser(usernameInput, passwordInput);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if (codeInput.length() == 0) {
                showJDialog("验证码不能为空");
            } else if (usernameInput.length() == 0 || passwordInput.length() == 0) {
                //校验用户名和密码是否为空
                //调用showJDialog方法并展示弹框
                showJDialog("用户名或者密码为空");
            } else if (!codeInput.equalsIgnoreCase(rightCode.getText())) {
                showJDialog("验证码输入错误");
            } else if (ans == 1) {
                //正确，可以开始游戏
                this.setVisible(false);
                new GameJFrame();
            } else if (ans == 0) {
                showJDialog("用户名或者密码错误");
            }
            //点注册按钮
        } else if (e.getSource() == register) {
            this.setVisible(false);
            new RegisterJFrame();
        }
        //点重新获取验证码按钮
        else if (e.getSource() == rightCode) {
            String code = CodeUtil.getCode();
            rightCode.setText(code);
        }
    }


    public void showJDialog(String content) {
        //创建一个弹框对象
        JDialog jDialog = new JDialog();
        //给弹框设置大小
        jDialog.setSize(200, 150);
        //让弹框置顶
        jDialog.setAlwaysOnTop(true);
        //让弹框居中
        jDialog.setLocationRelativeTo(null);
        //弹框不关闭永远无法操作下面的界面
        jDialog.setModal(true);

        //创建Jlabel对象管理文字并添加到弹框当中
        JLabel warning = new JLabel(content);
        warning.setBounds(0, 0, 200, 150);
        jDialog.getContentPane().add(warning);

        //让弹框展示出来
        jDialog.setVisible(true);
    }

    //按下不松
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == login) {
            login.setIcon(new ImageIcon("image/login/登录按下.png"));
        } else if (e.getSource() == register) {
            register.setIcon(new ImageIcon("image/login/注册按下.png"));
        }
    }


    //松开按钮
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == login) {
            login.setIcon(new ImageIcon("image/login/登录按钮.png"));
        } else if (e.getSource() == register) {
            register.setIcon(new ImageIcon("image/login/注册按钮.png"));
        }
    }

    //鼠标划入
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    //鼠标划出
    @Override
    public void mouseExited(MouseEvent e) {

    }

    //数据库——查
    public int findUser(String usernameInput, String passwordInput) throws Exception {
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
        while (result.next()) {
            String upass = result.getString(2);
            if (passwordInput.equals(upass)) {//密码正确
                return 1;
            }
        }
        return 0;
    }

}
/*
import javax.swing.*;
import java.awt.*;

public class LoginJFrame extends JFrame {
    //创建一个登录界面
    public LoginJFrame(){
        //设置界面的宽高
        this.setSize(488,430);
        //设置界面的标题
        this.setTitle("拼图の登录");
        //设置界面指定（写代码时界面不会被IDEA覆盖）
        this.setAlwaysOnTop(true);
        //设置界面居中
        this.setLocationRelativeTo(null);
        //设置关闭模式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //让界面显示出来，一般放在最后
        this.setVisible(true);
    }

}
*/
