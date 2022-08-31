package ba.edu.ibu.bg;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewPostAdapter extends RecyclerView.Adapter<NewPostAdapter.MyViewHolder> {

    private Context mContext;
    private List<Post> mData;

    public NewPostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.post_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.username.setText(mData.get(position).getUsername());
        holder.postedOn.setText(mData.get(position).getPostedOn());

        //Using Glide to display image
        Glide.with(mContext)
                .load(mData.get(position).getImageURL())
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, postedOn;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            postedOn = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.post_image);
        }
    }
}
