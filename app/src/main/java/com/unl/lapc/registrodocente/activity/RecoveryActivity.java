package com.unl.lapc.registrodocente.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.util.Constantes;

/**
 * Actividad para recuperar la contraseña
 */
public class RecoveryActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;

    private EditText txtUsuario;
    private EditText txtEmail;

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

        Button btnRecuperar = (Button) findViewById(R.id.btnRecuperar);

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperar();
            }
        });
    }

    /**
     * Valida que los datos sean correctos e invoca a la actividad de cambio de contraseña
     */
    private void recuperar(){
        // Reset errors.
        txtUsuario.setError(null);
        txtEmail.setError(null);

        String usuario = txtUsuario.getText().toString();
        String email = txtEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

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
            //Invoca a cambiar la clave
            Intent i = new Intent(this, PasswordActivity.class);
            startActivityForResult(i, Constantes.PASSWORD_REQUEST);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constantes.PASSWORD_REQUEST) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
        }

    }
}
