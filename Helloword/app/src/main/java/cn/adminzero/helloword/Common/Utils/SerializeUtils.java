package cn.adminzero.helloword.Common.Utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author: 王翔
 * @date: 2019/11/7-12:33
 * @description: <br>
 *     序列化对象的工具类，需要Obj实现Serialize接口
 * <EndDescription>
 */
public class SerializeUtils {
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        objectOutputStream.close();
        return bytes;
    }
    public static Object serializeToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return object;
    }
}
