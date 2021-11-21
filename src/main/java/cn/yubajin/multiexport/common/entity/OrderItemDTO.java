package cn.yubajin.multiexport.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: zhuoda
 * @create: 2020-03-20 09:07 PM from win10
 */

@Slf4j
@Data
@AllArgsConstructor
public class OrderItemDTO {
    private String column;
    private boolean asc = true;
}
