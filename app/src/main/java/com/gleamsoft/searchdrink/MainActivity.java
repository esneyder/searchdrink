package com.gleamsoft.searchdrink;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;



import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;



public class MainActivity extends AppCompatActivity  {

private LoginButton btnLogin;
private CallbackManager callbackManager;
private ProfilePictureView profilePictureView;
private LinearLayout infoLayout;
private TextView email;
private TextView gender;
private TextView facebookName;
private Button btnnext;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FacebookSdk.sdkInitialize(getApplicationContext());

    setContentView(R.layout.activity_main);
    btnnext= (Button) findViewById(R.id.btnnext);
    btnLogin = (LoginButton) findViewById(R.id.login_button);
    email = (TextView) findViewById(R.id.email);
    facebookName = (TextView) findViewById(R.id.name);
    gender = (TextView) findViewById(R.id.gender);
    infoLayout = (LinearLayout) findViewById(R.id.layout_info);
    profilePictureView = (ProfilePictureView) findViewById(R.id.image);

    btnLogin.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
    callbackManager = CallbackManager.Factory.create();

    // Callback registration
    btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("Main", response.toString());
                            setProfileToView(object);
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            // App code
        }

        @Override
        public void onError(FacebookException exception) {
            Toast.makeText(MainActivity.this, "error to Login Facebook", Toast.LENGTH_SHORT).show();
        }
    });


   startActivity(new Intent(this,MapsActivity.class));
    //hass();
}

@Override

protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    callbackManager.onActivityResult(requestCode, resultCode, data);

}
private void setProfileToView(JSONObject jsonObject) {
    try {
        email.setText(jsonObject.getString("email"));
        gender.setText(jsonObject.getString("gender"));
        facebookName.setText(jsonObject.getString("name"));

        profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
        profilePictureView.setProfileId(jsonObject.getString("id"));
        infoLayout.setVisibility(View.VISIBLE);
    } catch (JSONException e) {
        e.printStackTrace();
    }
}


private void hass(){

    try {
        PackageInfo info = getPackageManager().getPackageInfo("com.gleamsoft.searchdrink", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", "KeyHash:  " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    }
    catch (PackageManager.NameNotFoundException e) {

    }
    catch (NoSuchAlgorithmException e) {

    }
}
}