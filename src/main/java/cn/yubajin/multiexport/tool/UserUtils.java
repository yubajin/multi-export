package cn.yubajin.multiexport.tool;

import cn.yubajin.multiexport.entity.User;
import cn.yubajin.multiexport.entity.dto.UserAddDTO;
import cn.yubajin.multiexport.service.UserService;
import cn.yubajin.multiexport.utils.AccountUtil;
import cn.yubajin.multiexport.utils.SnowflakeUtil;

import java.util.Random;


public class UserUtils {

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            User user = new User();
            //MP雪花算法单例生成tempId
            Long tempId = SnowflakeUtil.nextId();
            user.setId(tempId);
            user.setNickname(AccountUtil.getRandomJianHan(3));
            Random random = new Random();
            user.setPoint(random.nextInt(9));
            user.setLoginip("127.0.0.1");
            user.setOrgcode("1.273.694");
            UserAddDTO copy = SmartBeanUtil.copy(user, UserAddDTO.class);
            UserService userService = SpringUtil.getBean(UserService.class);
            userService.insertUser(copy);
        }
    }
}
