package com.example.budgetbuddy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.budgetbuddy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pixplicity.easyprefs.library.Prefs;

public class ActLogin extends ActBase {

    TextInputLayout edtEmailId, edtPassword;
    TextView btnForgetPass, btnCreateUser;
    Button btnLogin;
    FirebaseAuth mAuth;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        initialization();
        setSpannable();
        clickListeners();

    }

    private void setSpannable() {
        String s1 = "Don't have an account? Create an account.";

        SpannableString ss1 = new SpannableString(s1);
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), "Don't have an account? ".length()-1, "Don't have an account? Create an account.".length()-1, 0);
        ss1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(ActLogin.this, ActSignUp.class));
            }
        }, "Don't have an account? ".length(), "Don't have an account? Create an account.".length(), 0);

        btnCreateUser.setText(ss1);
        btnCreateUser.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void clickListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String email_id = edtEmailId.getEditText().getText().toString();
                String password = edtPassword.getEditText().getText().toString();

                if (credentialValidation(email_id, password)) {
                    showLoader();
                    mAuth.signInWithEmailAndPassword(email_id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            closeLoader();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            closeLoader();
                            final FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            if (current_user != null) {
                                uid = current_user.getUid();
                                Prefs.putString(String.valueOf(R.string.user_id), uid);
                            }
                        }
                    });
                }

            }
        });


    }


    private void initialization() {
        //Layout
        edtEmailId = findViewById(R.id.edt_email_id);
        edtPassword = findViewById(R.id.edt_password);
        btnForgetPass = findViewById(R.id.btn_forget_pass);
        btnLogin = findViewById(R.id.btn_login);
        btnCreateUser = findViewById(R.id.btn_create_user);
        //Firebase
        mAuth = FirebaseAuth.getInstance();
    }

    private boolean credentialValidation(String email_id, String password) {


        if (!utils.isStringValidate(email_id)) {
            utils.yoyoAnimation(edtEmailId);
            return false;
        }

        if (!utils.isStringValidate(password)) {
            utils.yoyoAnimation(edtPassword);
            return false;
        }


        return true;
    }
}