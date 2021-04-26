package cn.cqs.base;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by bingo on 2021/3/23.
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/23
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {
    /**
     * 获取指定日期
     * @param year
     * @param month
     * @param date
     * @return
     */
    public static Date getDate(int year, int month, int date){
        Calendar cd = Calendar.getInstance();
        cd.set(year, month, date);
        return cd.getTime();
    }
    public static Date getDate(int year, int month, int date,int hourOfDay, int minute){
        Calendar cd = Calendar.getInstance();
        cd.set(year, month, date,hourOfDay,minute);
        return cd.getTime();
    }

    /**
     * 获取当前时间的前一天
     * @return
     */
    public static Date getFrontTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 获取当前时间的前一天（标准时间 时间为零时）
     * @return
     */
    public static Date getStandardFrontTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),0,0,0);
        return calendar.getTime();
    }

    /**
     * 获取某日期的前一天日期
     * @param date
     * @return
     */
    public static Date getStandardFrontTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),0,0,0);
        return calendar.getTime();
    }
    /**
     * 获取当前时间的后一天
     * @return
     */
    public static Date getAfterTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
    /**
     * 获取当前时间的后一天 （标准时间 时间为零时）
     * @return
     */
    public static Date getStandardAfterTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),0,0,0);
        return calendar.getTime();
    }

    /**
     * 获取某日期的后一天日期
     * @param date
     * @return
     */
    public static Date getStandardAfterTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),0,0,0);
        return calendar.getTime();
    }
    /**
     * 获取年
     * @return
     */
    public static int getYear(){
        Calendar cd = Calendar.getInstance();
        return  cd.get(Calendar.YEAR);
    }

    /**
     * 获取指定日期的年份
     * @param date
     * @return
     */
    public static int getYear(Date date){
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        return  cd.get(Calendar.YEAR);
    }
    /**
     * 获取月
     * @return
     */
    public static int getMonth(){
        Calendar cd = Calendar.getInstance();
        return  cd.get(Calendar.MONTH)+1;
    }
    /**
     * 获取指定日期的月份
     * @return
     */
    public static int getMonth(Date date){
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        return  cd.get(Calendar.MONTH)+1;
    }
    /**
     * 获取日
     * @return
     */
    public static int getDay(){
        Calendar cd = Calendar.getInstance();
        return  cd.get(Calendar.DATE);
    }
    /**
     * 获取指定日期的当前天 几号
     * @return
     */
    public static int getDay(Date date){
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        return  cd.get(Calendar.DATE);
    }
    /**
     * 获取时
     * @return
     */
    public static int getHour(){
        Calendar cd = Calendar.getInstance();
        return  cd.get(Calendar.HOUR);
    }
    /**
     * 获取分
     * @return
     */
    public static int getMinute() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MINUTE);
    }

    /**
     * 获取当前时间的时间戳
     * @return
     */
    public static long getCurrentTimeMillis(){
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间
     */

    public static String getCurrentTime(){
        return getFormatedDateTime(System.currentTimeMillis());
    }
    /**
     * @param dateTime
     * @return yyyy-MM-dd HH:mm
     */
    public static String getFormatedDateTime(long dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(new Date(dateTime));
    }
    public static String getFormatedDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(date);
    }
    public static String getFormatedDate_YMD(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 将时间戳转化成Date
     * @param date
     * @return
     */
    public static Date getDate(String date) {
        if (TextUtils.isEmpty(date)) return new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
