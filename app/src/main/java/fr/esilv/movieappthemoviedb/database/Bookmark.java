package fr.esilv.movieappthemoviedb.database;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import static android.support.v4.content.ContextCompat.startActivity;

public class Bookmark {
    private String title;
    private String backdrop_path;

    public Bookmark(String title, String backdrop_path) {
        this.title = title;
        this.backdrop_path = backdrop_path;
    }
    public Bookmark(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

}
