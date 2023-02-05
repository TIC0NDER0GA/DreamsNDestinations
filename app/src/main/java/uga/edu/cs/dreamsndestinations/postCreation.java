package uga.edu.cs.dreamsndestinations;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class postCreation extends Fragment {

    private Button getChooseImage;
    private Button upload;
    private Button rotate;
    private EditText title;
    private EditText body;
    private ImageView userImage;
    private ProgressBar progbar;
    private Uri mImage;
    private View rootView = getView();
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference mStoreRef;
    private DatabaseReference mDataRef;
    private String downloadUrl;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_creation, container, false);

        if (view != null) {
            getChooseImage = (Button) view.findViewById(R.id.download);
            upload = (Button) view.findViewById(R.id.upload);
            title = (EditText) view.findViewById(R.id.postTitle);
            body = (EditText) view.findViewById(R.id.postText);
            userImage = (ImageView) view.findViewById(R.id.userImage);
            progbar = (ProgressBar) view.findViewById(R.id.progressBar);
        }

        getChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilePicker();
            }
        });

        upload.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        }));

        mStoreRef = FirebaseStorage.getInstance().getReference("uploads");
        mDataRef = FirebaseDatabase.getInstance().getReference("uploads");

        return view;
    }

    private void showFilePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImage = data.getData();
            Picasso.get().load(mImage).into(userImage);
            userImage.setImageURI(mImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile() {
        if (mImage != null) {
            StorageReference fileReference = mStoreRef.child(title.getText().toString() + "." + getFileExtension(mImage));
            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Get the download URL
                     downloadUrl = uri.toString();
                    Log.e(TAG, downloadUrl);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle the error
                }
            });
            fileReference.putFile(mImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progbar.setProgress(0);
                        }
                    }, 5000);
                    Toast.makeText(getContext(),"Upload success", Toast.LENGTH_SHORT);
                    Upload upload = new Upload(title.getText().toString().trim(),"BRUH",body.getText().toString());
                    String uploadId = mDataRef.push().getKey();
                    mDataRef.child(uploadId).setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                    progbar.setProgress((int) progress);
                }
            });
        } else {
            Toast.makeText(getContext(), "no file selected", Toast.LENGTH_SHORT).show();
        }
    }

}