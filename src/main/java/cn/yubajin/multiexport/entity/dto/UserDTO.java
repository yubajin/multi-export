package cn.yubajin.multiexport.entity.dto;

import cn.yubajin.multiexport.common.entity.PageParamDTO;
import lombok.Data;

import java.io.Serializable;

/**
 *(User)DTO类
 *
 * @author yubj17
 * @since 2021-11-16 14:53:56
 */
 
@Data
public class UserDTO extends PageParamDTO implements Serializable {

    /**
    * 用户名
    */
    private String nickname;


}