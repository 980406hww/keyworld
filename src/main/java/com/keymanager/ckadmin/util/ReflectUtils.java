package com.keymanager.ckadmin.util;

import com.baomidou.mybatisplus.annotations.TableField;
import com.keymanager.ckadmin.entity.Customer;
import java.lang.reflect.Field;

/**
 * @ClassName ReflectUtils
 * @Description 反射工具类
 * @Author lhc
 * @Date 2019/9/4 16:41
 * @Version 1.0
 */
public class ReflectUtils {

    /**
     * 获取实体类 @Column 的其中一个指定属性名称
     *
     * @param clazz, col
     * @return tableField
     */
    public static String getTableFieldValue(Class<?> clazz, String col) {
        if (col == null){
            return null;
        }
        String tableField = null;

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (col.equals(field.getName()) && field.isAnnotationPresent(TableField.class)) {
                TableField declaredAnnotation = field.getAnnotation(TableField.class);
                tableField = declaredAnnotation.value();
                return tableField;
            }
        }
        // 判断父类是否有指定字段
        Class<?> clazz2 = clazz.getSuperclass();
        if (!(clazz2.isAssignableFrom(Object.class))){
            Field[] fields2 = clazz2.getDeclaredFields();
            for (Field field : fields2) {
                if (col.equals(field.getName()) && field.isAnnotationPresent(TableField.class)) {
                    TableField declaredAnnotation = field.getAnnotation(TableField.class);
                    tableField = declaredAnnotation.value();
                    return tableField;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
     System.out.println(getTableFieldValue(Customer.class, "createTime"));

    }
}