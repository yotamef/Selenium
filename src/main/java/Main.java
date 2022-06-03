import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;



public class Main extends JPanel {

    private String phoneInput;
    private String textInput;
    private JLabel messageFromOther;
    private JLabel statusLabel;
    private JLabel sent;

    public Main() {
        this.setBounds(0,0,Window.WINDOW_WIDTH,Window.WINDOW_HEIGHT);
        this.setDoubleBuffered(true);
        this.setLayout(null);
        this.setVisible(true);

        JButton start = new JButton("start");
        start.setBounds(Window.WINDOW_WIDTH/2-60,Window.WINDOW_HEIGHT/2-BUTTON_SIZE,BUTTON_SIZE,BUTTON_SIZE);
        start.setVisible(true);
        start.addActionListener((event) -> {
            new Thread(() -> {
                insertPhoneAndText();
            }).start();
            start.setVisible(false);
        });
        this.add(start);


    }
    public static final int BUTTON_SIZE = 100;

    public void connect() {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\Yotam\\Downloads\\chromedriver_win32\\chromedriver.exe");

        ChromeDriver driver = new ChromeDriver();
        String link = "https://web.whatsapp.com/send?phone="+phoneInput;
        driver.get(link);
        driver.manage().window().maximize();

        boolean connect = false;
        while (!connect) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            try {
                WebElement element = driver.findElement(By.id("side"));
                connect = true;
            } catch (Exception e) {
                System.out.println("no element");
            }

        }
        System.out.println("done");

        try {
            WebElement type = driver.findElement(By.cssSelector("div[title=\"הקלדת ההודעה\"]"));
            System.out.println("succeed");
            type.sendKeys(textInput);
            WebElement send = driver.findElement(By.cssSelector("span[data-testid=\"send\"]"));
            send.click();

            boolean sentBool = false;
            while (!sentBool) {
                Thread.sleep(1000);
                try {
                    WebElement hasSent = driver.findElement(By.cssSelector("span[data-testid=\"msg-time\"]"));
                    Thread.sleep(100);
                } catch (Exception e) {
                    sentBool = true;
                }
            }

            sent = new JLabel("the message was sent successfully");
            sent.setBounds(Window.WINDOW_WIDTH/2-50,Window.WINDOW_HEIGHT/2-50,LABEL_TEXT_WIDTH,LABEL_HEIGHT);
            sent.setVisible(true);
            this.add(sent);
            repaint();

            boolean didntRead = true;
            statusLabel = new JLabel();
            statusLabel.setBounds(Window.WINDOW_WIDTH / 2-50, Window.WINDOW_HEIGHT / 2 -50 + LABEL_HEIGHT, LABEL_TEXT_WIDTH, LABEL_HEIGHT);
            statusLabel.setVisible(true);
            this.add(statusLabel);

            while (didntRead) {
                Thread.sleep(1000);

                ArrayList <WebElement> statuses = (ArrayList<WebElement>) driver.findElements(By.cssSelector("span[data-testid='msg-dblcheck']"));
                WebElement relevantStatus = statuses.get(statuses.size()-1);
                System.out.println(relevantStatus.getAttribute("aria-label"));
                statusLabel.setText("message status: " + relevantStatus.getAttribute("aria-label"));
                repaint();

                if (relevantStatus.getAttribute("aria-label").equals(" נקראה ")) {
                    didntRead = false;
                }
                try {
                    Thread.sleep(10);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }


            }

            boolean didntSendBack = true;
            while (didntSendBack) {
                try {
                    ArrayList<WebElement> messages = (ArrayList<WebElement>) driver. findElements(By.cssSelector("div[class='Nm1g1 _22AX6']"));
                    WebElement relevant = messages.get(messages.size() - 1);
                    if (!relevant.findElements(By.tagName("span")).get(0).getAttribute("aria-label").equals("את/ה:")) {
                        didntSendBack = false;
                        ArrayList<WebElement> messagesBack = (ArrayList<WebElement>) driver.findElements(By.cssSelector("span[class='i0jNr selectable-text copyable-text']"));
                        WebElement messageBack = messagesBack.get(messagesBack.size()-1);
                        System.out.println(messageBack.findElement(By.tagName("span")).getText());

                        messageFromOther = new JLabel("response: " +messageBack.findElement(By.tagName("span")).getText());
                        messageFromOther.setBounds(Window.WINDOW_WIDTH / 2-50, Window.WINDOW_HEIGHT / 2 -50 + 2*LABEL_HEIGHT, LABEL_TEXT_WIDTH, LABEL_HEIGHT);
                        messageFromOther.setVisible(true);
                        this.add(messageFromOther);
                        repaint();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {
                    Thread.sleep(10000);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            driver.close();


        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }

    }

    public static final int LABEL_PHONE_WIDTH = 120;
    public static final int LABEL_TEXT_WIDTH = 300;
    public static final int LABEL_HEIGHT = 20;


    public void insertPhoneAndText() {
        JLabel phone = new JLabel("phone number: ");
        phone.setBounds(0,0,LABEL_PHONE_WIDTH,LABEL_HEIGHT);
        phone.setVisible(true);
        this.add(phone);
        JLabel text = new JLabel("text: ");
        text.setBounds(0,LABEL_HEIGHT,LABEL_PHONE_WIDTH,LABEL_HEIGHT);
        text.setVisible(true);
        this.add(text);
        JTextField userPhoneNumber = new JTextField();
        userPhoneNumber.setBounds(LABEL_PHONE_WIDTH,0,LABEL_PHONE_WIDTH,LABEL_HEIGHT);
        userPhoneNumber.setVisible(true);
        this.add(userPhoneNumber);
        JTextField userText = new JTextField();
        userText.setBounds(LABEL_PHONE_WIDTH,LABEL_HEIGHT,LABEL_TEXT_WIDTH,LABEL_HEIGHT);
        userText.setVisible(true);
        this.add(userText);

        JButton start = new JButton("start");
        start.setBounds(Window.WINDOW_WIDTH/2-60,Window.WINDOW_HEIGHT/2-BUTTON_SIZE,BUTTON_SIZE,BUTTON_SIZE);
        start.setVisible(true);
        this.add(start);
        repaint();

        JLabel message = new JLabel();
        message.setBounds(0,LABEL_HEIGHT*2,LABEL_TEXT_WIDTH,LABEL_HEIGHT);
        message.setVisible(true);

        start.addActionListener(event -> {
            textInput = userText.getText();
            phoneInput = userPhoneNumber.getText();
            if (textInput.equals("")) {
                message.setText("no text");
            }
            else if (phoneInput.equals("")) {
                message.setText("no phone number");
            }
            else if (!validateTelAndMobileNo(phoneInput)) {
                message.setText("phone number is not valid");
            }
            else {
                phoneInput = "972"+phoneInput.substring(phoneInput.length()-9);
                System.out.println(phoneInput);

                new Thread (() -> {
                    connect();
                }).start();

            }

            this.add(message);
            text.setVisible(false);
            phone.setVisible(false);
            userPhoneNumber.setVisible(false);
            userText.setVisible(false);
            start.setVisible(false);
            repaint();
        });
    }


    public boolean validateTelAndMobileNo(String mobileNo){
        //String test = "00911234567891";
        return  !(!mobileNo.matches("(00972|0|\\+972)[5][0-9]{8}") && !mobileNo.matches("(00970|0|\\+970)[5][0-9]{8}") && !mobileNo.matches("(05[0-9]|0[12346789])([0-9]{7})") && !mobileNo.matches("(00972|0|\\+972|0|)[2][0-9]{7}"));
    }

}
