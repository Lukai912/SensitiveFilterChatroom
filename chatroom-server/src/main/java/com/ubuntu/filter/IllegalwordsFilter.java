package com.ubuntu.filter;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class IllegalwordsFilter extends BaseWordFilter {
    @Autowired
    public static String name= "非法词汇";
    public static final IllegalwordsFilter IllegalwordsFilter_instance = new IllegalwordsFilter(
            new BufferedReader(new InputStreamReader(
                    ClassLoader.getSystemResourceAsStream("illegalwords"), StandardCharsets.UTF_8)));

    /**
     * 加载一个文件中的词典，并构建filter<br/>
     * 文件中，每行一个敏感词条<br/>
     * <b>注意：</b>读取完成后会调用{@link BufferedReader#close()}方法。<br/>
     * <b>注意：</b>读取中的{@link IOException}不会抛出
     */
    public IllegalwordsFilter(BufferedReader reader){
        addDict(reader);
    }
}
