package vcbraun.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.List;

import vcbraun.flixster.DetailActivity;
import vcbraun.flixster.R;
import vcbraun.flixster.models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    private Context context;
    private List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout container;
        TextView tvTitle;
        TextView tvDescription;
        ImageView ivPoster;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            card = itemView.findViewById(R.id.card);
        }

        public void bind(final Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvDescription.setText(movie.getOverview());
            String imagePath;
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imagePath = movie.getBackdropPath();
            } else {
                imagePath = movie.getPosterPath();
            }

            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder);

            Glide.with(context).load(imagePath).apply(options).into(ivPoster);

            // Make it so that clicking on a row opens a detail view
           card.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent i = new Intent(context, DetailActivity.class);
                   i.putExtra("movie", Parcels.wrap(movie));
                   context.startActivity(i);
               }
           });
        }
    }
}
