package uga.edu.cs.dreamsndestinations;

import static android.content.ContentValues.TAG;
import static android.provider.Telephony.BaseMmsColumns.MESSAGE_TYPE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mail;
    private EditText pass;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        submit = (Button) findViewById(R.id.submit);
        mail = (EditText) findViewById(R.id.email_entry);
        pass = (EditText) findViewById(R.id.name_entry);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(mail.getText().toString(),pass.getText().toString(), view);
            }
        });
    }

    private void createAccount(String email, String password, View view) {
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent intent = new Intent( view.getContext(), MainActivity.class );
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            LinearLayout l = (LinearLayout) findViewById(R.id.make_user);
                            Toast.makeText( register.this, "Authentication failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}