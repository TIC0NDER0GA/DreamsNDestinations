package uga.edu.cs.dreamsndestinations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context comtext;
    private List<StorageReference> uploads;

    public ImageAdapter(Context context, List<StorageReference> upl) {
        comtext = context;
        uploads = upl;
    }




    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        StorageReference imageRef = uploads.get(position);
        // use Picasso or Glide to load the image into the ImageView
        Picasso.get().load(imageRef.getDownloadUrl().toString()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (uploads != null) {
            return uploads.size();
        } else {
            return 0;
        }
    }

    public class ImageViewHolder extends  RecyclerView.ViewHolder {
        public TextView textViewname;
        public ImageView imageView;


        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_upload);

        }
    }
}
