package com.unl.lapc.registrodocente.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.unl.lapc.registrodocente.R;

public class PasswordActivity extends AppCompatActivity {

    private EditText txtClave;
    private EditText txtConf;

    private String prefUsuario;
    private String prefEmail;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        prefUsuario = sharedPref.getString(SettingsActivity.KEY_PREF_SERGURIDAD_USUARIO, "");
        prefEmail = sharedPref.getString(SettingsActivity.KEY_PREF_SERGURIDAD_EMAIL, "");

        txtClave = (EditText) findViewById(R.id.clave);
        txtConf = (EditText) findViewById(R.id.confirmarclave);

        Button btnCambiar= (Button) findViewById(R.id.btnCambiar);

        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiar();
            }
        });
    }

    private void cambiar(){
        // Reset errors.
        txtClave.setError(null);
        txtConf.setError(null);

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
