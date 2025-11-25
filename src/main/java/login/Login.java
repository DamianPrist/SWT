package login;

import DAO.UserDAO;
import Entity.User;
import MainInterface.MainInterface;
import connect.DatabaseConnection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import register.Register;

/**
 * SWT login 界面
 * @author SUNRISE / Gemini / echo-escape
 */
public class Login {

    protected Shell shell;
    private Text textUser;
    private Text textPassword;
    private Font fontInput;

    // 用户数据访问对象
    private UserDAO userDAO = new UserDAO();
    private User currentUser;

    // 美化用的颜色资源
    private Color primaryColor;
    private Color secondaryColor;
    private Color accentColor;
    private Color bgColor;
    private Color textFieldBgColor;

    /**
     * 打开窗口
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();

        // 窗口居中计算
        Rectangle screenSize = display.getPrimaryMonitor().getBounds();
        Rectangle shellSize = shell.getBounds();
        shell.setLocation(
                (screenSize.width - shellSize.width) / 2,
                (screenSize.height - shellSize.height) / 2
        );

        shell.open();
        shell.layout();

        // SWT 事件循环
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * 创建窗口内容
     */
    protected void createContents() {
        shell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN);
        shell.setSize(600, 500); // 稍微增大窗口尺寸
        shell.setText("用户登录");

        // 初始化颜色方案 - 现代浅色主题
        Display display = Display.getCurrent();
        primaryColor = new Color(display, 74, 144, 226);   // 主色调蓝色
        secondaryColor = new Color(display, 250, 250, 252); // 次要背景色
        accentColor = new Color(display, 102, 187, 106);   // 强调色绿色
        bgColor = new Color(display, 248, 250, 252);       // 主背景色
        textFieldBgColor = new Color(display, 255, 255, 255); // 输入框背景

        shell.setBackground(bgColor);

        // 监听器用于释放资源
        shell.addDisposeListener(e -> {
            primaryColor.dispose();
            secondaryColor.dispose();
            accentColor.dispose();
            bgColor.dispose();
            textFieldBgColor.dispose();
        });

        // 创建渐变背景的Composite（模拟现代卡片效果）
        Composite headerComposite = new Composite(shell, SWT.NONE);
        headerComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
        headerComposite.setBackground(primaryColor);
        headerComposite.setBounds(0, 0, 600, 120);

        // 标题 Label - 增加高度确保字体完整显示
        Label labelTitle = new Label(headerComposite, SWT.CENTER);
        labelTitle.setText("欢迎登录");
        labelTitle.setForeground(new Color(display, 255, 255, 255)); // 白色文字
        Font fontTitle = new Font(display, "微软雅黑", 24, SWT.BOLD); // 减小字体大小
        labelTitle.setFont(fontTitle);
        labelTitle.setBounds(0, 25, 600, 70); // 调整位置和高度
        labelTitle.setBackground(primaryColor);
        labelTitle.addDisposeListener(e -> fontTitle.dispose());

        // 主内容区域
        Composite mainComposite = new Composite(shell, SWT.NONE);
        mainComposite.setBackground(bgColor);
        mainComposite.setBounds(50, 150, 500, 280);

        // 账户 Label - 增加高度
        Label labelUser = new Label(mainComposite, SWT.NONE);
        labelUser.setText("用户名");
        labelUser.setBackground(bgColor);
        Font fontLabel = new Font(display, "微软雅黑", 12, SWT.NORMAL);
        labelUser.setFont(fontLabel);
        labelUser.setBounds(50, 40, 80, 30); // 增加高度到30

        // 用户名输入框 - 增加高度
        textUser = new Text(mainComposite, SWT.BORDER | SWT.SINGLE);
        textUser.setFont(new Font(display, "微软雅黑", 11, SWT.NORMAL));
        textUser.setBackground(textFieldBgColor);
        textUser.setBounds(140, 35, 300, 40); // 增加高度到40

        // 密码 Label - 增加高度
        Label labelPwd = new Label(mainComposite, SWT.NONE);
        labelPwd.setText("密码");
        labelPwd.setBackground(bgColor);
        labelPwd.setFont(fontLabel);
        labelPwd.setBounds(50, 100, 80, 30); // 增加高度到30

        // 密码输入框 - 增加高度
        textPassword = new Text(mainComposite, SWT.BORDER | SWT.PASSWORD | SWT.SINGLE);
        textPassword.setFont(new Font(display, "微软雅黑", 11, SWT.NORMAL));
        textPassword.setBackground(textFieldBgColor);
        textPassword.setBounds(140, 95, 300, 40); // 增加高度到40

        // 按钮容器
        Composite buttonComposite = new Composite(mainComposite, SWT.NONE);
        buttonComposite.setBackground(bgColor);
        buttonComposite.setBounds(100, 170, 300, 50); // 调整位置

        // 登录按钮 - 增加高度
        Button btnLogin = new Button(buttonComposite, SWT.PUSH);
        btnLogin.setText("登录");
        btnLogin.setFont(new Font(display, "微软雅黑", 12, SWT.BOLD));
        btnLogin.setBackground(primaryColor);
        btnLogin.setForeground(new Color(display, 255, 255, 255));
        btnLogin.setBounds(0, 0, 140, 45); // 增加高度到45

        // 添加鼠标悬停效果
        btnLogin.addListener(SWT.MouseEnter, e -> {
            btnLogin.setBackground(new Color(display, 56, 124, 206));
        });
        btnLogin.addListener(SWT.MouseExit, e -> {
            btnLogin.setBackground(primaryColor);
        });

        // 登录逻辑
        btnLogin.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleLogin();
            }
        });

        // 注册按钮 - 增加高度
        Button btnRegister = new Button(buttonComposite, SWT.PUSH);
        btnRegister.setText("注册账号");
        btnRegister.setFont(new Font(display, "微软雅黑", 12, SWT.BOLD));
        btnRegister.setBackground(secondaryColor);
        btnRegister.setForeground(primaryColor);
        btnRegister.setBounds(160, 0, 140, 45); // 增加高度到45

        // 添加鼠标悬停效果
        btnRegister.addListener(SWT.MouseEnter, e -> {
            btnRegister.setBackground(new Color(display, 240, 242, 245));
        });
        btnRegister.addListener(SWT.MouseExit, e -> {
            btnRegister.setBackground(secondaryColor);
        });

        // 注册逻辑
        btnRegister.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleRegister();
            }
        });

        // 底部信息 - 增加高度
        Label footerLabel = new Label(shell, SWT.CENTER);
        footerLabel.setText("© 2024 系统登录界面");
        footerLabel.setForeground(new Color(display, 153, 153, 153));
        footerLabel.setFont(new Font(display, "微软雅黑", 10, SWT.NORMAL)); // 调整字体大小
        footerLabel.setBackground(bgColor);
        footerLabel.setBounds(0, 450, 600, 25); // 增加高度到25

        // 统一释放字体资源
        shell.addDisposeListener(e -> {
            fontLabel.dispose();
            if (fontInput != null) fontInput.dispose();
        });
    }

    /**
     * 处理登录事件
     */
    private void handleLogin() {
        String username = textUser.getText().trim();
        String password = textPassword.getText();

        // 输入验证
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("错误", "请输入用户名和密码！", SWT.ICON_ERROR);
            return;
        }

        try {
            currentUser = userDAO.validateUser(username, password);
            if (currentUser != null) {
                showMessage("成功", "登录成功！", SWT.ICON_INFORMATION);
                // 关闭登录窗口
                shell.dispose();
                // 启动MainInterface并传递用户信息
                MainInterface mainInterface = new MainInterface();
                mainInterface.setCurrentUser(currentUser);
                mainInterface.open();
            } else {
                showMessage("失败", "登录失败，请检查用户名和密码！", SWT.ICON_ERROR);
            }
        } catch (Exception e) {
            showMessage("错误", "登录过程中出现错误：" + e.getMessage(), SWT.ICON_ERROR);
        }
    }

    /**
     * 获取当前登录用户
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * 处理注册事件
     */
    private void handleRegister() {
        Register register = new Register();
        register.open();
    }

    /**
     * 辅助方法：显示消息弹窗
     */
    private void showMessage(String title, String message, int iconStyle) {
        MessageBox messageBox = new MessageBox(shell, iconStyle | SWT.OK);
        messageBox.setText(title);
        messageBox.setMessage(message);
        messageBox.open();
    }
}