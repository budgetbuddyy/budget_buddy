package com.example.budgetbuddy.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.helper.Utils;
import com.example.budgetbuddy.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.pixplicity.easyprefs.library.Prefs;

public class ActSignUp extends ActBase {

    TextInputLayout edtEmailId, edtPassword, edtName, edtMobile;
    Button btnRegister;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    String uid;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sign_up);

        initialization();
        clickListener();

    }

    private void clickListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email_id = edtEmailId.getEditText().getText().toString();
                String password = edtPassword.getEditText().getText().toString();
                String name = edtMobile.getEditText().getText().toString();
                String mobile = edtMobile.getEditText().getText().toString();

                if (credentialValidation(email_id, password, name, mobile)) {
                    showLoader();

                    mAuth.createUserWithEmailAndPassword(email_id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                closeLoader();
                                final FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                if(current_user != null){
                                    uid = current_user.getUid();
                                    Prefs.putString(String.valueOf(R.string.user_id),uid);
                                }

                                startActivity(new Intent(ActSignUp.this,ActMain.class));
                                finish();
                            }else{

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Sign up error",e.getMessage().toString());
                        }
                    });
                }
            }
        });
    }


    private boolean credentialValidation(String email_id, String password, String name, String mobile) {

        if (!utils.isStringValidate(name)) {
            utils.yoyoAnimation(edtName);
            return false;
        }

        if (!utils.isStringValidate(mobile)) {
            utils.yoyoAnimation(edtMobile);
            return false;
        }

        if (!utils.isStringValidate(email_id)) {
            utils.yoyoAnimation(edtEmailId);
            return false;
        }

        if(!utils.isEmailValidate(email_id)){
            utils.yoyoAnimation(edtEmailId);
            return false;
        }

        if (!utils.isStringValidate(password)) {
            utils.yoyoAnimation(edtPassword);
            return false;
        }


        return true;
    }

    private void initialization() {
        //Utils & Model

        user = new User();
        //EditText
        edtEmailId = findViewById(R.id.edt_email_id);
        edtPassword = findViewById(R.id.edt_password);
        edtName = findViewById(R.id.edt_name);
        edtMobile = findViewById(R.id.edt_mobile);

        //Buttons
        btnRegister = findViewById(R.id.btn_register);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
    }
}