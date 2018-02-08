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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextRepassword;
    private TextView textViewSignIn;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        buttonRegister = (Button) findViewById(R.id.registerButton);

        editTextRepassword = (EditText) findViewById(R.id.repasswordTextField);
        editTextEmail = (EditText) findViewById(R.id.emailTextField);
        editTextPassword = (EditText) findViewById(R.id.passwordTextField);

        textViewSignIn = (TextView) findViewById(R.id.signInTextView);

        buttonRegister.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);

    }

    //Login User Method
    private void RegisterUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String repassword = editTextRepassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email field is empty
            Toast.makeText(this, "Please enter Email.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() < 6) {
            //Password too short
            Toast.makeText(this, "Password length too short.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(repassword)){
            //Password and Repassword not matching
            Toast.makeText(this, "Passwords not matching.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            //password field is empty
            Toast.makeText(this, "Please enter Password.", Toast.LENGTH_SHORT).show();
            return;
        }

        //continues after validation is okay
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //User successfully logged in
                    Toast.makeText(MainActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();

                    //Firebase email verification process and message
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(MainActivity.this,"Verification Email is sent to: "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this,"Failed to send verification email",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //Starting new activity to proceed
                    Intent newActivity = new Intent(MainActivity.this,Login.class);
                    startActivity(newActivity);
                    finish();

                } else {
                    Toast.makeText(MainActivity.this, "Registeration failed, try again!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View view) {

        if(view == buttonRegister) {
            RegisterUser();
        }

        if(view == textViewSignIn) {
            //Opens Signin activity
            Intent newActivity = new Intent(MainActivity.this,Login.class);
            startActivity(newActivity);
            finish();
        }

    }
}
