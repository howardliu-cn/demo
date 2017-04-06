package cn.howardliu.demo.nio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <br>created at 17-3-29
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Pojo implements Serializable {
    private String name;
    private int age;
    private List<String> list = new ArrayList<>();

    public Pojo(String name, int age, List<String> list) {
        this.name = name;
        this.age = age;
        this.list = list;
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

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
