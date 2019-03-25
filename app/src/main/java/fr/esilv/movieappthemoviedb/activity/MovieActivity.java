package fr.esilv.movieappthemoviedb.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import fr.esilv.movieappthemoviedb.R;
import fr.esilv.movieappthemoviedb.database.Bookmark;
import fr.esilv.movieappthemoviedb.database.MovieDB;
import fr.esilv.movieappthemoviedb.model.Movie;
import fr.esilv.movieappthemoviedb.model.MoviePageResult;
import fr.esilv.movieappthemoviedb.model.MovieTrailer;
import fr.esilv.movieappthemoviedb.model.MovieTrailerResult;
import fr.esilv.movieappthemoviedb.network.GetMovieDataService;
import fr.esilv.movieappthemoviedb.network.GetMovieTrailerService;
import fr.esilv.movieappthemoviedb.network.RetrofitInstance;
import fr.esilv.movieappthemoviedb.adapter.MovieAdapter;
import fr.esilv.movieappthemoviedb.utils.MovieClickListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.view.View.OnClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static fr.esilv.movieappthemoviedb.activity.MainActivity.API_KEY;
import static fr.esilv.movieappthemoviedb.activity.MainActivity.movieImagePathBuilder;

public class MovieActivity extends AppCompatActivity {

    @BindView(R.id.movie_activity_title) TextView mMovieTitle;
    @BindView(R.id.movie_activity_poster) ImageView mMoviePoster;
    @BindView(R.id.movie_activity_overview) TextView mMovieOverview;
    @BindView(R.id.movie_activity_release_date) TextView mMovieReleaseDate;
    @BindView(R.id.movie_activity_rating) TextView mMovieRating;
    @BindView(R.id.button_to_see) Button buttonToSee;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private YouTubePlayer youTubePlayer;
    public MovieDB movieDB;
    private String YoutubeAPIKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YoutubeAPIKey = "AIzaSyCAn9jbCoRFc4CSn1b0kUcBdd5hWR_Fpq0";
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final Movie mMovie = (Movie) bundle.getSerializable("movie");
        movieDB = new MovieDB(this);
        if(isInSeen(mMovie)){
            buttonToSee.getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.DARKEN);
            buttonToSee.setText("Delete From Bookmark");
        }
        else {
            buttonToSee.getBackground().setColorFilter(Color.parseColor("#008000"), PorterDuff.Mode.DARKEN);
            buttonToSee.setText("Add To Bookmark");
        }
        populateActivity(mMovie);
        getTrailer(mMovie.getId());
        buttonToSee.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            if (isInSeen(mMovie) == false) {
                movieDB.open();
                ArrayList<Bookmark> listTemp = new ArrayList<>();
                if (listTemp.size() == 1 && listTemp.get(0).getBackdrop_path() == null){
                    movieDB.deleteBookmark(listTemp.get(0));
                }
                movieDB.insertBookmark(new Bookmark(mMovie.getTitle(), mMovie.getPosterPath()));
                movieDB.close();
                buttonToSee.getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.DARKEN);
                buttonToSee.setText("Delete from Bookmark");
            }
            else {
                movieDB.open();
                movieDB.deleteBookmark(new Bookmark(mMovie.getTitle(), mMovie.getBackdropPath()));
                movieDB.close();
                buttonToSee.getBackground().setColorFilter(Color.parseColor("#008000"), PorterDuff.Mode.DARKEN);
                buttonToSee.setText("Add To Bookmark");
            }
            }
        });
    }
    private void populateActivity(Movie mMovie){
        Picasso.with(this).load(movieImagePathBuilder(mMovie.getPosterPath())).into(mMoviePoster);
        mMovieTitle.setText(mMovie.getTitle());
        mMovieOverview.setText(mMovie.getOverview());
        mMovieReleaseDate.setText(mMovie.getReleaseDate());
        String userRatingText = String.valueOf(mMovie.getVoteAverage()) + "/10";
        mMovieRating.setText(userRatingText);
    }

    private boolean isInSeen(Movie movie){
        movieDB.open();
        Bookmark test = movieDB.getBookmarkWithTitle(movie.getTitle());
        if (test == null){
            return false;
        }
        else{
            return true;
        }

    }

    private void getTrailer(int movieId) {
        GetMovieTrailerService movieTrailerService = RetrofitInstance.getRetrofitInstance().create(GetMovieTrailerService.class);
        Call<MovieTrailerResult> call = movieTrailerService.getTrailers(movieId, API_KEY);
        call.enqueue(new Callback<MovieTrailerResult>() {
            @Override
            public void onResponse(Call<MovieTrailerResult> call, Response<MovieTrailerResult> response) {
                final String youtubeKey = response.body().getTrailerResult().get(0).getKey();
                final String message = "https://youtube.com/embed/" + youtubeKey;
                Log.wtf("MovieActivity", message);
                youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.rv_movie_trailers);

                if (youTubePlayerFragment == null)
                    return;

                youTubePlayerFragment.initialize(YoutubeAPIKey, new YouTubePlayer.OnInitializedListener() {

                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                        boolean wasRestored) {
                        if (!wasRestored) {
                            youTubePlayer = player;

                            //set the player style default
                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                            //cue the 1st video by default
                            youTubePlayer.cueVideo(youtubeKey);
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                    }
                });
            }
            @Override
            public void onFailure(Call<MovieTrailerResult> call, Throwable t) {
                Toast.makeText(MovieActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openStream(String link){
        String url = "http://www.example.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
