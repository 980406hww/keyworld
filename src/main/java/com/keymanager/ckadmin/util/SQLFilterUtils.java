package com.keymanager.ckadmin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName SQLFilter
 * @Description SQL过滤
 * @Author lhc
 * @Date 2019/9/5 9:36
 * @Version 1.0
 */
public class SQLFilterUtils {
    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public static boolean sqlInject(String str){
        if(StringUtils.isBlank(str)){
            return false;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");
        str = StringUtils.replace(str, "null", "");
        str = StringUtils.replace(str, "=", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String sqlReg=".*(master|truncate|insert|delete|update|declare|drop|grant|--).*";
        Pattern p = Pattern.compile(sqlReg);
        Matcher m=p.matcher(str);
        return m.matches();
    }

    public static void main(String[] args) {
        String str= "productkeywordcriteria{keyword=null, terminaltype=null, searchengin=null, status=null}";
        String sqlReg=".*(update|drop|truncate|alter|set|delete|insert|exec|master|or|and|union|declare|count|grant|group|--).*";
        Pattern p = Pattern.compile(sqlReg);
        Matcher m=p.matcher(str);
        System.out.println(m.matches());
        System.out.println(m.group());
    }

}
