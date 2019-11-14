package Common.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author: 王翔
 * @date: 2019/11/7-12:33
 * @description: <br>
 *     序列化对象的工具类，需要Obj实现Serialize接口
 * <EndDescription>
 */
public class SerializeUtils {
    public static String serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        String string = byteArrayOutputStream.toString(StandardCharsets.ISO_8859_1);
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return string;
    }
    public static Object serializeToObject(String str) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes(StandardCharsets.ISO_8859_1));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return object;
    }
}
