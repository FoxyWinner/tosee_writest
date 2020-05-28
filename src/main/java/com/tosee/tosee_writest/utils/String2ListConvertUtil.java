package com.tosee.tosee_writest.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/5 9:28 下午
 */
@Slf4j
public class String2ListConvertUtil
{
    public static List<String> StringAnswer2ListAnswer(String source)
    {
        if(source.startsWith("[")) source = source.replace("[","");
        if(source.endsWith("]")) source = source.replace("]","");

        List<String> strs = CollectionUtils.arrayToList(source.split(","));
        List<String> result = new ArrayList<>();
        for (String str : strs)
        {
            if(str.equals("")) result.add(str);
            else result.add(str.trim());
        }
        return result;
    }

    public static List<Integer> String2ListInteger(String source)
    {
        if(source.startsWith("[")) source = source.replace("[","");
        if(source.endsWith("]")) source = source.replace("]","");

        List<String> strs = new ArrayList<>();
        if (!StringUtils.isEmpty(source))
        {
            strs = CollectionUtils.arrayToList(source.split(","));
        }
        List<Integer> result = new ArrayList<>();

        if (!CollectionUtils.isEmpty(strs))
        {
            for (String str : strs)
            {
                result.add(Integer.valueOf(str.trim()));
            }
        }
        return result;
    }
}
