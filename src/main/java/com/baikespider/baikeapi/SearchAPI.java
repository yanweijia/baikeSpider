package com.baikespider.baikeapi;


import com.baikespider.vo.BaikeResult;

public interface SearchAPI {

    /**
     * 根据词条编号获取 MainContent DIV的源码
     *
     * @param pageCode 词条编号
     * @return 若不存在词条 返回 null
     */
    BaikeResult downloadPageSourceByCode(Integer pageCode);

}
