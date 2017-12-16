package com.baikespider.main;

import com.baikespider.baikeapi.SearchAPI;
import com.baikespider.baikeapi.impl.BaikeAPI;
import com.baikespider.vo.BaikeResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final SearchAPI searchAPI = new BaikeAPI();

    /**
     * @param args 参数
     */
    public static void main(String[] args) {
        String arg[] = {"1", "1000"};
        spider(arg);
    }

    /**
     * @param args 参数
     */
    public static void spider(String[] args) {
        logger.info("~~~程序开始运行~~~");
        if (args.length != 2) {
            System.out.println("命令格式如下: " +
                    "\n\t参数1 开始词条编号" +
                    "\n\t参数2 结束词条编号" +
                    "\n\t   例子:  java -jar baikespider.jar 1 100");
            return;
        }
        try {
            Integer fromNum = Integer.parseInt(args[0]);
            Integer toNum = Integer.parseInt(args[1]);
            //多线程爬虫代码
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
            do {
                Integer finalFromNum = fromNum;
                fixedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        BaikeResult baikeResult = searchAPI.downloadPageSourceByCode(finalFromNum);
                        if (baikeResult != null)
                            logger.info(String.format("爬取词条编号 [%d][%s] 成功", finalFromNum, baikeResult.getWord()));
                        else
                            logger.info(String.format("爬取词条编号 [%d] 失败", finalFromNum));
                    }
                });
            } while (fromNum++ < toNum);
            fixedThreadPool.shutdown();
            fixedThreadPool.awaitTermination(1, TimeUnit.DAYS);
        } catch (Exception e) {
            logger.error("", e);
        }


        logger.info("~~~程序运行结束~~~");
    }
}
