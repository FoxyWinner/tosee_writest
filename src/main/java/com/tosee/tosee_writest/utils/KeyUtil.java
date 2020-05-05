package com.tosee.tosee_writest.utils;

import java.util.Random;

public class KeyUtil
{
    /**
     * 生成唯一主键
     * 时间+随机数
     */
    public static synchronized String genUniqueKey()
    {
        Random random = new Random();


        //生成6位随机数
        Integer number = random.nextInt(900000)+1000000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
