package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText nameEditText, emailEditText, passwordEditText;
    private Button signUpButton;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Find views
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        loginTextView = findViewById(R.id.loginTextView);

        // Set click listeners
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                createAccountWithEmailAndPassword(name, email, password);
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the login screen
                Intent intent = new Intent(signupActivity.this,loginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createAccountWithEmailAndPassword(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign-up successful, handle the registered user
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(signupActivity.this,"Sing up Successful", Toast.LENGTH_SHORT).show();
                            // You can add your desired logic here, such as saving the user's name or navigating to the next screen
                        } else {
                            // Sign-up failed, handle the error
                            Exception exception = task.getException();
                            String errorMessage = "Sign-up failed.";
                            if (exception != null) {
                                errorMessage = exception.getMessage();
                                Log.e("SignUpActivity", "Sign-up error: ", exception);
                            }
                            Toast.makeText(signupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}