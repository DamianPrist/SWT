import login.Login;

/**
 * 启动应用程序
 */
public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("正在启动程序...");

            // 实例化并打开登录界面
            Login loginWindow = new Login();
            loginWindow.open();

            // 注意：程序代码会停在上一行，直到登录窗口关闭后才会执行到这里
            System.out.println("程序结束");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
