package cn.yubajin.multiexport.tool;

import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SmartBeanUtil {

    /**
     * 复制bean的属性
     *
     * @param source 源 要复制的对象
     * @param target 目标 复制到此对象
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    /**
     * 复制对象
     *
     * @param source 源 要复制的对象
     * @param target 目标 复制到此对象
     * @param <T>
     * @return
     */
    public static <T> T copy(Object source, Class<T> target) {
        if(source == null || target == null){
            return null;
        }
        try {
            T newInstance = target.newInstance();
            BeanUtils.copyProperties(source, newInstance);
            return newInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 复制list
     *
     * @param source
     * @param target
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> List<K> copyList(List<T> source, Class<K> target) {

        if (null == source || source.isEmpty()) {
            return Collections.emptyList();
        }
        return source.stream().map(e -> copy(e, target)).collect(Collectors.toList());
    }

    public static void main(String[] args) {
//        InnovationStudioDTO innovationStudioDTO = new InnovationStudioDTO();
//        innovationStudioDTO.setName("xiaoming");
//        innovationStudioDTO.setCityAuditStatus("unaudited");
//        String[] infos = {"info1", "info2"};
//        innovationStudioDTO.setInfo(Arrays.asList(infos));
//
//        InnovationStudio innovationStudio = SmartBeanUtil.copy(innovationStudioDTO, InnovationStudio.class);
//
//        String infos_str = SmartStringUtil.ListToString(",", innovationStudioDTO.getInfo());
//        innovationStudio.setInfo(infos_str);
//
//        System.out.println(innovationStudio);
    }
}
