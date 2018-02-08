package com.example.vdntd.attendancemanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignIn;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        //Get information about user into userobject
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //Email verification check user.isEmailVerified() == true

        //Track if user is already logged in
        if(firebaseAuth.getCurrentUser() != null) {
            //Start home page activity
            finish();
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        editTextEmail = (EditText) findViewById(R.id.emailAddressTextField);
        editTextPassword = (EditText) findViewById(R.id.passwordTextField);
        buttonSignIn = (Button) findViewById(R.id.loginButton);
        textViewSignIn = (TextView) findViewById(R.id.signInTextView);

        buttonSignIn.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);

    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email field is empty
            Toast.makeText(this, "Please enter Email.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            //password field is empty
            Toast.makeText(this, "Please enter Password.", Toast.LENGTH_SHORT).show();
            return;
        }

        //continues after validation is okay
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(task.isSuccessful()) {
                    //Start home page activity
                    finish();
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignIn) {
            userLogin();
        }

        if(view == textViewSignIn) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
