package cn.exitcode.day001.apicontect.common;

import lombok.Data;


@Data
public class QueryPageParam {
    private static int PAGESIZE = 10;
    private static int PAGENUM = 1;
    private static String KEYWORD="";

    private int pageSize = PAGESIZE;
    private int pageNum = PAGENUM;
    private String keyword=KEYWORD;

}
