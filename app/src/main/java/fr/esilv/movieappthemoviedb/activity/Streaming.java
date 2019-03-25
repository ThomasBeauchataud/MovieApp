package fr.esilv.movieappthemoviedb.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

public class Streaming extends AppCompatActivity {

    private String source;
    private String movieName;

    public Streaming(String movieName) {
        this.movieName = movieName.toLowerCase();
        this.movieName = movieName.replace(' ','-');
        this.movieName += ".htm";
        source = "https://www.voirfilms.mx/";
    }

    public void Launch(){
        String url = source + movieName;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
