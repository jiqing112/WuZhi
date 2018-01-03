package wuzhi.fladimir.com.wuzhi.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import wuzhi.fladimir.com.wuzhi.model.entity.Follow;

/**
 * Created by Sc_Ji on 2018-01-03.
 * Db
 */

public class MySqliteHelper extends SQLiteOpenHelper {
    private static SQLiteDatabase mSqlDataBase;

    public MySqliteHelper(Context context) {
        super(context, DbConstant.DATABASE_NAME, null, DbConstant.DATABASE_VERSION);
        mSqlDataBase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstant.CREAT_TABLE_FOLLOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 查询
     *
     * @param table
     * @param args
     * @return
     */
    private static Cursor query(String table, String[] args) {
        return mSqlDataBase.query(table, args,
                null, null, null, null, null);
    }

    /**
     * @param id
     * @param userName
     */
    public void InsertFollower(int id, String userName, String userSign) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", userName);
        values.put("sign", userSign);
        mSqlDataBase.insert(DbConstant.TABLE_NAME_Follow, null, values);
    }

    /**
     * 清空数据表
     *
     * @param tableName
     */
    public void ClearTable(String tableName) {
        mSqlDataBase.execSQL("DELETE FROM" + tableName);
    }

    /**
     * 读取关注列表
     *
     * @return
     */
    public ArrayList<Follow> getFollowList() {
        ArrayList<Follow> mList = new ArrayList<>();
        Cursor cursor = query(DbConstant.TABLE_NAME_Follow, new String[]{DbConstant.FOLLOW_ID,
                DbConstant.FOLLOW_NAME, DbConstant.FOLLOW_SIGN});
        while (cursor.moveToNext()) {
            Follow follow = new Follow();
            follow.setUserId(cursor.getInt(cursor.getColumnIndex("id")));
            follow.setUserName(cursor.getString(cursor.getColumnIndex("name")));
            follow.setUserSign(cursor.getString(cursor.getColumnIndex("sign")));
            mList.add(follow);
        }
        cursor.close();
        //mSqlDataBase.close();
        return mList;
    }

}