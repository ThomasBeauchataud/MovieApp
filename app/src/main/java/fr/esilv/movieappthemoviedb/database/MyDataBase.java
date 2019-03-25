package fr.esilv.movieappthemoviedb.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MyDataBase extends SQLiteOpenHelper {

    private static final String BOOKMARK_TABLE = "bookmark_table";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_BACKDROP_PATH = "backdrop_path";

    private static final String CREATE_BOOKMARK_TABLE = "CREATE TABLE IF NOT EXISTS " + BOOKMARK_TABLE + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TITLE + " TEXT NOT NULL, "
            + COL_BACKDROP_PATH + " TEXT NOT NULL); ";

    public MyDataBase(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOKMARK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + BOOKMARK_TABLE + ";");
        onCreate(db);
    }
}
