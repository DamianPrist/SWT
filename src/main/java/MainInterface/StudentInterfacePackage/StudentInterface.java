package MainInterface.StudentInterfacePackage;

import Entity.Student;
import login.Login;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * 学生主界面
 */
public class StudentInterface {

    protected Shell shell;
    private Student currentStudent;

    // 颜色资源
    private Color primaryColor;
    private Color secondaryColor;
    private Color accentColor;
    private Color dangerColor;
    private Color bgColor;
    private Color sidebarColor;
    private Color contentBgColor;

    // 界面组件
    private Composite contentArea; // 右侧内容区域
    private Label contentTitle;    // 内容区域标题

    /**
     * 打开学生主界面
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();

        // 窗口居中
        Rectangle screenSize = display.getPrimaryMonitor().getBounds();
        Rectangle shellSize = shell.getBounds();
        shell.setLocation(
                (screenSize.width - shellSize.width) / 2,
                (screenSize.height - shellSize.height) / 2
        );

        shell.open();
        shell.layout();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * 创建界面内容
     */
    protected void createContents() {
        shell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN);
        shell.setSize(1200, 800);
        shell.setText("学生成绩管理系统 - 学生端");

        // 初始化颜色方案
        Display display = Display.getCurrent();
        primaryColor = new Color(display, 74, 144, 226);
        secondaryColor = new Color(display, 250, 250, 252);
        accentColor = new Color(display, 102, 187, 106);
        dangerColor = new Color(display, 239, 83, 80);
        bgColor = new Color(display, 248, 250, 252);
        sidebarColor = new Color(display, 240, 242, 245);
        contentBgColor = new Color(display, 255, 255, 255);

        shell.setBackground(bgColor);

        // 设置主窗口布局
        GridLayout shellLayout = new GridLayout(1, false);
        shellLayout.marginWidth = 0;
        shellLayout.marginHeight = 0;
        shell.setLayout(shellLayout);

        // 资源释放监听器
        shell.addDisposeListener(e -> disposeResources());

        // 创建头部区域
        createHeaderArea();

        // 创建主体区域（侧边栏+内容区域）
        createMainArea();

        // 创建底部区域
        createFooterArea();
    }

    /**
     * 创建头部区域
     */
    private void createHeaderArea() {
        Composite headerComposite = new Composite(shell, SWT.NONE);
        headerComposite.setBackground(primaryColor);
        GridData headerData = new GridData(SWT.FILL, SWT.TOP, true, false);
        headerData.heightHint = 140;
        headerComposite.setLayoutData(headerData);

        GridLayout headerLayout = new GridLayout(1, true);
        headerLayout.marginHeight = 20;
        headerLayout.marginWidth = 0;
        headerLayout.verticalSpacing = 0;
        headerComposite.setLayout(headerLayout);

        // 系统标题
        Label titleLabel = new Label(headerComposite, SWT.CENTER);
        titleLabel.setText("学生成绩管理系统 - 学生端");
        titleLabel.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
        Font titleFont = new Font(Display.getCurrent(), "微软雅黑", 24, SWT.BOLD);
        titleLabel.setFont(titleFont);
        titleLabel.setBackground(primaryColor);

        GridData titleData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        titleLabel.setLayoutData(titleData);

        // 显示当前登录学生信息
        Label studentInfoLabel = new Label(headerComposite, SWT.CENTER);
        if (currentStudent != null) {
            studentInfoLabel.setText("欢迎您：" + currentStudent.getStudentName() +
                    " (" + currentStudent.getStudentId() + ")");
        } else {
            studentInfoLabel.setText("欢迎您，学生用户");
        }
        studentInfoLabel.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
        Font infoFont = new Font(Display.getCurrent(), "微软雅黑", 14, SWT.NORMAL);
        studentInfoLabel.setFont(infoFont);
        studentInfoLabel.setBackground(primaryColor);

        GridData infoData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        infoData.verticalIndent = 10;
        studentInfoLabel.setLayoutData(infoData);

        titleLabel.addDisposeListener(e -> {
            titleFont.dispose();
            infoFont.dispose();
        });
    }

    /**
     * 创建主体区域
     */
    private void createMainArea() {
        Composite mainComposite = new Composite(shell, SWT.NONE);
        mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        mainComposite.setBackground(bgColor);

        GridLayout mainLayout = new GridLayout(2, false);
        mainLayout.marginWidth = 0;
        mainLayout.marginHeight = 0;
        mainLayout.horizontalSpacing = 0;
        mainComposite.setLayout(mainLayout);

        // 创建侧边栏
        createSidebar(mainComposite);

        // 创建内容区域
        createContentArea(mainComposite);
    }

    /**
     * 创建侧边栏 - 学生端只有查看成绩功能
     */
    private void createSidebar(Composite parent) {
        Composite sidebarComposite = new Composite(parent, SWT.NONE);
        sidebarComposite.setBackground(sidebarColor);
        GridData sidebarData = new GridData(SWT.LEFT, SWT.FILL, false, true);
        sidebarData.widthHint = 250;
        sidebarComposite.setLayoutData(sidebarData);

        GridLayout sidebarLayout = new GridLayout(1, false);
        sidebarLayout.marginWidth = 20;
        sidebarLayout.marginHeight = 50;
        sidebarLayout.verticalSpacing = 25;
        sidebarComposite.setLayout(sidebarLayout);

        // 查看我的成绩按钮
        Button btnMyGrade = createSidebarButton(sidebarComposite, "查看我的成绩", primaryColor);
        btnMyGrade.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                showMyGrade();
            }
        });

        // 添加空白区域
        Label spacer = new Label(sidebarComposite, SWT.NONE);
        spacer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        spacer.setBackground(sidebarColor);

        // 退出登录按钮
        Button btnLogout = createSidebarButton(sidebarComposite, "退出登录", dangerColor);
        btnLogout.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleLogout();
            }
        });
    }

    /**
     * 创建侧边栏按钮
     */
    private Button createSidebarButton(Composite parent, String text, Color bgColor) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(text);
        button.setFont(new Font(Display.getCurrent(), "微软雅黑", 14, SWT.BOLD));
        button.setBackground(bgColor);
        button.setForeground(new Color(Display.getCurrent(), 255, 255, 255));

        GridData buttonData = new GridData(SWT.FILL, SWT.TOP, true, false);
        buttonData.heightHint = 70;
        button.setLayoutData(buttonData);

        // 添加鼠标悬停效果
        button.addListener(SWT.MouseEnter, e -> {
            Color hoverColor = darkenColor(bgColor, 0.85f);
            button.setBackground(hoverColor);
        });
        button.addListener(SWT.MouseExit, e -> {
            button.setBackground(bgColor);
        });

        return button;
    }

    /**
     * 创建内容区域
     */
    private void createContentArea(Composite parent) {
        Composite contentComposite = new Composite(parent, SWT.NONE);
        contentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        contentComposite.setBackground(contentBgColor);

        GridLayout contentLayout = new GridLayout(1, false);
        contentLayout.marginWidth = 50;
        contentLayout.marginHeight = 50;
        contentComposite.setLayout(contentLayout);

        // 内容区域标题
        contentTitle = new Label(contentComposite, SWT.CENTER);
        contentTitle.setText("欢迎使用学生成绩管理系统");
        contentTitle.setForeground(primaryColor);
        Font contentTitleFont = new Font(Display.getCurrent(), "微软雅黑", 20, SWT.BOLD);
        contentTitle.setFont(contentTitleFont);
        contentTitle.setBackground(contentBgColor);
        GridData titleData = new GridData(SWT.FILL, SWT.TOP, true, false);
        titleData.heightHint = 65;
        contentTitle.setLayoutData(titleData);
        contentTitle.addDisposeListener(e -> contentTitleFont.dispose());

        // 内容显示区域
        contentArea = new Composite(contentComposite, SWT.NONE);
        contentArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        contentArea.setBackground(contentBgColor);

        // 内容区域使用FillLayout
        FillLayout contentAreaLayout = new FillLayout(SWT.VERTICAL);
        contentAreaLayout.marginHeight = 30;
        contentArea.setLayout(contentAreaLayout);

        // 显示欢迎内容
        showWelcomeContent();
    }

    /**
     * 创建底部区域
     */
    private void createFooterArea() {
        Label footerLabel = new Label(shell, SWT.CENTER);
        footerLabel.setText("© 2025 学生成绩管理系统 - 学生端");
        footerLabel.setForeground(new Color(Display.getCurrent(), 153, 153, 153));
        footerLabel.setFont(new Font(Display.getCurrent(), "微软雅黑", 13, SWT.NORMAL));
        footerLabel.setBackground(bgColor);
        GridData footerData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
        footerData.heightHint = 50;
        footerLabel.setLayoutData(footerData);
    }

    /**
     * 颜色变深效果
     */
    private Color darkenColor(Color color, float factor) {
        Display display = (Display) color.getDevice();
        int r = Math.max(0, (int)(color.getRed() * factor));
        int g = Math.max(0, (int)(color.getGreen() * factor));
        int b = Math.max(0, (int)(color.getBlue() * factor));
        return new Color(display, r, g, b);
    }

    /**
     * 显示欢迎内容
     */
    private void showWelcomeContent() {
        // 清除内容区域
        for (Control control : contentArea.getChildren()) {
            control.dispose();
        }

        // 创建欢迎标签
        Label welcomeLabel = new Label(contentArea, SWT.WRAP | SWT.CENTER);
        welcomeLabel.setText("欢迎使用学生成绩管理系统！\n\n" +
                "您当前以学生身份登录。\n\n" +
                "• 查看我的成绩 - 查看您的各科成绩和总成绩\n" +
                "• 退出登录 - 返回登录界面");
        welcomeLabel.setFont(new Font(contentArea.getDisplay(), "微软雅黑", 18, SWT.NORMAL));
        welcomeLabel.setBackground(contentBgColor);

        contentArea.layout();
    }

    /**
     * 显示我的成绩界面
     */
    private void showMyGrade() {
        contentTitle.setText("我的成绩");

        // 清除内容区域
        for (Control control : contentArea.getChildren()) {
            control.dispose();
        }

        // 创建学生成绩界面
        new ShowStudentGrade(contentArea, currentStudent);

        contentArea.layout();
    }

    /**
     * 处理退出登录
     */
    private void handleLogout() {
        MessageBox confirmBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
        confirmBox.setText("确认退出");
        confirmBox.setMessage("确定要退出系统吗？");

        int result = confirmBox.open();
        if (result == SWT.YES) {
            shell.dispose();
            // 返回登录界面
            new Login().open();
        }
    }

    /**
     * 设置当前学生
     */
    public void setCurrentStudent(Student student) {
        this.currentStudent = student;
    }

    /**
     * 释放资源
     */
    private void disposeResources() {
        if (primaryColor != null) primaryColor.dispose();
        if (secondaryColor != null) secondaryColor.dispose();
        if (accentColor != null) accentColor.dispose();
        if (dangerColor != null) dangerColor.dispose();
        if (bgColor != null) bgColor.dispose();
        if (sidebarColor != null) sidebarColor.dispose();
        if (contentBgColor != null) contentBgColor.dispose();
    }
}