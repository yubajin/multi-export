package cn.yubajin.multiexport.wrapper;


import cn.yubajin.multiexport.tool.SmartBeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import cn.yubajin.multiexport.entity.User;
import cn.yubajin.multiexport.entity.vo.UserVO;
import java.util.List;
import java.util.stream.Collectors;

/**
 *(User)包装实体类
 *
 * @author yubj17
 * @since 2021-11-16 14:53:55
 */
 
public class UserWrapper {

    public static UserWrapper build(){
        return new UserWrapper();
    }
    
    public UserVO entityVO(User user){
        UserVO copy = SmartBeanUtil.copy(user, UserVO.class);
        // to do
        return copy;
    }
    
    // listVO
    public List<UserVO> listVO(List<User> list){
        return list.stream().map(this::entityVO).collect(Collectors.toList());
    }
    
    public IPage<UserVO> pageVO(List<User> list){
        // 注意！！！ 得到总数, 必须紧接在pageHelper.start()语句后，否则不能正确查出总数total
        PageInfo pageResult = new PageInfo(list);
        List<UserVO> collect = listVO(list);
        Page<UserVO> pageVO = new Page(pageResult.getPageNum(), pageResult.getPageSize(), pageResult.getTotal());
        pageVO.setRecords(collect);
        return pageVO;
    }
}