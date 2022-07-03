package com.xloger.lawrefbook.repository.book.entity.menu;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Created on 2022/6/22 23:20.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
public class DateConverter {

    @TypeConverter
    public static Date toDate(short dateLong){
        return new Date();
    }

    @TypeConverter
    public static short fromDate(Date date){
        return 0;
    }
}
