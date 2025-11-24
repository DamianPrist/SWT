/*
 * Created by JFormDesigner on Mon Nov 24 14:10:47 CST 2025
 */

package Register;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * @author lenovo
 */
public class Register extends JFrame {
    public Register() {
        initComponents();
    }

    //返回按钮点击事件
    private void back_button(ActionEvent e) {
        this.dispose();
    }

    //注册按钮点击事件
    private void register_button(ActionEvent e) {
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        button1 = new JButton();
        textArea1 = new JTextArea();
        textField1 = new JTextField();
        passwordField1 = new JPasswordField();
        passwordField2 = new JPasswordField();
        button2 = new JButton();

        //======== this ========
        setBackground(new Color(240, 248, 255)); // 使用与登录界面相同的背景色
        var contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(240, 248, 255)); // 设置内容面板背景色

        //---- 标题 label1 ----
        label1.setText("注册");
        label1.setFont(new Font("微软雅黑", Font.BOLD, 24));
        label1.setHorizontalAlignment(SwingConstants.CENTER); // 文字居中
        contentPane.add(label1);
        label1.setBounds(200, 40, 150, 50); // 调整位置和大小

        //---- 用户名标签 label2 ----
        label2.setText("用户名");
        label2.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        label2.setHorizontalAlignment(SwingConstants.CENTER); // 文字居中
        contentPane.add(label2);
        label2.setBounds(130, 110, 80, 25); // 调整位置和大小

        //---- 密码标签 label3 ----
        label3.setText("密码");
        label3.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        label3.setHorizontalAlignment(SwingConstants.CENTER); // 文字居中
        contentPane.add(label3);
        label3.setBounds(130, 160, 80, 25); // 调整位置和大小

        //---- 确认密码标签 label4 ----
        label4.setText("确认密码");
        label4.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        label4.setHorizontalAlignment(SwingConstants.CENTER); // 文字居中
        contentPane.add(label4);
        label4.setBounds(130, 210, 80, 25); // 调整位置和大小

        //---- 注册按钮 button1 ----
        button1.setText("注册");
        button1.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button1.setBackground(new Color(70, 130, 180)); // 使用与登录按钮相同的蓝色
        button1.setForeground(Color.WHITE); // 白色文字
        button1.setFocusPainted(false); // 不绘制焦点边框
        contentPane.add(button1);
        button1.setBounds(260, 270, 100, 35); // 调整位置和大小

        // textArea1 如果没有实际用途可以删除，这里保留但隐藏
        textArea1.setVisible(false);
        contentPane.add(textArea1);
        textArea1.setBounds(0, 0, 0, 0);

        //---- 用户名输入框 textField1 ----
        textField1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textField1.setHorizontalAlignment(JTextField.CENTER); // 输入文字居中
        contentPane.add(textField1);
        textField1.setBounds(220, 110, 200, 30); // 调整位置和大小

        //---- 密码输入框 passwordField1 ----
        passwordField1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordField1.setHorizontalAlignment(JTextField.CENTER); // 输入文字居中
        contentPane.add(passwordField1);
        passwordField1.setBounds(220, 160, 200, 30); // 调整位置和大小

        //---- 确认密码输入框 passwordField2 ----
        passwordField2.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordField2.setHorizontalAlignment(JTextField.CENTER); // 输入文字居中
        contentPane.add(passwordField2);
        passwordField2.setBounds(220, 210, 200, 30); // 调整位置和大小

        //---- 返回按钮 button2 ----
        button2.setText("返回");
        button2.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button2.setBackground(new Color(72, 209, 204)); // 使用青色
        button2.setForeground(Color.WHITE); // 白色文字
        button2.setFocusPainted(false); // 不绘制焦点边框
        button2.addActionListener(e -> back_button(e));
        contentPane.add(button2);
        button2.setBounds(140, 270, 100, 35); // 调整位置和大小

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }

        // 设置窗口大小与登录界面一致
        setSize(550, 400);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JButton button1;
    private JTextArea textArea1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JButton button2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}