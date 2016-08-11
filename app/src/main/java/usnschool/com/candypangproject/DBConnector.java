package usnschool.com.candypangproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by it on 2016-08-01.
 */
public class DBConnector extends SQLiteOpenHelper{



    public DBConnector(Context context) {
        super(context, "candypangdb.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table logininfotbl (id text, password integer)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table if exists logininfotbl";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
