package fr.esilv.movieappthemoviedb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.esilv.movieappthemoviedb.R;
import fr.esilv.movieappthemoviedb.model.Movie;
import fr.esilv.movieappthemoviedb.utils.MovieClickListener;
import fr.esilv.movieappthemoviedb.viewholder.MovieViewHolder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private final MovieClickListener movieClickListener;
    private List<Movie> movieList;

    public MovieAdapter(List<Movie> movieList, MovieClickListener movieClickListener) {
        this.movieList = movieList;
        this.movieClickListener = movieClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card_view, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = this.movieList.get(position);
        holder.bind(movie, movieClickListener);
    }

    @Override
    public int getItemCount() {
        return this.movieList.size();
    }

    @Override
    public void onViewRecycled(MovieViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public void updateList(List<Movie> newList){
        movieList = new ArrayList<Movie>();
        movieList.addAll(newList);
        notifyDataSetChanged();
    }
}