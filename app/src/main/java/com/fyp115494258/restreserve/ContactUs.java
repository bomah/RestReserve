package com.fyp115494258.restreserve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactUs extends AppCompatActivity {


    EditText edtYourName;
    EditText edtYourEmail;
    EditText edtYourSubject;
    EditText edtYourMessage;

    Button btnPostMessge;


    //http://androidmkab.com/2016/12/13/create-android-contact-form-beginne/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);


         edtYourName        = (EditText) findViewById(R.id.edtYourName);
         edtYourEmail      = (EditText) findViewById(R.id.edtYourEmail);
         edtYourSubject   = (EditText) findViewById(R.id.edtYourSubject);
         edtYourMessage     = (EditText) findViewById(R.id.edtYourMessage);



        btnPostMessge = (Button) findViewById(R.id.btnPostMessage);
        btnPostMessge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name      = edtYourName.getText().toString();
                String email     = edtYourEmail.getText().toString();
                String subject   = edtYourSubject.getText().toString();
                String message   = edtYourMessage.getText().toString();


                if (TextUtils.isEmpty(name)){
                    edtYourName.setError("Enter Your Name");
                    edtYourName.requestFocus();
                    return;
                }

                Boolean onError = false;
                if (!isValidEmail(email)) {
                    onError = true;
                    edtYourEmail.setError("Invalid Email");
                    return;
                }

                if (TextUtils.isEmpty(subject)){
                    edtYourSubject.setError("Enter Your Subject");
                    edtYourSubject.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(message)){
                    edtYourMessage.setError("Enter Your Message");
                    edtYourMessage.requestFocus();
                    return;
                }

                Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

                /* Fill it with Data */
                sendEmail.setType("plain/text");
                sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"115494258@umail.ucc.ie"});
                sendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                sendEmail.putExtra(android.content.Intent.EXTRA_TEXT,
                        message);

                /* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(sendEmail, "Send Email"));


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //Get a Tracker (should auto-report)


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    // validating email id

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}


