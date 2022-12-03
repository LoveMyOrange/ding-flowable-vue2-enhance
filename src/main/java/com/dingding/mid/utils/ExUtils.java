package com.dingding.mid.utils;

import cn.hutool.core.util.NumberUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author LoveMyOrange
 * @create 2022-10-16 22:13
 */
@Component
public class ExUtils {
    public Boolean strEqualsMethod(String controlId,String value){
        List<String> list = Arrays.asList(value);
            String s = list.get(0);
            return s.equals(controlId);
    }
    public Boolean strEqualsMethod(String controlId,String...values){
        List<String> list = Arrays.asList(values);
        if(list.size()>1){
            return Boolean.FALSE;
        }
        else{
            String s = list.get(0);
            return s.equals(controlId);
        }
    }

    public Boolean strContains(String controlId,String...values){
        List<String> list = Arrays.asList(values);
        return list.contains(controlId);
    }

    public Boolean strContains(String controlId,Number...values){
        Long aLong = Long.valueOf(controlId);
        List<Number> numbers = Arrays.asList(values);
        return numbers.contains(aLong);
    }

    public Boolean strContainsMethod(String controlId,String...values){
        List<String> strings = Arrays.asList(values);
        return strings.contains(controlId);
    }


    public Boolean numberContains(Number controlId,Number...values){
        List<Number> list = Arrays.asList(values);
        return list.contains(controlId);
    }
    public Boolean b(String controlId,Number...values){
        List<Number> numbers = Arrays.asList(values);
        Long aLong = Long.valueOf(controlId);
        if( aLong> numbers.get(0).longValue()  &&aLong   <numbers.get(1).longValue()){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }



    public Boolean ab(String controlId,Number...values){
        List<Number> numbers = Arrays.asList(values);
        Long aLong = Long.valueOf(controlId);
        if(aLong >= numbers.get(0).longValue()  &&aLong   <numbers.get(1).longValue()){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    public Boolean ba(String controlId,Number...values){
        List<Number> numbers = Arrays.asList(values);
        Long aLong = Long.valueOf(controlId);
        if(aLong > numbers.get(0).longValue()  &&aLong   <=numbers.get(1).longValue()){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    public Boolean aba(String controlId,Number...values){
        List<Number> numbers = Arrays.asList(values);
        Long aLong = Long.valueOf(controlId);
        if(aLong >= numbers.get(0).longValue()  && aLong   <=numbers.get(1).longValue()){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
