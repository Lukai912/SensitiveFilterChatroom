package com.ubuntu.filter;

import com.ubuntu.chatroomserver.util.BCConvert;
import com.ubuntu.chatroomserver.util.StopWordUtils;
import com.ubuntu.chatroomserver.util.TextUtils;
import com.ubuntu.chatroomserver.util.WordNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class BaseWordFilter {
    private enum FilterState{SUCCESS,ERROR};
    private enum KeywordType {FILTER,STOP};
    private FilterState FilterStatus =  FilterState.ERROR;
    private Map<Integer,Integer> set = new HashMap<Integer,Integer>(); // 存储首字
    private Map<Integer, WordNode> nodes = new HashMap<Integer, WordNode>(); // 存储节点
    private Set<Integer> stopwdSet = new HashSet<Integer>(); // 停顿词
    private char SIGN = '*'; // 敏感词过滤替换

    /**
     * 加载一个文件中的词典，并构建filter<br/>
     * 文件中，每行一个敏感词条<br/>
     * <b>注意：</b>读取完成后会调用{@link BufferedReader#close()}方法。<br/>
     * <b>注意：</b>读取中的{@link IOException}不会抛出
     */
    public void addDict(BufferedReader reader){
        try{
            List<String> sensitiveWords = new ArrayList<String>();
            for(String line = reader.readLine(); line != null; line = reader.readLine()){
                sensitiveWords.add(line);
            }
            addSensitiveWord(sensitiveWords);
            addStopWord(StopWordUtils.getStopWord());
            reader.close();
            FilterStatus = FilterState.SUCCESS;

        }catch(IOException e){
            e.printStackTrace();
        }
    }
    /**
     * 增加停顿词
     *
     * @param words
     */
    public void addStopWord(final List<String> words) {
        if (words != null && words.size() > 0) {
            char[] chs;
            for (String curr : words) {
                chs = curr.toCharArray();
                for (char c : chs) {
                    stopwdSet.add(charConvert(c));
                }
            }
        }
    }

    /**
     * 添加DFA节点
     * @param words
     */
    public void addSensitiveWord(final List<String> words) {
        if (words != null && words.size() > 0) {
            char[] chs;
            int fchar;
            int lastIndex;
            WordNode fnode; // 首字母节点
            for (String curr : words) {
                chs = curr.toCharArray();
                if(chs.length  == 0)
                    continue;
                fchar = charConvert(chs[0]);
                if (!set.containsKey(fchar)) {// 没有首字定义
                    set.put(fchar, fchar);// 首字标志位 可重复add,反正判断了，不重复了
                    fnode = new WordNode(fchar, chs.length == 1);
                    nodes.put(fchar, fnode);
                } else {
                    fnode = nodes.get(fchar);
                    if (!fnode.isLast() && chs.length == 1)
                        fnode.setLast(true);
                }
                lastIndex = chs.length - 1;
                for (int i = 1; i < chs.length; i++) {
                    fnode = fnode.addIfNoExist(charConvert(chs[i]), i == lastIndex);
                }
            }
        }
    }

    /**
     * 过滤判断 将敏感词转化为成屏蔽词
     * @param src
     * @return
     */
    public final String doFilter(final String src) {
        String clearString = TextUtils.delSpaceAndLineTag(src);
        System.out.println(clearString);
        char[] chs = clearString.toCharArray();
        int length = chs.length;
        int currc;
        int k;
        WordNode node;
        for (int i = 0; i < length; i++) {
            currc = charConvert(chs[i]);
            if (!set.containsKey(currc)) {
                continue;
            }
            node = nodes.get(currc);// 日 2
            if (node == null)// 其实不会发生，习惯性写上了
                continue;
            boolean couldMark = false;
            int markNum = -1;
            if (node.isLast()) {// 单字匹配（日）
                couldMark = true;
                markNum = 0;
            }
            k = i;
            for (; ++k < length;) {
                int temp = charConvert(chs[k]);
                if (stopwdSet.contains(temp))
                    continue;
                node = node.querySub(temp);
                if (node == null)// 没有了
                    break;
                if (node.isLast()) {
                    couldMark = true;
                    markNum = k - i;// 3-2
                }
            }
            if (couldMark) {
                for (k = 0; k <= markNum; k++) {
                    chs[k + i] = SIGN;
                }
                i = i + markNum;
            }
        }

        return new String(chs);
    }

    /**
     * 是否包含敏感词
     * @param src
     * @return
     */
    public final boolean isContains(final String src) {
        char[] chs = src.toCharArray();
        int length = chs.length;
        int currc;
        int k;
        WordNode node;
        for (int i = 0; i < length; i++) {
            currc = charConvert(chs[i]);
            if (!set.containsKey(currc)) {
                continue;
            }
            node = nodes.get(currc);// 日 2
            if (node == null)// 其实不会发生，习惯性写上了
                continue;
            boolean couldMark = false;
            if (node.isLast()) {// 单字匹配（日）
                couldMark = true;
            }
            // 继续匹配（日你/日你妹），以长的优先
            // 你-3 妹-4 夫-5
            k = i;
            for (; ++k < length;) {
                int temp = charConvert(chs[k]);
                if (stopwdSet.contains(temp))
                    continue;
                node = node.querySub(temp);
                if (node == null)// 没有了
                    break;
                if (node.isLast()) {
                    couldMark = true;
                }
            }
            if (couldMark) {
                return true;
            }
        }

        return false;
    }

    /**
     * 包含目標詞的個數
     * @param src
     * @return
     */
    public final int containsNum(final String src) {
        char[] chs = src.toCharArray();
        int length = chs.length;
        int currc;
        int k;
        int num =0;
        WordNode node;
        for (int i = 0; i < length; i++) {
            currc = charConvert(chs[i]);
            if (!set.containsKey(currc)) {
                continue;
            }
            node = nodes.get(currc);// 日 2
            if (node == null)// 其实不会发生，习惯性写上了
                continue;
            boolean couldMark = false;
            if (node.isLast()) {// 单字匹配（日）
                couldMark = true;
            }
            // 继续匹配（日你/日你妹），以长的优先
            // 你-3 妹-4 夫-5
            k = i;
            for (; ++k < length;) {
                int temp = charConvert(chs[k]);
                if (stopwdSet.contains(temp))
                    continue;
                node = node.querySub(temp);
                if (node == null)// 没有了
                    break;
                if (node.isLast()) {
                    couldMark = true;
                }
            }
            if (couldMark) {
                num++;
            }
        }

        return num;
    }

    /**
     * 大写转化为小写 全角转化为半角
     *
     * @param src
     * @return
     */
    public int charConvert(char src) {
        int r = BCConvert.qj2bj(src);
        return (r >= 'A' && r <= 'Z') ? r + 32 : r;
    }
}
