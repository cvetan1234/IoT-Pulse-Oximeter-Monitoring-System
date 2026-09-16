package com.example.muc8;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for setting and saving a username using SharedPreferences.
 */
public class OptionsActivity extends AppCompatActivity {

    private EditText usernameEditText; // EditText for entering username
    private Button saveButton; // Button to save username

    private SharedPreferences sharedPreferences; // SharedPreferences instance
    private static final String PREFS_NAME = "MyPrefs"; // SharedPreferences file name
    private static final String USERNAME_KEY = "username"; // Key for saving username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options); // Set the activity layout

        // Initialize UI components
        usernameEditText = findViewById(R.id.username_edit_text);
        saveButton = findViewById(R.id.save_button);

        // Get SharedPreferences instance for saving username
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load the saved username, if available
        String savedUsername = sharedPreferences.getString(USERNAME_KEY, "");
        usernameEditText.setText(savedUsername);

        // Save button click listener
        saveButton.setOnClickListener(v -> {
            // Save the entered username
            String username = usernameEditText.getText().toString().trim();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USERNAME_KEY, username); // Store username with the key
            editor.apply(); // Apply changes asynchronously
            runOnUiThread(() -> Toast.makeText(OptionsActivity.this, "Username saved", Toast.LENGTH_SHORT).show());
            finish(); // Close the activity after saving
        });
    }
}
