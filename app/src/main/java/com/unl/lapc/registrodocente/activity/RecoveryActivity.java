package com.unl.lapc.registrodocente.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.unl.lapc.registrodocente.R;

public class RecoveryActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;

    private EditText txtUsuario;
    private EditText txtEmail;
    private EditText txtClave;
    private EditText txtConf;

    private String prefUsuario;
    private String prefEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        prefUsuario = sharedPref.getString(SettingsActivity.KEY_PREF_SERGURIDAD_USUARIO, "");
        prefEmail = sharedPref.getString(SettingsActivity.KEY_PREF_SERGURIDAD_EMAIL, "");

        txtUsuario = (EditText) findViewById(R.id.usuario);
        txtEmail = (EditText) findViewById(R.id.email);
        txtClave = (EditText) findViewById(R.id.clave);
        txtConf = (EditText) findViewById(R.id.confirmarclave);

        Button btnRecuperar = (Button) findViewById(R.id.btnRecuperar);

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperar();
            }
        });
    }

    private void recuperar(){
        // Reset errors.
        txtUsuario.setError(null);
        txtClave.setError(null);
        txtEmail.setError(null);
        txtConf.setError(null);

        String usuario = txtUsuario.getText().toString();
        String email = txtEmail.getText().toString();
        String clave = txtClave.getText().toString();
        String confClave = txtConf.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!isPasswordConfirmValid(clave, confClave)) {
            txtConf.setError(getString(R.string.error_invalid_confirm_password));
            focusView = txtConf;
            cancel = true;
        }

        if (!isPasswordValid(clave)) {
            txtClave.setError(getString(R.string.error_invalid_password));
            focusView = txtClave;
            cancel = true;
        }

        if (!isEmailValid(email)) {
            txtEmail.setError("Correo no registrado");
            focusView = txtEmail;
            cancel = true;
        }

        if (!isUserValid(usuario)) {
            txtUsuario.setError("Usuario no registrado");
            focusView = txtUsuario;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            //Actualiza la nueva clave
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(SettingsActivity.KEY_PREF_SERGURIDAD_CLAVE, clave);
            editor.commit();

            setResult(RESULT_OK, new Intent());
            finish();
        }
    }

    /**
     * Valida el email
     * @param email
     * @return
     */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return prefEmail.equals(email);

    }

    /**
     * Valida el nombre de usuario
     * @param nombre
     * @return
     */
    private boolean isUserValid(String nombre) {
        return prefUsuario.equals(nombre);
    }

    /**
     * Valida la clave
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 5;
    }

    private boolean isPasswordConfirmValid(String password, String confirm) {
        return password.equals(confirm);
    }
}
