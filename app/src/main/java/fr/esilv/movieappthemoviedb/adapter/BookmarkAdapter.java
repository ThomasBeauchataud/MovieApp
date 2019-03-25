package fr.esilv.movieappthemoviedb.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fr.esilv.movieappthemoviedb.R;
import fr.esilv.movieappthemoviedb.activity.MainActivity;
import fr.esilv.movieappthemoviedb.activity.Streaming;
import fr.esilv.movieappthemoviedb.activity.ToSeeActivity;
import fr.esilv.movieappthemoviedb.database.Bookmark;
import fr.esilv.movieappthemoviedb.database.MovieDB;
import fr.esilv.movieappthemoviedb.model.Movie;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private static final String TAG = "BookmarkAdapter";
    private ArrayList<Bookmark> bookmarkList;
    private Context mContext;
    private MovieDB movieDB;
    private Bookmark nullBookmark;

    public BookmarkAdapter(ArrayList<Bookmark> bookmarkList, Context mContext) {
        this.bookmarkList = bookmarkList;
        this.mContext = mContext;
        movieDB = new MovieDB(mContext);
        nullBookmark = new Bookmark("You don't have any Bookmark",null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_to_see,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if(bookmarkList.get(position).getBackdrop_path() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load("http://image.tmdb.org/t/p/w185/" + bookmarkList.get(position).getBackdrop_path())
                    .into(viewHolder.imageView);
        }
        else {
            Glide.with(mContext)
                    .asBitmap()
                    .load("https://upload.wikimedia.org/wikipedia/commons/d/d5/No_sign.svg")
                    .into(viewHolder.imageView);
        }
        Log.d(TAG,"Image path : "+bookmarkList.get(position).getBackdrop_path());
        viewHolder.title.setText(bookmarkList.get(position).getTitle());
        viewHolder.delete_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieDB.open();
                movieDB.deleteBookmark(bookmarkList.get(position));
                ArrayList<Bookmark> listTemp = movieDB.getAllBookmark();
                if (listTemp != null) {
                    updateList(listTemp);
                }
                else{
                    listTemp.add(nullBookmark);
                    updateList(listTemp);
                }
                movieDB.close();
            }
        });
        viewHolder.watch_stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Streaming streaming = new Streaming(bookmarkList.get(position).getTitle());
                streaming.Launch();
            }
        });
    }

    @Override
    public int getItemCount() {

        return bookmarkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        RelativeLayout parent;
        Button delete_bookmark;
        Button watch_stream;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            watch_stream = itemView.findViewById(R.id.watch_stream);
            delete_bookmark = itemView.findViewById(R.id.delete_bookmark);
            imageView = itemView.findViewById(R.id.image_to_see);
            title = itemView.findViewById(R.id.title_to_see);
            parent = itemView.findViewById(R.id.parent_to_see);
        }
    }

    public void updateList(ArrayList<Bookmark> newList){
        bookmarkList = new ArrayList<>();
        bookmarkList.addAll(newList);
        notifyDataSetChanged();
    }
}
