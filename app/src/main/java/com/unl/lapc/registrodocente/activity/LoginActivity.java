package com.unl.lapc.registrodocente.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.unl.lapc.registrodocente.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private boolean mAuthTask = false;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        String userDb = sharedPref.getString(SettingsActivity.KEY_PREF_SERGURIDAD_USUARIO, "");
        if(TextUtils.isEmpty(userDb)){
            mEmailSignInButton.setText("REGISTRAR");
            Snackbar.make(mEmailSignInButton, "Por favor registre un usuario para prosegir", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }else{
            mEmailSignInButton.setText("INICIAR");
        }

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void login() {
        /*if (mAuthTask) {
            return;
        }*/

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            mAuthTask = true;

            String mUsuario = mEmailView.getText().toString();
            String mClave = mPasswordView.getText().toString();


            String pUsuario = sharedPref.getString(SettingsActivity.KEY_PREF_SERGURIDAD_USUARIO, "");
            String pClave = sharedPref.getString(SettingsActivity.KEY_PREF_SERGURIDAD_CLAVE, "");

            if(pUsuario.equals("")){
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(SettingsActivity.KEY_PREF_SERGURIDAD_USUARIO, mUsuario);
                editor.putString(SettingsActivity.KEY_PREF_SERGURIDAD_CLAVE, mClave);
                editor.commit();

                pUsuario = mUsuario;
                pClave = mClave;
            }

            if(mUsuario.equals(pUsuario)){
                if(mClave.equals(pClave)){
                    Main.login = pUsuario;
                    setResult(RESULT_OK, new Intent());
                    finish();
                }else{
                    mPasswordView.setError("Clave incorrecta");
                }
            }else{
                mEmailView.setError("Usuario incorrecto");
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        //return email.contains("@");
        return email.length() >= 5;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 5;
    }

}

