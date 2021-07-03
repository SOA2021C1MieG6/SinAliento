package ar.edu.unlam.sinaliento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import ar.edu.unlam.sinaliento.utils.MySharedPreferences;

public class ConfigureAlertActivity extends AppCompatActivity {

    TextView mEmailTextView;
    Switch mEmailSwitch;
    EditText mAdditionalEmailEditText;
    Switch mAdditionalEmailSwitch;

    MySharedPreferences sharedPreferences = MySharedPreferences.getSharedPreferences(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_alert);

        mEmailTextView = findViewById(R.id.emailTextView);
        mEmailSwitch = findViewById(R.id.emailSwitch);
        mAdditionalEmailEditText = findViewById(R.id.additionalEmailEditText);
        mAdditionalEmailSwitch = findViewById(R.id.additionalEmailSwitch);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mEmailTextView.setText(sharedPreferences.getEmail());
        mEmailSwitch.setChecked(sharedPreferences.isEnableEmail());

        if (sharedPreferences.additionalEmailExists()) {
            mAdditionalEmailEditText.setHint(sharedPreferences.getAdditionalEmail());
        }

        else {
            mAdditionalEmailEditText.setHint(getString(R.string.additional_email_default_hint));
        }

        mAdditionalEmailSwitch.setChecked(sharedPreferences.isEnableAdditionalEmail());
    }

    private void goToAppActivity() {
        Intent appIntent = new Intent(this, AppActivity.class);
        startActivity(appIntent);
    }

    private boolean emailIsValid(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        return VALID_EMAIL_ADDRESS_REGEX.matcher(email).find();
    }

    public void saveAlertConfiguration(View view) {
        sharedPreferences.setEnableEmail(mEmailSwitch.isChecked());
        String txtAdditionalEmail = mAdditionalEmailEditText.getText().toString();

        if (emailIsValid(txtAdditionalEmail)) {
            sharedPreferences.setAdditionalEmail(txtAdditionalEmail);

            Toast.makeText(this, getString(R.string.stored_additional_email_text), Toast.LENGTH_SHORT).show();
        }

        else if (!txtAdditionalEmail.isEmpty()) {
            Toast.makeText(this, getString(R.string.unvalid_additional_email_text), Toast.LENGTH_SHORT).show();
        }

        if (sharedPreferences.additionalEmailExists()) {
            sharedPreferences.setEnableAdditionalEmail(mAdditionalEmailSwitch.isChecked());
        }

        Toast.makeText(this, getString(R.string.save_configuration_toast_text), Toast.LENGTH_SHORT).show();

        goToAppActivity();
    }

    public void cancelAlertConfiguration(View view) {
        goToAppActivity();
    }

    public void deleteAdditionalEmail(View view) {
        sharedPreferences.setAdditionalEmail(null);
        Toast.makeText(this, getString(R.string.delete_additional_email_toast_text), Toast.LENGTH_SHORT).show();
        goToAppActivity();
    }
}
