package fr.esilv.movieappthemoviedb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MovieDB {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "movie.db";

    private static final String BOOKMARK_TABLE = "bookmark_table";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_BACKDROP_PATH = "backdrop_path";
    private static final int NUM_COL_TITLE = 1;
    private static final int NUM_COL_BACKDROP_PATH = 2;

    private SQLiteDatabase bdd;

    private MyDataBase myDataBase;

    public MovieDB(Context context){
        myDataBase = new MyDataBase(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        bdd = myDataBase.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getDB(){
        return bdd;
    }

    public MyDataBase getMyDataBase() {return myDataBase;}

    public long insertBookmark(Bookmark bookmark){
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, bookmark.getTitle());
        values.put(COL_BACKDROP_PATH, bookmark.getBackdrop_path());
        return bdd.insert(BOOKMARK_TABLE, null, values);
    }

    public void deleteBookmark(Bookmark bookmark){
        bdd.execSQL("DELETE FROM " + BOOKMARK_TABLE + " WHERE " + COL_TITLE + " = \"" + bookmark.getTitle() + "\"");
    }

    public Bookmark getBookmarkWithTitle(String title){
        Cursor c = bdd.query(BOOKMARK_TABLE, new String[] {COL_ID, COL_TITLE, COL_BACKDROP_PATH}, COL_TITLE + " = \"" + title +"\"", null, null, null, null);
        return cursorToBookmark(c);
    }

    public ArrayList<Bookmark> getAllBookmark (){
        ArrayList<Bookmark> list = new ArrayList<Bookmark>();
        Cursor c = bdd.rawQuery("SELECT * FROM " + BOOKMARK_TABLE,null);
        if (c.getCount() == 0)
            return null;
        else {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++){
                Bookmark bookmark = new Bookmark();
                bookmark.setTitle(c.getString(NUM_COL_TITLE));
                bookmark.setBackdrop_path(c.getString(NUM_COL_BACKDROP_PATH));
                list.add(bookmark);
                c.moveToNext();
            }
        }
        return list;
    }

    private Bookmark cursorToBookmark(Cursor c){
        if (c.getCount() == 0)
            return null;
        c.moveToFirst();
        Bookmark bookmark = new Bookmark();
        bookmark.setTitle(c.getString(NUM_COL_TITLE));
        bookmark.setBackdrop_path(c.getString(NUM_COL_BACKDROP_PATH));
        c.close();
        return bookmark;
    }

    public void Update(SQLiteDatabase db){
        myDataBase.onUpgrade(db, VERSION_BDD, VERSION_BDD);
    }
}
