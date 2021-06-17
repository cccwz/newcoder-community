package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {
    private static final Logger LOGGER= LoggerFactory.getLogger(SensitiveFilter.class);

    //替换符号
    private static final String REPLACEMENT="***";

    //初始化根节点
    private TrieNode rootNode=new TrieNode();

    @PostConstruct
    public void init(){
        try (
                InputStream is = this.getClass()
                    .getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        ){
            String keyword;
            while((keyword=reader.readLine())!=null){
                //添加前缀树
                this.addKeyword(keyword);
            }
        }
        catch (IOException e) {
            LOGGER.error("加载敏感词文件失败"+e.getMessage());
        }

    }

    private void addKeyword(String keyword) {
        TrieNode tempNode=rootNode;
        for(int i=0;i<keyword.length();i++){
            char c=keyword.charAt(i);
            TrieNode subNode=tempNode.getSubNode(c);
            if(subNode==null){
                subNode=new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            tempNode=subNode;

            if(i==keyword.length()-1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     *
     * @param text 待过滤的文本
     * @return  过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)) return null;

        //指针1
        TrieNode tempNode=rootNode;
        //指针2
        int begin=0;
        //指针3
        int position=0;
        //结果
        StringBuilder stringBuilder=new StringBuilder();
        while(position<text.length()){
            char c=text.charAt(position);
            //跳过符号
            if(isSymbol(c)){
                if(tempNode==rootNode){
                    stringBuilder.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            tempNode=tempNode.getSubNode(c);
            if(tempNode==null){
                //以begin开头的字符串不是敏感词
                tempNode=rootNode;
                stringBuilder.append(c);
                begin++;
                position++;
            }else if(tempNode.isKeywordEnd()){
                stringBuilder.append(REPLACEMENT);
                position++;
                begin=position;
                tempNode=rootNode;
            }else {
                position++;
            }
        }
        //将最后一批正常词计入
        stringBuilder.append(text.substring(begin));
        return stringBuilder.toString();
    }

    private boolean isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }

    //前缀树
    private class TrieNode{
        //关键字结束标识
        private boolean isKeywordEnd=false;
        //子节点
        private Map<Character,TrieNode> subNodes=new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子节点
        public void  addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }

        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
