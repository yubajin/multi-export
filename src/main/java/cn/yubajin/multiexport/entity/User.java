package cn.yubajin.multiexport.entity;

import cn.yubajin.multiexport.common.annotation.Excel;
import lombok.Data;
import java.io.Serializable;
/**
 * (User)实体类
 *
 * @author yubj17
 * @since 2021-11-16 16:32:16
 */
@Data
public class User implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
    * 用户ID
    */
    @Excel(name = "用户id")
    private Long id;
    
    private String verifiedmobile;
    /**
    * 用户密码
    */
    private String password;
    /**
    * 密码SALT
    */
    private String salt;
    /**
    * 支付密码
    */
    private String paypassword;
    /**
    * 支付密码Salt
    */
    private String paypasswordsalt;
    
    private String locale;
    /**
    * 用户URI
    */
    private String uri;
    /**
    * 用户名
    */
    @Excel(name = "用户名")
    private String nickname;
    /**
    * 头衔
    */
    private String title;
    /**
    * 标签
    */
    private String tags;
    /**
    * default默认为网站注册, weibo新浪微薄登录
    */
    private String type;
    /**
    * 积分
    */
    @Excel(name = "积分")
    private Integer point;
    /**
    * 金币
    */
    private Integer coin;
    /**
    * 小头像
    */
    private String smallavatar;
    /**
    * 中头像
    */
    private String mediumavatar;
    /**
    * 大头像
    */
    private String largeavatar;
    /**
    * 邮箱是否为已验证
    */
    private Integer emailverified;
    /**
    * 是否初始化设置的，未初始化的可以设置邮箱、用户名。
    */
    private Integer setup;
    /**
    * 用户角色
    */
    private String roles;
    /**
    * 是否为推荐
    */
    private Object promoted;
    
    private Object promotedseq;
    /**
    * 推荐时间
    */
    private Object promotedtime;
    /**
    * 是否被禁止
    */
    private Object locked;
    /**
    * 帐号锁定期限
    */
    private Integer lockdeadline;
    /**
    * 帐号密码错误次数
    */
    private Integer consecutivepassworderrortimes;
    
    private Integer lastpasswordfailtime;
    /**
    * 最后登录时间
    */
    private Integer logintime;
    /**
    * 最后登录IP
    */
    @Excel(name = "最后登录IP")
    private String loginip;
    /**
    * 最后登录会话ID
    */
    private String loginsessionid;
    /**
    * 实名认证时间
    */
    private Object approvaltime;
    /**
    * 实名认证状态
    */
    private Object approvalstatus;
    /**
    * 未读私信数
    */
    private Object newmessagenum;
    /**
    * 未读消息数
    */
    private Object newnotificationnum;
    /**
    * 注册IP
    */
    private String createdip;
    /**
    * 注册时间
    */
    private Object createdtime;
    /**
    * 最后更新时间
    */
    private Object updatedtime;
    /**
    * 邀请码
    */
    private String invitecode;
    /**
    * 组织机构ID
    */
    private Object orgid;
    /**
    * 组织机构内部编码
    */
    @Excel(name = "组织机构内部编码")
    private String orgcode;
    /**
    * 注册方式
    */
    private String registeredway;
    /**
    * 绑定状态
    */
    private Object bindstatus;
    /**
    * 确认时间
    */
    private Object confirmtime;
    /**
    * 邮箱
    */
    private String email;
    /**
    * 签名照
    */
    private String signatureimg;


}