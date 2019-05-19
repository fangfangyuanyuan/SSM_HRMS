package com.hrms.test;
import com.hrms.bean.Curriculum;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyBatisTest {
    private static final String MYBATIES_CONFIG_PATH = "MyBatis.xml";
    private static InputStream input = null;
    private static SqlSessionFactory sqlSessionFactory = null;
    private static SqlSession session = null;

    public static void main(String[] args) throws IOException {
        try {
            input = Resources.getResourceAsStream(MYBATIES_CONFIG_PATH);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(input);
            session = sqlSessionFactory.openSession();

            Curriculum c = new Curriculum();
            c.setCouId("0051610049");
            c.setTeaId("25346501");
            c.setCurYear(2019);
            Curriculum c1 = session.selectOne("selCurByCouAndTeaAndYear", c);
            session.commit();
            System.out.println(c1.toString());

//            查询不需要commit
//            User u2 = session.selectOne("user.selectUserByName", "张飞");
//            System.out.println(u2.toString());

//            动态sql
//            User u3 = new User();
////            u3.setId(30);
//            u3.setUserName("三");  // 注意id默认值是0
//            User u0 = session.selectOne("com.block.dao.IUserDao.findUserList", u3);
//            System.out.println(u0.toString());

//            List<User> users = session.selectList("user.findAllUser2");
//            for (User user:users){
//                System.out.println(user.toString());
//            }

//            查询结果中出现null，报Cause: java.lang.IllegalArgumentException，如何解决？解决
//            动态查询中，string字段的条件查询，无结果？待定

//            关联查询
//            List<NewsColumnDemo> ncds = session.selectList("newsInfo.findAll");
//           for (NewsColumnDemo ncd:ncds){
//               System.out.println(ncd.toString());
//           }

//           带聚合结构的查询
//            List<NewsColumnDemo2> ncds2 = session.selectList("newsInfo.findAll_news_column_user");
//            for (NewsColumnDemo2 ncd2:ncds2){
//                System.out.println(ncd2.toString());
//            }
//            ColumnInfo c = new ColumnInfo();
//            c.setColumnid(3);
//            c.setColumnName("yy");
//            int a = session.update("com.block.dao.IColumnInfoDao.updateColumnInfo",c);
//            session.commit();
//            System.out.println("update" + a + "个column");
//            List<ColumnInfo> columnInfos = session.selectList("com.block.dao.IColumnInfoDao.findAllColumnInfo");
//            for (ColumnInfo columnInfo:columnInfos){
//                System.out.println(columnInfo.toString());
//            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (session != null)
                session.close();
        }
    }
}
