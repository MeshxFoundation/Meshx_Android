package com.pgy.meshim.utils

import android.annotation.SuppressLint
import android.content.Context
import com.pgy.meshim.R
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by lzy on 2018/6/26.
 */
object TimeUtils {

    @SuppressLint("SimpleDateFormat")
            /**
             * 时间转化为显示字符串
             *
             * @param timeStamp
             * 单位为秒
             */
    fun getFriendlyTimeStr(context: Context, timeStamp: Long): String {
        if (timeStamp == 0L)
            return ""
        val inputTime = Calendar.getInstance()
        inputTime.timeInMillis = timeStamp * 1000
        val currenTimeZone = inputTime.time
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        if (calendar.before(inputTime)) {
            return SimpleDateFormat("HH:mm").format(currenTimeZone)
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return if (calendar.before(inputTime)) {
            "${context.getString(R.string.yesterday)} ${SimpleDateFormat("HH:mm").format(currenTimeZone)}"
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -5)
            if (calendar.before(inputTime)) {
                getWeekDayStr(context, inputTime.get(Calendar.DAY_OF_WEEK))
            } else {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.MONTH, Calendar.JANUARY)
                val year = inputTime.get(Calendar.YEAR)
                val month = inputTime.get(Calendar.MONTH)
                val day = inputTime.get(Calendar.DAY_OF_MONTH)
                year.toString() + "/" + month + "/" + day
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
            /**
             * 时间转化为聊天界面显示字符串
             *
             * @param timeStamp 单位为秒
             */
    fun getFriendlyDateStr(context: Context, date: Long): String {
        var timeStamp = date

        if (timeStamp == 0L) return ""
        val inputTime = Calendar.getInstance()
        val timeStr = timeStamp.toString() + ""
        if (timeStr.length == 10) {
            timeStamp *= 1000
        }
        inputTime.timeInMillis = timeStamp
        val currenTimeZone = inputTime.time
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        if (calendar.before(inputTime)) {
            return SimpleDateFormat("HH:mm").format(currenTimeZone)
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        if (calendar.before(inputTime)) {
            return context.getString(R.string.yesterday) + " " + SimpleDateFormat("HH:mm").format(currenTimeZone)
        }

        calendar.add(Calendar.DAY_OF_MONTH, -5)
        return if (calendar.before(inputTime)) {
            getWeekDayStr(context, inputTime.get(Calendar.DAY_OF_WEEK)) + " " + SimpleDateFormat("HH:mm").format(currenTimeZone)
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            if (calendar.before(inputTime)) {
                SimpleDateFormat("MM/dd HH:mm").format(currenTimeZone)
            } else {
                SimpleDateFormat("yyyy/MM/dd HH:mm").format(currenTimeZone)
            }
        }
    }

    /**
     * 24小时制转化成12小时制
     *
     * @param strDay
     */
//    private fun timeFormatStr(calendar: Calendar, strDay: String): String {
//        val hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val tempStr = if (hour > 11) {
//            "下午 $strDay"
//        } else {
//            "上午 $strDay"
//        }
//        return tempStr
//    }

    /**
     * 时间转化为星期
     *
     * @param indexOfWeek   星期的第几天
     */
    private fun getWeekDayStr(context: Context, indexOfWeek: Int): String {
        var weekDayStr = ""
        when (indexOfWeek) {
            1 -> weekDayStr = context.getString(R.string.sunday)
            2 -> weekDayStr = context.getString(R.string.monday)
            3 -> weekDayStr = context.getString(R.string.tuesday)
            4 -> weekDayStr = context.getString(R.string.wednesday)
            5 -> weekDayStr = context.getString(R.string.thursday)
            6 -> weekDayStr = context.getString(R.string.friday)
            7 -> weekDayStr = context.getString(R.string.saturday)
        }
        return weekDayStr
    }
}