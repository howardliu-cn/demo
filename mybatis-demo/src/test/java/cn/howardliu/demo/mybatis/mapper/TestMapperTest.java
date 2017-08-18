package cn.howardliu.demo.mybatis.mapper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

/**
 * <br>created at 17-7-5
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class TestMapperTest {
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void setUp() throws Exception {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void insert() throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            TestMapper mapper = session.getMapper(TestMapper.class);
            mapper.insert(1, "test1");
            mapper.insert(2, "test2");
            mapper.insert(3, "test3");
            mapper.insert(4, "test4");
            mapper.insert(5, "test5");
        }
    }

    @Test
    public void list() throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            TestMapper mapper = session.getMapper(TestMapper.class);
            System.out.println(mapper.list());
            System.out.println(mapper.list());
            System.out.println(mapper.list());
            System.out.println(mapper.list());
            System.out.println(mapper.list());
        }
    }

    @Test
    public void get() throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            TestMapper mapper = session.getMapper(TestMapper.class);
            System.out.println(mapper.get(1));
            System.out.println(mapper.get(2));
            System.out.println(mapper.get(3));
            System.out.println(mapper.get(4));
            System.out.println(mapper.get(5));
        }
    }

    @Test
    public void delete() throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            TestMapper mapper = session.getMapper(TestMapper.class);
            System.out.println(mapper.delete(1));
            System.out.println(mapper.delete(2));
            System.out.println(mapper.delete(3));
            System.out.println(mapper.delete(4));
            System.out.println(mapper.delete(5));
        }
    }
}