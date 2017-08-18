package cn.howardliu.demo.mybatis.mapper;

import cn.howardliu.demo.mybatis.pojo.Test;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>created at 17-7-5
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TestMapper {
    int insert(@Param("sid") Integer sid, @Param("name") String name);

    List<Test> list();

    Test get(@Param("sid") Integer sid);

    int delete(@Param("sid") Integer sid);
}
