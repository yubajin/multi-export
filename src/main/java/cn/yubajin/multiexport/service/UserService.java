package cn.yubajin.multiexport.service;

import cn.yubajin.multiexport.entity.User;
import cn.yubajin.multiexport.entity.dto.UserAddDTO;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * (User)表服务接口
 *
 * @author yubj17
 * @since 2021-11-16 14:53:49
 */
public interface UserService extends IService<User>{

    /**
     * 通过列表数据
     *
     * @param user entity对象
     * @return 实例对象
     */
     List<User> queryAll(User user);
    
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    User queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<User> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param userAddDTO 实例对象
     * @return 实例对象
     */
    User insertUser(UserAddDTO userAddDTO);


    /**
     * 新增数据
     *
     * @param userList 实例对象
     * @return 实例对象
     */
    int batchInsertUser(List<User> userList);

    /**
     * 修改数据
     *
     * @param userAddDTO 实例对象
     * @return 实例对象
     */
    User updateUser(UserAddDTO userAddDTO);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    List<User> queryList(User copy);
}