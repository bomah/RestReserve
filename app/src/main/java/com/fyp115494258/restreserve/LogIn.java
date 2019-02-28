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

import com.fyp115494258.restreserve.Common.Common;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {





    Button btnLogin;

    EditText edtLogInEmail,edtLogInPassword;

    TextView txtCreateAccountLink;

    ProgressDialog loadingBar;

    FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference table_user;

    DatabaseReference restaurant;

    String currentUser;

    User user;

    Boolean emailAddressChecker;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);



        mAuth=FirebaseAuth.getInstance();



        database=FirebaseDatabase.getInstance();

        table_user = database.getReference("User");

        restaurant = database.getReference("Restaurant");


        txtCreateAccountLink=(TextView)findViewById(R.id.txtCreateAccountLink);
        edtLogInEmail=(EditText) findViewById(R.id.edtLogInEmail);
        edtLogInPassword=(EditText) findViewById(R.id.edtLogInPassword);
        btnLogin=(Button)findViewById(R.id.btnLogIn);
        loadingBar=new ProgressDialog(this);


        txtCreateAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToSignUp();;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogIn();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void AllowUserToLogIn() {


        String email = edtLogInEmail.getText().toString();
        String password = edtLogInPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please provide Email...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please provide Password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login");
            loadingBar.setMessage("Please wait");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();


            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {

                                VerifyEmailAddress();




                                //SendUserToHomeActivity();


                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(LogIn.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }




    }

    private void VerifyEmailAddress(){

        FirebaseUser fUser=mAuth.getCurrentUser();
        emailAddressChecker=fUser.isEmailVerified();

        if(emailAddressChecker){

            SendUserToHomeActivity();



        }
        else
        {
            Toast.makeText(this,"Please verify account with email",Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }

    private void SendUserToHomeActivity() {




        currentUser=mAuth.getCurrentUser().getUid();

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(currentUser).exists() ){




                    user = dataSnapshot.child(currentUser).getValue(User.class);


                    Query userView= restaurant.orderByChild("adminEmail").equalTo(user.getEmail());

                    userView.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Intent hIntent= new Intent(LogIn.this,RestaurantAdminHome.class);

                                hIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                hIntent.putExtra("AdminEmail",user.getEmail());

                                //creating variable to store current user
                                Common.currentUser = user;
                                startActivity(hIntent);
                                finish();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                    if(user.getPhoneNumber().equals("0860377975")) {

                        Intent hintent = new Intent(LogIn.this, AdminHome.class);

                        hintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        //creating variable to store current user
                        Common.currentUser = user;
                        startActivity(hintent);
                        finish();


                    }
                    else{

                        Intent homeIntent = new Intent(LogIn.this, Home.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        //creating variable to store current user
                        Common.currentUser = user;
                        startActivity(homeIntent);
                        finish();
                    }




                }
            }





            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendUserToSignUp() {

        Intent signUpIntent = new Intent(LogIn.this, SignUp.class);

        startActivity(signUpIntent);



    }


}
