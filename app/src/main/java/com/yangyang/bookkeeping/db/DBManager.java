package com.yangyang.bookkeeping.db;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.db
 *   文件名：DBManager
 *   创建者：YangYang
 *   描述：负责管理数据库的类
 *          对表增删改查
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.yangyang.bookkeeping.entity.AccountBean;
import com.yangyang.bookkeeping.entity.BarChartItemBean;
import com.yangyang.bookkeeping.entity.ChartItemBean;
import com.yangyang.bookkeeping.entity.TypeBean;
import com.yangyang.bookkeeping.utils.FloatUtils;
import com.yangyang.smartbutler.utils.L;

import java.util.ArrayList;
import java.util.List;

public class DBManager {


    private static SQLiteDatabase db;

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    //读取typetb表
    public static List<TypeBean> getTypeList(int kind){
        List<TypeBean> list = new ArrayList<>();

        String sql = "select * from typetb where kind = " + kind;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind);
            list.add(typeBean);
        }

        cursor.close();
        return list;
    }

    //向accounttb插入
    public static void insertToAccounttb(AccountBean bean) {
        ContentValues values = new ContentValues();
        values.put("uId", bean.getuId());
        values.put("typename",bean.getTypename());
        values.put("sImageId",bean.getsImageId());
        values.put("beizhu",bean.getBeizhu());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("kind",bean.getKind());
        db.insert("accounttb",null,values);
        L.i(bean.toString());
    }

    //获取某天的收入与支出条目
    public static List<AccountBean> getAccountOneDayFromAccounttb(String uId, int year, int month, int day) {
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where uId=? and year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{uId + "", year + "", month + "", day + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            //String uId = cursor.getInt(cursor.getColumnIndex("uId"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            AccountBean accountBean = new AccountBean(id, uId, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }

        cursor.close();
        return list;
    }

    //获取某月的收入与支出条目
    public static List<AccountBean>getAccountOneMonthFromAccounttb(String uId, int year,int month){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where uId=? and year=? and month=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{uId + "", year + "", month + ""});
        L.i(cursor.toString());
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, uId, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    //获取某天的收入或支出总额
    public static float getSumMoneyOneDay(String uId, int year,int month,int day,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where uId=? and year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{uId + "", year + "", month + "", day + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }

        cursor.close();
        return total;
    }
    //获取某月的收入或支出总额
    public static float getSumMoneyOneMonth(String uId, int year,int month,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where uId=? and year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{uId + "", year + "", month + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }

        cursor.close();
        return total;
    }

    /** 统计某月份支出或者收入情况有多少条  收入-1   支出-0*/
    public static int getCountItemOneMonth(String uId, int year,int month,int kind){
        int total = 0;
        String sql = "select count(money) from accounttb where uId=? and year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{uId + "", year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total = count;
        }
        return total;
    }

    //获取某年的收入或支出总额
    public static float getSumMoneyOneYear(String uId, int year,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where uId=? and year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{uId + "", year + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        cursor.close();
        return total;
    }

    //根据传入的id，删除accounttb表当中的一条数据
    public static int deleteItemFromAccounttbById(int id, String uId){
        int i = db.delete("accounttb", "id=? and uId=?", new String[]{id + "", uId + ""});
        return i;
    }

    //根据备注模糊搜索
    public static List<AccountBean> getAccountListByRemarkFromAccounttb(String uId, String remark){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where uId=? and beizhu like '%" + remark + "%'";
        Cursor cursor = db.rawQuery(sql, new String[]{uId + ""});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String bz = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, uId, typename, sImageId, bz, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        cursor.close();
        return list;
    }


    //查找数据库中有几个年份的信息(去重)
    public static List<Integer> getYearListFromAccounttb(String uId) {
        List<Integer>list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb where uId=? order by year asc";
        Cursor cursor = db.rawQuery(sql, new String[]{uId + ""});
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        return list;
    }

    public static void deleteAllAccount(String uId) {
        String sql = "delete from accounttb where uId=?";
        db.execSQL(sql, new String[]{uId + ""});
    }

    //查询指定年份和月份的收入或者支出每一种类型的总钱数
    public static List<ChartItemBean>getChartListFromAccounttb(String uId, int year, int month, int kind){
        List<ChartItemBean>list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(uId, year, month, kind);  //求出支出或者收入总钱数
        String sql = "select typename,sImageId,sum(money)as total from accounttb where uId=? and year=? and month=? and kind=? group by typename " +
                "order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{uId + "", year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            //计算所占百分比  total /sumMonth
            float ratio = FloatUtils.div(total,sumMoneyOneMonth);
            ChartItemBean bean = new ChartItemBean(sImageId, typename, ratio, total);
            list.add(bean);
        }
        return list;
    }

    //获取这个月当中某一天收入支出最大的金额，金额是多少

    public static float getMaxMoneyOneDayInMonth(String uId, int year,int month,int kind){
        String sql = "select sum(money) from accounttb where uId=? and year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{uId + "", year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            return money;
        }
        return 0;
    }

    //根据指定月份每一日收入或者支出的总钱数的集合
    public static List<BarChartItemBean>getSumMoneyOneDayInMonth(String uId, int year, int month, int kind){
        String sql = "select day,sum(money) from accounttb where uId=? and year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{uId + "", year + "", month + "", kind + ""});
        List<BarChartItemBean>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            float smoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            BarChartItemBean itemBean = new BarChartItemBean(year, month, day, smoney);
            list.add(itemBean);
        }
        return list;
    }
}
