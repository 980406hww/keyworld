
#缓存抓排名关键字索引
CREATE INDEX `CacheCrawlRankKeyword` ON `t_customer_keyword` (`fType`, `fStatus`, `fSearchEngine`, `fCaptureStatus`, `fCapturePositionQueryTime`) USING BTREE COMMENT '缓存抓排名关键字索引';