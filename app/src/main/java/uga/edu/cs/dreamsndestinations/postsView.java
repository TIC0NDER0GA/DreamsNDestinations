package uga.edu.cs.dreamsndestinations;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class postsView extends Fragment {
    private RecyclerView rcv;
    private ImageAdapter imgadp;
    private StorageReference dbRef;
    private FirebaseStorage database;
    private List<StorageReference> listOfImages;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        database = FirebaseStorage.getInstance("gs://dreamsndestinations-9927f.appspot.com");
        dbRef = database.getReference();


        dbRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                listOfImages = new ArrayList<>();
                for (StorageReference item : listResult.getItems()) {
                    listOfImages.add(item);
                }
                if (listOfImages != null) {
                    view = inflater.inflate(R.layout.fragment_posts_view, container, false);
                    rcv = view.findViewById(R.id.recycler_view);
                    imgadp = new ImageAdapter(getContext(), listOfImages);
                    rcv.setAdapter(imgadp);
                    rcv.setHasFixedSize(true);
                    rcv.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    Log.d("TAG", "listOfImages is null");
                }
            }
        });

        return view;


    }
}