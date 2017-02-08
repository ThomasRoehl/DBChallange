package de.throehl.mobilitaetsprofil.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.controller.ControllerFactory;
import de.throehl.mobilitaetsprofil.controller.ViewControllerFactory;
import de.throehl.mobilitaetsprofil.model.dbEntries.UserAccount;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private final String className = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*
        Setup Application Configuration
         */

        ControllerFactory.initController(getApplicationContext());
        ControllerFactory.getDbController().emptyTables();
        ViewControllerFactory.addActivity(className, this);

        /*
        ----------------------------------
         */

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.id_login);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!attemptLogin()) return;

                String name = mEmailView.getText().toString();
                String pw = mPasswordView.getText().toString();
                UserAccount user = new UserAccount(name, pw);
                int userID = ControllerFactory.getDbController().getUserID(user);
                Log.d("LOGIN", "USERID:\t" + userID);
                if (userID == -1){
                    mPasswordView.setError(getString(R.string.error_user_pw));
                    return;
                }
                ControllerFactory.setID(ControllerFactory.getDbController().getUserID(user)+"");

                Intent intent = new Intent(ControllerFactory.getAppContext(), ConnectionSearchActivity.class);
                intent.putExtra("ACTIVITY", className);
                startActivity(intent);

            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.id_register);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!attemptLogin()) return;

                String name = mEmailView.getText().toString();
                String pw = mPasswordView.getText().toString();
                UserAccount user = new UserAccount(name, pw);
                int userID = ControllerFactory.getDbController().getUserID(user);
                Log.d("LOGIN", "USERID:\t" + userID);
                if (userID == -1){
                    ControllerFactory.getDbController().insertUserAccount(user);
                    ControllerFactory.setID(ControllerFactory.getDbController().getUserID(user)+"");
                }

                Intent intent = new Intent(ControllerFactory.getAppContext(), ConnectionSearchActivity.class);
                intent.putExtra("ACTIVITY", className);
                startActivity(intent);

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private boolean attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password)){
            if (mPasswordView.getText().length() < 4) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        return !cancel;
    }
}

