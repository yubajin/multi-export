package cn.yubajin.multiexport.handler;

import cn.yubajin.multiexport.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class UserHandler {

    public static void main(String[] args) {
        System.out.append("count is  : " + queryCount());
    }

    /**
     * 封装查询操作
     *
     * @param pageSql
     * @return
     */
    public static List queryUserList(String pageSql) {
        List userList = new ArrayList();
        Connection conn = DataSourceUtils.getConnection();
        ResultSet rst = null;
        User user;
        //取得发送sql语句的对象
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(pageSql);
            rst = pst.executeQuery();
            while (rst.next()) {
                user = new User();
                user.setId((Long) rst.getObject("id"));
                user.setNickname((String) rst.getObject("nickname"));
                user.setPoint((Integer) rst.getObject("point"));
                user.setLoginip((String) rst.getObject("loginIp"));
                user.setOrgcode((String) rst.getObject("orgCode"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //释放资源
            // close()方法把连接返回到连接池而不是真正地关闭它，而是将其放入池中等待其他操作复用。
            DataSourceUtils.close(conn, pst);
        }

        return userList;
    }

    /**获取总条数
     */
    public static int queryCount() {
        String countSql = "SELECT count(*) as count  from user";
        ResultSet rst = null;
        Long count = null;
        Connection conn = null;
        try {
            conn = DataSourceUtils.getConnection();
            PreparedStatement pst = conn.prepareStatement(countSql);
            rst = pst.executeQuery();
            while (rst.next()) {
                count = (Long) rst.getObject("count");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return count.intValue();
    }

}
