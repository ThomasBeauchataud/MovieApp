package fr.esilv.movieappthemoviedb.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import fr.esilv.movieappthemoviedb.R;
import fr.esilv.movieappthemoviedb.adapter.BookmarkAdapter;
import fr.esilv.movieappthemoviedb.database.Bookmark;
import fr.esilv.movieappthemoviedb.database.MovieDB;

public class ToSeeActivity extends AppCompatActivity {

    private ArrayList<Bookmark> bookmarkArrayList;
    private MovieDB movieDB;
    private static final String TAG = "ToSeeActivity";
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_see);
        Log.d(TAG,"Creating MovieDb");
        movieDB = new MovieDB(this);
        initData();
        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG,"init RecyclerView");
        RecyclerView recyclerView = findViewById(R.id.rv_to_see);
        BookmarkAdapter adapter = new BookmarkAdapter(bookmarkArrayList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData(){
        Log.d(TAG,"Inserting data");
        movieDB.open();
        bookmarkArrayList = movieDB.getAllBookmark();
        movieDB.close();
        Log.d(TAG,"Data received");
    }
}
