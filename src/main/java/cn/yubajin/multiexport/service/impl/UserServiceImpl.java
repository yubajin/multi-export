package cn.yubajin.multiexport.service.impl;

import cn.yubajin.multiexport.dao.UserDao;
import cn.yubajin.multiexport.entity.User;
import cn.yubajin.multiexport.entity.dto.UserAddDTO;
import cn.yubajin.multiexport.service.UserService;
import cn.yubajin.multiexport.tool.SmartBeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author yubj17
 * @since 2021-11-16 14:53:54
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Resource
    private UserDao userDao;

     /**
     * 通过列表数据
     *
     * @param user entity对象
     * @return 实例对象
     */
    @Override
    public List<User> queryAll(User user){
        return this.userDao.queryAll(user);
    }


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Long id) {
        return this.userDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<User> queryAllByLimit(int offset, int limit) {
        return this.userDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param userAddDTO 实例对象
     * @return 实例对象
     */
    @Override
    public User insertUser(UserAddDTO userAddDTO) {
        User copy = SmartBeanUtil.copy(userAddDTO, User.class);

        this.userDao.insert(copy);
        return copy;
    }

    @Override
    public int batchInsertUser(List<User> userList) {
        return userDao.batchInsertUser(userList);
    }

    /**
     * 修改数据
     *
     * @param userAddDTO 实例对象
     * @return 实例对象
     */
    @Override
    public User updateUser(UserAddDTO userAddDTO) {
        User copy = SmartBeanUtil.copy(userAddDTO, User.class);

        this.userDao.update(copy);
        return this.queryById(copy.getId());
    }

    @Override
    public List<User> queryList(User copy) {
        List<User> users = this.userDao.selectList(Wrappers.<User>query().lambda().eq(copy.getNickname() != null && copy.getNickname() != "", User::getNickname, copy.getNickname()));
        return users;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.userDao.deleteById(id) > 0;
    }
}