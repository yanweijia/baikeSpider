package com.baikespider.vo;

/**
 * 百科结果集
 */
public class BaikeResult {
    /**
     * 页面编号
     */
    private Integer code;
    /**
     * 关键词
     */
    private String word;
    /**
     * html文本
     */
    private String htmlContent;

    public BaikeResult() {
    }

    public BaikeResult(Integer code, String word, String htmlContent) {
        this.code = code;
        this.word = word;
        this.htmlContent = htmlContent;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
