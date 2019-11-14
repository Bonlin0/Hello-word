package CommonClass;

import java.io.Serializable;

/**
 * @author: 王翔
 * @date: 2019/11/14-15:03
 * @description: <br>
 * <EndDescription>
 */
public class peroson implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    int age;

    public peroson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
