package com.baikespider.baikeapi.impl;

import com.baikespider.baikeapi.SearchAPI;
import com.baikespider.exception.BaikeNotFoundException;
import com.baikespider.utils.MongoDB;
import com.baikespider.vo.BaikeResult;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

public class BaikeAPI implements SearchAPI {
    private static Logger logger = LogManager.getLogger(BaikeAPI.class);


    /**
     * 通过词条关键词获取百科页面 HTML 源码
     *
     * @param word
     * @return 若不存在词条, 返回 null
     */
    public String getPageSourceByWord(String word) {
        if (word == null || word.trim().equals(""))
            return null;
        try {
            URIBuilder builder = new URIBuilder().setScheme("https").setHost("baike.baidu.com").setPath("item/" + word);
            logger.debug(builder.toString());
            Document doc = Jsoup.connect(builder.toString()).get();
            return parseHTMLContent(doc);
        } catch (Exception e) {
            logger.error(String.format("关键词 [%s] 页面内容获取失败!", word), e);
        }
        return null;
    }

    /**
     * @see com.baikespider.baikeapi.SearchAPI#downloadPageSourceByCode(Integer)
     */
    public BaikeResult downloadPageSourceByCode(Integer pageCode) {
        if (pageCode == null)
            return null;
        try {
            URIBuilder builder = new URIBuilder().setScheme("https").setHost("baike.baidu.com").setPath("view/" + pageCode);
            logger.debug(builder.toString());
            Document doc = Jsoup.connect(builder.toString()).get();
            String word = doc.getElementsByTag("h1").first().text();
            String htmlContent = parseHTMLContent(doc);
            if (htmlContent == null)
                throw new BaikeNotFoundException("不存在该词条");
            BaikeResult baikeResult = new BaikeResult(pageCode, word, htmlContent);
        writeResultToDB(baikeResult);
        return baikeResult;
    } catch (Exception e) {
        logger.error(String.format("编号 [%s] 页面内容获取失败!", "" + pageCode), e);
        writeWrongInfoToDB(pageCode, e.getClass(), e.getMessage());
    }
        return null;
    }

    /**
     * 将爬到内容写到数据库
     *
     * @param result
     * @return
     */
    private boolean writeResultToDB(BaikeResult result) {
        try {
            MongoDB.writeResultObjectToDB("cl_baike_page", result);
            return true;
        } catch (Exception e) {
            logger.error("存入数据库失败!", e);
            return false;
        }
    }

    /**
     * 将爬取失败的 code 记录到数据库
     *
     * @param code   词条编号
     * @param type   错误类名
     * @param reason 错误原因
     * @return
     */
    private boolean writeWrongInfoToDB(Integer code, Class type, String reason) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            map.put("reason", reason);
            MongoDB.writeResultObjectToDB("cl_baike_wrong_code", map);
            return true;
        } catch (Exception e) {
            logger.error("存入数据库失败!", e);
            return false;
        }
    }

    /**
     * 分析页面 HTML ,获取所需内容
     *
     * @param doc
     * @return
     * @throws Exception
     */
    private String parseHTMLContent(Document doc) throws Exception {
        Element beforeContent = doc.getElementsByClass("before-content").first();
        Element contentWrapper = doc.getElementsByClass("content-wrapper").first();
        if (beforeContent == null) {
            if (contentWrapper == null) return null;
            else return contentWrapper.toString();
        } else
            return "" + beforeContent + contentWrapper;
    }

}
