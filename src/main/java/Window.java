import javax.swing.*;

public class Window extends JFrame {


    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 500;

    public Window() {
        this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        this.setLayout(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        Main mainScene  = new Main();
        this.add(mainScene);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Window window = new Window();
    }

}
