package com.example.food4you.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food4you.R;
import com.example.food4you.Utilities.SignalManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    //function that create the login Intent
    //A screen will come up, there will be a connection and when the connection is finished,
    // we will return to result and we will turn on onSignInResult
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            signIn();
        else
            transactToMainActivity();

    }

    private void transactToMainActivity() {
        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void signIn(){
        //choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        //create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.logoremovebg)
                .setTheme(R.style.Base_Theme_Food4You)
                .setIsSmartLockEnabled(false)
                .setAlwaysShowSignInMethodScreen(true)
                .build();
        signInLauncher.launch(signInIntent);
    }

    //get result and if the result is good give me the user and move on to the next activity
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            transactToMainActivity();
        }
        else
        {
            if (response == null)
                SignalManager.getInstance().toast("Sign-in canceled");
            else
            {
                int errorCode = response.getError().getErrorCode();  //get the error code
                handleSignInError(errorCode);   //handle the error

            }
            //retry the sign in process
            signIn();
        }
    }

    private void handleSignInError(int errorCode) {

        switch (errorCode)
        {
            case ErrorCodes.NO_NETWORK:
                SignalManager.getInstance().toast("No network connection");
                break;
            case ErrorCodes.UNKNOWN_ERROR:
                SignalManager.getInstance().toast("Unknown error occurred");
                break;
            default:
                SignalManager.getInstance().toast("Sign - in failed");
                break;
        }
    }
}