package cn.oasys.web.common;

import cn.oasys.web.model.dao.role.AoaRoleMapper;
import cn.oasys.web.model.pojo.user.AoaPosition;
import cn.oasys.web.model.pojo.user.AoaUser;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ Description   :  TODO
 * @ Author        :  wang
 * @ CreateDate    :  2020/10/27 8:01
 */

public class Common {
    public static int differentDays(Date date1,Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {
            return day2-day1;
        }
    }
    public static void writefile(HttpServletResponse response, File file) {
        ServletOutputStream sos = null;
        FileInputStream aa = null;
        try {
            aa = new FileInputStream(file);
            sos = response.getOutputStream();
            // 读取文件问字节码
            byte[] data = new byte[(int) file.length()];
            IOUtils.readFully(aa, data);
            // 将文件流输出到浏览器
            IOUtils.write(data, sos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                sos.close();
                aa.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
    /**
     * @ Description   :  获取每月節假日天数
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/27 11:29
     * @ Param         :  null
     * @ return        :
     */
    public static List<Integer> getHoliday(Date date) throws ParseException {
        int num = 0;
        SimpleDateFormat simdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        List<Date> dates = getMonthWekDay(year, month - 1);
        Map<String, Object> data = (Map) sendGet("http://timor.tech/api/holiday/year/" + year + "-" + month, "");
        for (String key : data.keySet()) {
            Map<String, Object> hdata = (Map<String, Object>) data.get(key);
            Date d = simdf.parse((String) hdata.get("date"));
            if ((Boolean) hdata.get("holiday") == false || dates.contains(d)) {
                dates.remove(simdf.parse((String) hdata.get("date")));
            }
            if ((Boolean) hdata.get("holiday") == true) {
                num++;
            }
        }
        num += dates.size();
        List<Integer> integers = new ArrayList<>();
        integers.add(num);
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            integers.add(31);
        } else {
            integers.add(30);
        }
        return integers;
    }

    public static int PositonToRole(AoaUser user) {
        if (user.getPosition().getPositionId() == 1) {
            return 1;
        } else if (user.getPosition().getPositionId() == 2) {
            return 2;
        } else if (user.getPosition().getPositionId() == 3) {
            return 3;
        } else {
            if (user.getPosition().getName().matches(".*经理$")) {
                return 4;
            } else {
                if (user.getRole().getRoleValue() != 6 && user.getRole().getRoleValue() != 7) {
                    return 5;
                } else {
                    return user.getRole().getRoleValue();
                }
            }
        }
    }

    /**
     * @ Description   :  爬虫
     * @ Author        :  wang
     * @ CreateDate    :  2020/10/27 11:27
     * @ Param         :  null
     * @ return        :
     */
    public static Object sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "/" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            int cot = 0;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        Map<String, Object> map = (Map<String, Object>) JSONObject.parse(result);

        return map.get("holiday");
    }

    public static List<Date> getMonthWekDay(int year, int mouth) {
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = new GregorianCalendar(year, mouth, 1);
        Calendar endCalendar = new GregorianCalendar(year, mouth, 1);
        endCalendar.add(Calendar.MONTH, 1);
        while (true) {
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            if (weekday == 1 || weekday == 7) {
                dateList.add(calendar.getTime());
            }
            calendar.add(Calendar.DATE, 1);
            if (calendar.getTimeInMillis() >= endCalendar.getTimeInMillis()) {
                break;
            }
        }
        return dateList;
    }
}
