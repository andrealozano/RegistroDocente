package com.unl.lapc.registrodocente.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.util.Utils;

/**
 * Pantalla para registrarse o autenticarse.
 */
public class LoginActivity extends AppCompatActivity {

    static final int RECUPERAR_REQUEST = 2;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private boolean mAuthTask = false;

    // UI references.
    private EditText txtUsuario;
    private EditText txtEmail;
    private EditText txtClave;
    private EditText txtConf;


    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        String userDb = sharedPref.getString(SettingsActivity.KEY_PREF_SERGURIDAD_USUARIO, "");

        if(TextUtils.isEmpty(userDb)){
            initRegisterForm();
        }else{
            initLoginForm();
        }
    }

    private void initRegisterForm(){
        setContentView(R.layout.activity_login_register);

        txtUsuario = (EditText) findViewById(R.id.usuario);
        txtEmail = (EditText) findViewById(R.id.email);
        txtClave = (EditText) findViewById(R.id.clave);
        txtConf = (EditText) findViewById(R.id.confirmarclave);

        Button btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });

        Snackbar.make(btnRegistrar, "Por favor registre un usuario para prosegir", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    private void initLoginForm(){
        setContentView(R.layout.activity_login);

        txtUsuario = (EditText) findViewById(R.id.usuario);
        txtClave = (EditText) findViewById(R.id.clave);


        Button btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        Button btnRecuperar = (Button) findViewById(R.id.btnRecuperar);
        btnRecuperar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperar();
            }
        });

        Snackbar.make(btnIniciar, "Por favor ingrese las credenciales", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    /**
     * Registra el usuario en las preferencias
     */
    private void registrar() {

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
            txtEmail.setError(getString(R.string.error_invalid_email));
            focusView = txtEmail;
            cancel = true;
        }

        if (!isUserValid(usuario)) {
            txtUsuario.setError(getString(R.string.error_invalid_user));
            focusView = txtUsuario;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mAuthTask = true;

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(SettingsActivity.KEY_PREF_SERGURIDAD_USUARIO, usuario);
            editor.putString(SettingsActivity.KEY_PREF_SERGURIDAD_EMAIL, email);
            editor.putString(SettingsActivity.KEY_PREF_SERGURIDAD_CLAVE, clave);
            editor.commit();

            Main.login = usuario;
            setResult(RESULT_OK, new Intent());
            finish();
        }
    }

    /**
     * Autentica el usuario o registra en caso de ser necesario.
     * Si hay errores muestra los mismos en los campos correspondientes.
     */
    private void login() {
        txtUsuario.setError(null);
        txtClave.setError(null);

        String usuario = txtUsuario.getText().toString();
        String clave = txtClave.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(clave)) {
            txtClave.setError(getString(R.string.error_field_required));
            focusView = txtClave;
            cancel = true;
        }

        if (TextUtils.isEmpty(usuario)) {
            txtUsuario.setError(getString(R.string.error_field_required));
            focusView = txtUsuario;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mAuthTask = true;

            String prefUsuario = sharedPref.getString(SettingsActivity.KEY_PREF_SERGURIDAD_USUARIO, "");
            String prefClave = sharedPref.getString(SettingsActivity.KEY_PREF_SERGURIDAD_CLAVE, "");

            if(usuario.equals(prefUsuario)){
                if(clave.equals(prefClave)){
                    Main.login = prefUsuario;
                    setResult(RESULT_OK, new Intent());
                    finish();
                }else{
                    txtClave.setError("Clave incorrecta");
                }
            }else{
                txtUsuario.setError("Usuario incorrecto");
            }
        }
    }

    /**
     * Envia al correo una contraseña temporal0000
     */
    private void recuperar() {
        Intent intent = new Intent(this, RecoveryActivity.class);
        startActivityForResult(intent, RECUPERAR_REQUEST);
    }

    /**
     * Valida el email
     * @param email
     * @return
     */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.length() > 0 && Utils.validarEmail(email);

    }

    /**
     * Valida el nombre de usuario
     * @param nombre
     * @return
     */
    private boolean isUserValid(String nombre) {
        //TODO: Replace this with your own logic
        //return email.contains("@");
        return nombre.length() >= 5;
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

    /**
     * Verifica la clave y su confriamción
     * @param password
     * @param confirm
     * @return
     */
    private boolean isPasswordConfirmValid(String password, String confirm) {
        //TODO: Replace this with your own logic
        return password.equals(confirm);
    }

    /**
     * Procesa el resultado al lanzar las subactividades de recuperación
     * @param requestCode Código de petición
     * @param resultCode Codigo de respuesta
     * @param data La actividad
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RECUPERAR_REQUEST) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(txtClave, "Su contraseña ha sido cambiada correctamente!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }

    }

}

