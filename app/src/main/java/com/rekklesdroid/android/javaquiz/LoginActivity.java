package com.rekklesdroid.android.javaquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rekklesdroid.android.javaquiz.model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    // for Sign Up
    MaterialEditText edtNewUsername;
    MaterialEditText edtNewPassword;
    MaterialEditText edtNewEmail;

    // for Log In
    @BindView(R.id.edt_username)
    MaterialEditText edtUsername;
    @BindView(R.id.edt_password)
    MaterialEditText edtPassword;

    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    @BindView(R.id.btn_log_in)
    Button btnLogIn;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //Firebase
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
    }

    @OnClick(R.id.btn_log_in)
    void logIn() {
        final String username = edtUsername.getText().toString();
        final String password = edtPassword.getText().toString();

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(username).exists()) {
                    if (!username.isEmpty()) {
                        User login = dataSnapshot.child(username).getValue(User.class);
                        if (login.getPassword().equals(password)) {

                            Intent homeActivity = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(homeActivity);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong password !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Please enter your username !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "User doesn't exist !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btn_sign_up)
    void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);

        edtNewUsername = sign_up_layout.findViewById(R.id.edt_new_username);
        edtNewPassword = sign_up_layout.findViewById(R.id.edt_new_password);
        edtNewEmail = sign_up_layout.findViewById(R.id.edt_new_email);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final User user = new User(edtNewUsername.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getName()).exists()) {
                            Toast.makeText(LoginActivity.this, "User already exists !", Toast.LENGTH_SHORT).show();
                        } else {
                            users.child(user.getName()).setValue(user);
                            Toast.makeText(LoginActivity.this, "User registration success !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                dialog.dismiss();
            }
        });

        alertDialog.show();
    }


}
