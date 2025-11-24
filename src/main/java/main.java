import Login.Login;

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            Login login = new Login();
            login.setVisible(true);
        });
    }
}
