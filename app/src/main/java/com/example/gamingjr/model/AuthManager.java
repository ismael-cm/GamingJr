package com.example.gamingjr.model;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class AuthManager {
    private FirebaseAuth mAuth;
    private Context mContext;

    public AuthManager(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
    }

    public void loginUser(String email, String password, final AuthListener authListener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            authListener.onLoginSuccess();
                        } else {
                            authListener.onLoginFailure(task.getException().getMessage());
                        }
                    }
                });
    }
}
