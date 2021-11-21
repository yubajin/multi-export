package cn.yubajin.multiexport.entity.vo;

import lombok.Data;

/**
 *(User)VO类
 *
 * @author yubj17
 * @since 2021-11-16 14:53:56
 */
 
@Data
public class UserVO{
    /**
    * 用户ID
    */
    private Long id;
    /**
    * 用户邮箱
    */
    private String email;
    
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
    private Integer promoted;
    
    private Integer promotedseq;
    /**
    * 推荐时间
    */
    private Integer promotedtime;
    /**
    * 是否被禁止
    */
    private Integer locked;
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
    private String loginip;
    /**
    * 最后登录会话ID
    */
    private String loginsessionid;
    /**
    * 实名认证时间
    */
    private Integer approvaltime;
    /**
    * 实名认证状态
    */
    private Object approvalstatus;
    /**
    * 未读私信数
    */
    private Integer newmessagenum;
    /**
    * 未读消息数
    */
    private Integer newnotificationnum;
    /**
    * 注册IP
    */
    private String createdip;
    /**
    * 注册时间
    */
    private Integer createdtime;
    /**
    * 最后更新时间
    */
    private Integer updatedtime;
    /**
    * 邀请码
    */
    private String invitecode;
    /**
    * 组织机构ID
    */
    private Integer orgid;
    /**
    * 组织机构内部编码
    */
    private String orgcode;
    /**
    * 注册设备来源(web/ios/android)
    */
    private String registeredway;
    /**
    * 分销平台token
    */
    private String distributortoken;
    /**
    * 用户uuid
    */
    private String uuid;
    /**
    * 初始化密码
    */
    private Integer passwordinit;
    /**
    * 是否人脸注册过
    */
    private Integer faceregistered;
    
    private String registervisitid;


}