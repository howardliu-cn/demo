package cn.howardliu.demo.mybatis.pojo;

/**
 * <br>created at 17-7-5
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class Test {
    private Integer sid;
    private String name;

    public Integer getSid() {
        return sid;
    }

    public Test setSid(Integer sid) {
        this.sid = sid;
        return this;
    }

    public String getName() {
        return name;
    }

    public Test setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "Test{" +
                "sid=" + sid +
                ", name='" + name + '\'' +
                '}';
    }
}
