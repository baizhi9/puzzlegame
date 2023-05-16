package com.myblog.ui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GameJFrame extends JFrame implements KeyListener, ActionListener {
    /*定义一个二维数组，用于打乱图片*/
    int[][] data = new int[4][4];
    /*记录空白图片的二维坐标*/
    int x = 0, y = 0;
    /*记录一个String变量，记录当前图片的路径*/
    String path = "image\\boys\\boy2\\";
    /*定义一个正确的二维数组，winwinwin!*/
    int[][] win = { {1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}};
    /*定义一个计步器*/
    int step = 0;
    /*在成员处定义七个条目按钮对象，才能在动作监听的函数中使用*/
    //创建选项下的条目对象

    JMenuItem boyItem = new JMenuItem("二次元帅哥");
    JMenuItem animalItem = new JMenuItem("可爱の动物");
    JMenuItem sportItem = new JMenuItem("我爱运动");
    JMenuItem replayItem = new JMenuItem("重新游戏");
    JMenuItem reloginItem = new JMenuItem("重新登录");
    JMenuItem closeItem = new JMenuItem("关闭游戏");
    JMenuItem mineItem = new JMenuItem("联系我们");


    /*创建一个游戏的主界面*/
    public GameJFrame(){
        /*初始化界面*/
        initJFrame();

        /*初始化菜单*/
        initJMenuBar();

        /*打乱数组顺序并存放到二维数组*/
        initdata();

        /*初始化图片*/
        initImage();

        /*让界面显示出来，一般放在最后*/
        this.setVisible(true);
    }

    private void initdata() {
        int[] tempArr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        //打乱数组中的顺序
        Random r = new Random();
        for (int i = 0; i < tempArr.length; i++) {
            int index = r.nextInt(tempArr.length);
            //拿到随机索引，与数据进行交换
            int temp = tempArr[i];
            tempArr[i] = tempArr[index];
            tempArr[index] = temp;
        }
        //把数据添加到二维数组，并记录空白图片坐标
        for(int i = 0; i < tempArr.length; i++){
            if(tempArr[i] == 0){
                x = i / 4;
                y = i % 4;
            }
            data[i / 4][i % 4] = tempArr[i];
        }
    }


    private void initImage() {
        //清空
        this.getContentPane().removeAll();

        //判断是否胜利
        if(isvictory()){
            JLabel winLabel = new JLabel(new ImageIcon("image/win.png"));
            winLabel.setBounds(203, 283, 197,73);
            this.getContentPane().add(winLabel);
        }

        //放置计步器
        JLabel stepCount = new JLabel("步数：" + step);
        stepCount.setBounds(50,30,100,20);
        this.getContentPane().add(stepCount);

        //重新放置图片
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int number = data[i][j];
                //创建一个图片对象
                ImageIcon icon = new ImageIcon( path + number + ".jpg");
                //创建一个JLabel对象（管理图片和文字的容器），储存上述图片
                JLabel jLabel = new JLabel(icon);
                //指定图片位置
                jLabel.setBounds(105 * j + 83,105 * i + 134,105,105);
                //给图片添加边框
                jLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                //把JLabel管理容器添加到界面中的隐藏容器里
                this.getContentPane().add(jLabel);
            }
        }

        //添加背景图片（后添加的塞在下方）
        JLabel background = new JLabel(new ImageIcon("image//background.png"));
        background.setBounds(40, 40,508,560);
        this.getContentPane().add(background);

        //刷新
        this.getContentPane().repaint();
    }


    private void initJMenuBar() {
        //创建菜单条
        JMenuBar jMenuBar = new JMenuBar();

        //创建菜单条上的三个菜单选项
        JMenu functionJmenu = new JMenu("功能");
        JMenu aboutJmenu = new JMenu("关于我们");
        JMenu changemenu = new JMenu("更换图片");


        //将条目和选项组合
        changemenu.add(boyItem);
        changemenu.add(animalItem);
        changemenu.add(sportItem);

        functionJmenu.add(changemenu);//功能选项也可以嵌套更换图片选项
        functionJmenu.add(replayItem);
        functionJmenu.add(reloginItem);
        functionJmenu.add(closeItem);

        aboutJmenu.add(mineItem);

        //给7个条目添加事件
        boyItem.addActionListener(this);
        animalItem.addActionListener(this);
        sportItem.addActionListener(this);
        replayItem.addActionListener(this);
        reloginItem.addActionListener(this);
        closeItem.addActionListener(this);
        mineItem.addActionListener(this);

        //将选项和菜单条组合
        jMenuBar.add(functionJmenu);
        jMenuBar.add(aboutJmenu);

        //将菜单条和主界面组合
        this.setJMenuBar(jMenuBar);
    }


    private void initJFrame() {
        //设置界面的宽高
        this.setSize(603,680);
        //设置界面的标题
        this.setTitle("淡季拼图小游戏 v1.0");
        //设置界面指定（写代码时界面不会被IDEA覆盖）
        this.setAlwaysOnTop(true);
        //设置界面居中
        this.setLocationRelativeTo(null);
        //设置关闭模式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //取消默认的居中布局方式，方可使用XY轴设置坐标
        this.setLayout(null);
        //给整个界面添加键盘监听事件
        this.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //按下A不松，显示完整图片
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == 65){
            //先清空
            this.getContentPane().removeAll();
            //再加载完整的图片
            JLabel all = new JLabel(new ImageIcon(path + "all.jpg"));
            all.setBounds(83,134,420,420);
            this.getContentPane().add(all);
            //最后加载背景图片
            JLabel background = new JLabel(new ImageIcon("image//background.png"));
            background.setBounds(40, 40,508,560);
            this.getContentPane().add(background);
            //刷新
            this.getContentPane().repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //判断游戏是否胜利，如果胜利，就直接结束此方法
        if(isvictory()){
            return;
        }
        int code = e.getKeyCode();
        //左：37 上：38 右：39 下：40
        if(code == 37){
            //向左移动：把空白方块右边的图片和空白方块交换
            if(y == 3){
                return;
            }
            data[x][y] = data[x][y + 1];
            data[x][y + 1] = 0;
            y++;
            step++;
            initImage();
        }else if(code == 38){
            //向上移动：把空白方块下面的图片往上移动，即把它们交换
            if(x == 3){
                return;//空白图片在最下方，无法移动，故直接结束方法
            }
            data[x][y] = data[x + 1][y];
            data[x + 1][y] = 0;
            x++;//更新空白方块的x坐标
            step++;
            //刷新图片
            initImage();
        }else if(code == 39){
            //向右移动：把空白方块左边的图片和空白方块交换
            if(y == 0){
                return;
            }
            data[x][y] = data[x][y - 1];
            data[x][y - 1] = 0;
            y--;
            step++;
            initImage();
        }else if(code == 40){
            //向下移动：把空白方块上边的图片和空白方块交换
            if(x == 0){
                return;
            }
            data[x][y] = data[x - 1][y];
            data[x - 1][y] = 0;
            x--;
            step++;
            initImage();
        }else if(code == 65){
            //A按完后，恢复原本拼图形态
            initImage();
        }else if(code == 87){
            //按W时，直接胜利
            data = new int[][]{
                    {1, 2, 3, 4},
                    {5, 6, 7, 8},
                    {9, 10, 11, 12},
                    {13, 14, 15, 0}
            };
            initImage();
        }
    }

    public boolean isvictory() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < win.length; j++) {
                if(data[i][j] != win[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //获取当前被点击的条目对象
        Object obj = e.getSource();
        //判断
       if(obj == boyItem){
           //更换帅哥图片的步骤：随机选择图片、修改path记录的地址值、重开一把
           Random r = new Random();
           int index = r.nextInt(8) + 1;
           path = "image\\boys\\boy" + index + "\\";
           step = 0;
           initdata();
           initImage();
        }else if(obj == animalItem){
           //更换动物图片的步骤：随机选择图片、修改path记录的地址值、重开一把
           Random r = new Random();
           int index = r.nextInt(8) + 1;
           path = "image\\animal\\animal" + index + "\\";
           step = 0;
           initdata();
           initImage();
        }else if(obj == sportItem){
           //更换运动图片的步骤：随机选择图片、修改path记录的地址值、重开一把
           Random r = new Random();
           int index = r.nextInt(10) + 1;
           path = "image\\sport\\sport" + index + "\\";
           step = 0;
           initdata();
           initImage();
        }else if(obj == replayItem){
            //重新游戏的步骤：步数清零、打乱数据、加载图片
            step = 0;
            initdata();
            initImage();
        }else if(obj == reloginItem){
            //重新登录的步骤：关闭当前的游戏界面、打开登录界面
            this.setVisible(false);
            new LoginJFrame();
        }else if(obj == closeItem){
            //关闭游戏的步骤：关闭当前的游戏界面
            System.exit(0);
        }else if(obj == mineItem){
            //打开vx号的步骤：打开弹窗、放上图片
            //创建一个弹窗对象
            JDialog jDialog = new JDialog();
            //创建一个JLabel管理图片
            JLabel mine = new JLabel(new ImageIcon("image/mine.png"));
            mine.setBounds(0,0,258,258);
            //把图片加载到弹窗
            jDialog.getContentPane().add(mine);
            //给弹窗设置大小并置顶和居中
            jDialog.setSize(344,344);
            jDialog.setAlwaysOnTop(true);
            jDialog.setLocationRelativeTo(null);
            //弹窗不关闭则无法操作下面的界面，并显示出来
            jDialog.setModal(true);
            jDialog.setVisible(true);
        }
    }
}
