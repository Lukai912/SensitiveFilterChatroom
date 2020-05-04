package com.ubuntu.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PoliticalFilter extends BaseWordFilter {
    public static final PoliticalFilter PoliticalFilter_instance = new PoliticalFilter(
            new BufferedReader(new InputStreamReader(
                    ClassLoader.getSystemResourceAsStream("political"), StandardCharsets.UTF_8)));

    /**
     * 加载一个文件中的词典，并构建filter<br/>
     * 文件中，每行一个敏感词条<br/>
     * <b>注意：</b>读取完成后会调用{@link BufferedReader#close()}方法。<br/>
     * <b>注意：</b>读取中的{@link IOException}不会抛出
     */
    public PoliticalFilter(BufferedReader reader){
        addDict(reader);
    }
}
