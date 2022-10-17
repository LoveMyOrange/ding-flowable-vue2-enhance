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

    public Boolean strEquals(String controlId,String...values){
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
    public Boolean strContains(String controlId,Number values){
        Long aLong = Long.valueOf(controlId);
        return aLong.equals(values);
    }
    public Boolean strContains(String controlId,Number...values){
        List<Number> list = Arrays.asList(values);
        Long aLong = Long.valueOf(controlId);
        return list.contains(aLong);
    }
    public Boolean numberContains(Number controlId,Number...values){
        List<Number> list = Arrays.asList(values);
        return list.contains(controlId);
    }
    public Boolean b(Number controlId,Number...values){
        List<Number> numbers = Arrays.asList(values);
        if(controlId.longValue() > numbers.get(0).longValue()  &&controlId.longValue()   <numbers.get(1).longValue()){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    public Boolean ab(Number controlId,Number...values){
        List<Number> numbers = Arrays.asList(values);
        if(controlId.longValue() >= numbers.get(0).longValue()  &&controlId.longValue()   <numbers.get(1).longValue()){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    public Boolean ba(Number controlId,Number...values){
        List<Number> numbers = Arrays.asList(values);
        if(controlId.longValue() > numbers.get(0).longValue()  &&controlId.longValue()   <=numbers.get(1).longValue()){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    public Boolean aba(Number controlId,Number...values){
        List<Number> numbers = Arrays.asList(values);
        if(controlId.longValue() >= numbers.get(0).longValue()  &&controlId.longValue()   <=numbers.get(1).longValue()){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
