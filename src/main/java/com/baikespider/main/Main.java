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
        if (args.length != 3) {
            System.out.println("命令格式如下: " +
                    "\n\t参数1 线程数" +
                    "\n\t参数2 开始词条编号" +
                    "\n\t参数3 结束词条编号" +
                    "\n\t   例子:  java -jar baikespider.jar 20 1 100");

            args = new String[]{"20", "1", "1"};
        }
        spider(args);
    }

    /**
     * @param args 参数
     */
    public static void spider(String[] args) {
        logger.info("~~~程序开始运行~~~");
        if (args.length != 3) {
            return;
        }
        try {
            Integer threadNum = Integer.parseInt(args[0]);
            Integer fromNum = Integer.parseInt(args[1]);
            Integer toNum = Integer.parseInt(args[2]);
            //多线程爬虫代码
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadNum);
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
