package cn.yubajin.multiexport.handler;

/**
 * Filename: PageHelper.java
 * 
 * @function:分页对象，用作数据的批量处理
 */

public class MyPageHelper {
    public static final int pageSize = 10000;

    public static void main(String[] args) {
        long currentPageNum=0;
        String pageSql= MyPageHelper.getPageSql(currentPageNum, pageSize);
        System.out.println("  pageSql  is  : "+pageSql);

    }

    /**
     *获取总页数
     * @param totalRecordCount 总条数
     * @return 总页数
     */
    public static int getTotalPageCount(int totalRecordCount) {
        if (totalRecordCount == 0) {
            return 0;
        }

        int lastPageCount = totalRecordCount % pageSize;
        int totalPageCount;
        if (lastPageCount > 0) {
            // 如果 余数大于零的话
            totalPageCount = totalRecordCount / pageSize + 1;
        } else {
            //如果余数为零的话
            totalPageCount = totalRecordCount / pageSize;
        }
        return totalPageCount;
    }


    /**
     *拼接查询sql,根据id偏移量进行查询的
     * @param currentPageNum 当前页数-1
     * @return sql
     */
    public static String getPageSql(long currentPageNum, int pageSize){

//        return "select * from zhgh.user" +
//                " limit " +(currentPageNum) + "," + pageSize;
//        return "select * from zhgh.user" +
//                " where id>=" +(1+(currentPageNum)) +
//                " and id<="+(currentPageNum + pageSize);

        String sql = "SELECT * FROM zhgh.user WHERE id >= (SELECT id FROM user ORDER BY id LIMIT "
                + currentPageNum + ", 1) LIMIT " + pageSize;
        return sql;
    }

}
