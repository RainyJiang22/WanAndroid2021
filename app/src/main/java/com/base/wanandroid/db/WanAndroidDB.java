package com.base.wanandroid.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.base.wanandroid.application.WanAndroidApplication;
import com.base.wanandroid.history.bean.HistoryEntity;
import com.base.wanandroid.history.dao.HistoryDao;

/**
 * @author jiangshiyu
 * @date 2022/6/8
 */

@Database(version = 1, entities = {HistoryEntity.class})
public abstract class WanAndroidDB extends RoomDatabase {

    public abstract HistoryDao historyDao();

    private static WanAndroidDB sInstance;

    public synchronized static WanAndroidDB getInstance() {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(WanAndroidApplication.getApplication(),
                    WanAndroidDB.class,"wan-android-app")
                    .build();
        }
        return sInstance;
    }
}
