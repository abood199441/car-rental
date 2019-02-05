package com.example.abdelrahmanapps.carrentaltrial;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abdelrahmanapps.carrentaltrial.models.UserObject;
import com.example.abdelrahmanapps.carrentaltrial.utils.Helper;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.apache.commons.validator.routines.EmailValidator;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private EditText names, email;
    private final static int RC_SIGN_IN = 144;
    private FirebaseAuth mAuth;
    String mNames;
    FirebaseUser user;
    String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }

        //start the sign up process.
        signUp();

        names = findViewById(R.id.name);
        email = findViewById(R.id.email);

        Button signUpButton = findViewById(R.id.btnRegister);
        signUpButton.setOnClickListener(this);
    }


    private void signUp(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());


        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                //sign in failed.
                //check response.getError().getErrorCode() and handle the error.
                if(response!=null){
                    Helper.showMessageDialog(this,"Error signing up with error code: "+
                            response.getError().getErrorCode() + ", please try again later. ");
                }
                // If response is null the user canceled the sign-in flow using the back button.

                // ...
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                EmailValidator validator = EmailValidator.getInstance();

                mNames = names.getText().toString();
                mEmail = email.getText().toString();

                if (TextUtils.isEmpty(mNames) || TextUtils.isEmpty(mEmail)) {
                    Helper.displayErrorMessage(SignUpActivity.this, "All input fields must be filled");
                } else if (!validator.isValid(mEmail)) {
                    Helper.displayErrorMessage(SignUpActivity.this, "You have entered an invalid email");
                } else {
                    if (Helper.isNetworkAvailable(SignUpActivity.this)) {
                        createUser();

                    } else {
                        Helper.displayErrorMessage(SignUpActivity.this, "No network available");
                    }
                }

        }


    }

    public void createUser() {
        //update user info
        updateUserInfo(mNames, user.getPhoneNumber(), mEmail, SignUpActivity.this);

        //send user to home page and finish this activity.
        Intent startUp = new Intent(this,HomeActivity.class);
        startActivity(startUp);
        finish();
    }




    public void updateUserInfo(String name,String phoneNumber,String email, final Context context) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Helper.displayErrorMessage(context, "Welcome to our rental services");

                        }
                    }
                });

        UserObject newUser = new UserObject(name,phoneNumber,email);


        UserData.name = name;
        UserData.email = email;
        UserData.phoneNumber = phoneNumber;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("Users");
        reference.child(user.getUid()).setValue(newUser);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                String token = task.getResult().getToken();
                reference.child(user.getUid()).child("token").setValue(token);

            }
        });

    }
}
