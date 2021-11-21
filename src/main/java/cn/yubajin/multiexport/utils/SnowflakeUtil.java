package cn.yubajin.multiexport.utils;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 借助mybatis-plus雪花算法
 * 二次封装
 * </p>
 *
 * @author zhouwj83
 * @since 2021-08-02
 *
 **/


 @Slf4j
public class SnowflakeUtil {

	/**
	 * mp分布式高效有序 ID 生产黑科技(sequence)
	 */
	private static Sequence sequence;

	/**
	 * 保证Sequence不重复创建
	 * @return
	 */
	private static Sequence getSequence() {
		if (sequence == null) {
			sequence = new Sequence();
		}
		return sequence;
	}

	/**
	 * 获取下一个ID
	 * @return
	 */
	public static long nextId() {
		return getSequence().nextId();
	}

}
