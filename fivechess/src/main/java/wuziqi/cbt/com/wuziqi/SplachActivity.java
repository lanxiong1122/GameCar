package wuziqi.cbt.com.wuziqi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SplachActivity extends Activity {

    private Button startButton, aboutUsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);

        // Initialize the buttons
        startButton = findViewById(R.id.start_button);
        aboutUsButton = findViewById(R.id.about_us_button);

        // Set click listeners
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplachActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutUsDialog();
            }
        });
    }


        private void showAboutUsDialog() {
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(getAssets().open("leng.txt")));
                StringBuilder text = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    text.append(line).append("\n");
                }
                reader.close();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("About Us")
                        .setMessage(text.toString())
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}