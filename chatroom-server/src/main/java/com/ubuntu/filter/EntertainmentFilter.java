package com.ubuntu.filter;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EntertainmentFilter extends BaseWordFilter {
    @Autowired
    public static String name= "娱乐";
    public static final EntertainmentFilter EntertainmentFilter_instance = new EntertainmentFilter(
            new BufferedReader(new InputStreamReader(
                    ClassLoader.getSystemResourceAsStream("entertainment"), StandardCharsets.UTF_8)));

    /**
     * 加载一个文件中的词典，并构建filter<br/>
     * 文件中，每行一个敏感词条<br/>
     * <b>注意：</b>读取完成后会调用{@link BufferedReader#close()}方法。<br/>
     * <b>注意：</b>读取中的{@link IOException}不会抛出
     */
    public EntertainmentFilter(BufferedReader reader){
        addDict(reader);
    }
}
