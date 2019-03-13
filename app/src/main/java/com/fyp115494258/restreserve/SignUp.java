package com.fyp115494258.restreserve;

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

import com.fyp115494258.restreserve.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    Button btnCreateAccount;

    EditText edtFirstName,edtLastName,edtPhoneNumber,edtEmail,edtPassword,edtConfirmPassword;


    ProgressDialog loadingBar;

    FirebaseDatabase database;
    DatabaseReference table_user;




    FirebaseAuth mAuth;



    //Referred to the following
    //https://www.youtube.com/watch?v=sdemJT8IlmQ&list=PLxefhmF0pcPmtdoud8f64EpgapkclCllj&index=9
    //https://www.youtube.com/watch?v=s9Ptu1EGxLg&list=PLxefhmF0pcPmtdoud8f64EpgapkclCllj&index=10
    //https://www.youtube.com/watch?v=eizfx5lRE4M&index=12&list=PLxefhmF0pcPmtdoud8f64EpgapkclCllj

    //https://www.youtube.com/watch?v=LLWqlyvHEi8



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth=FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");


        edtFirstName=(EditText) findViewById(R.id.edtFirstName);
        edtLastName=(EditText) findViewById(R.id.edtLastName);
        edtPhoneNumber=(EditText) findViewById(R.id.edtPhoneNumber);
        edtEmail=(EditText) findViewById(R.id.edtEmail);
        edtPassword=(EditText) findViewById(R.id.edtPassword);
        edtConfirmPassword=(EditText) findViewById(R.id.edtConfirmPassword);

        loadingBar=new ProgressDialog(this);




        btnCreateAccount=(Button)findViewById(R.id.btnCreateAccount);


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateNewAccount();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    private void SendUserToHomeActivity() {

        Intent mainIntent = new Intent(SignUp.this, Home.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }

    private void CreateNewAccount() {

        String firstName = edtFirstName.getText().toString();
        String lastName = edtLastName.getText().toString();
        String phoneNumber = edtPhoneNumber.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(firstName))
        {
            Toast.makeText(this, "Please provide your First Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(lastName))
        {
            Toast.makeText(this, "Please provide your Last Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneNumber))
        {
            Toast.makeText(this, "Please provide your Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please provide your Email", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please provide Password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(this, "Please confirm Password", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmPassword))
        {
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {

                                SendEmailVerificationMessage();


                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(SignUp.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }



    }


    private void SendEmailVerificationMessage(){

        FirebaseUser fUser=mAuth.getCurrentUser();

        if(fUser != null)
        {
            fUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful())
                    {










                        SendUserToLogIn();

                        mAuth.signOut();

                    }
                    else
                    {
                        String error=task.getException().getMessage();
                        Toast.makeText(SignUp.this,"Error: " + error, Toast.LENGTH_SHORT).show();

                        mAuth.signOut();
                    }
                }
            });
        }
    }

    private void SendUserToLogIn() {



        Intent logInIntent = new Intent(SignUp.this,LogIn.class);
        logInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logInIntent);
        finish();

        String currentUserId=mAuth.getCurrentUser().getUid();


        User user =new User(currentUserId,edtFirstName.getText().toString(),edtLastName.getText().toString(),edtPhoneNumber.getText().toString(),edtEmail.getText().toString(),edtPassword.getText().toString());
        table_user.child(currentUserId).setValue(user);

        Toast.makeText(SignUp.this,"Registration successful", Toast.LENGTH_SHORT).show();
        Toast.makeText(SignUp.this,"Please verify your account with email", Toast.LENGTH_SHORT).show();
    }
}
