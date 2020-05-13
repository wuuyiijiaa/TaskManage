package com.my.taskmanage.db;



import org.xutils.DbManager;
import org.xutils.x;

import androidx.annotation.Nullable;


@SuppressWarnings("WeakerAccess")
public class DbHelper {
    private static DbHelper helper;
    
    private DbHelper() {
    }
    
    public static DbHelper getInstance() {
        if (helper == null) {
            synchronized(DbHelper.class){
                if (helper == null) {
                    helper = new DbHelper();
                }
            }
        }
        return helper;
    }
    
    /**
     * 返回系统默认的数据库，存放在data/data/包名/databases
     */
    @Nullable
    public DbManager getDbManager() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName(Cons.DBNAME)
            .setDbDir(null)
            .setDbVersion(Cons.DBVERSION);
        try {
            return x.getDb(daoConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
