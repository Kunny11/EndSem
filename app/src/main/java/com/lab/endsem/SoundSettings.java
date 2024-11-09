package com.lab.endsem;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lab.endsem.db.UserDatabase;

public class SoundSettings extends AppCompatActivity {
    private RadioGroup radioGroupSounds;
    private RadioButton radioSound1, radioSound2, radioSound3;
    private Button btnSaveSound;
    private MediaPlayer mediaPlayer;
    private UserDatabase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_settings);

        dbHelper = new UserDatabase(this);

        radioGroupSounds = findViewById(R.id.radioGroupSounds);
        radioSound1 = findViewById(R.id.radioSound1);
        radioSound2 = findViewById(R.id.radioSound2);
        radioSound3 = findViewById(R.id.radioSound3);
        btnSaveSound = findViewById(R.id.btnSaveSound);

        radioGroupSounds.setOnCheckedChangeListener((group, checkedId) -> {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            switch (checkedId) {
                case R.id.radioSound1:
                    mediaPlayer = MediaPlayer.create(this, R.raw.sound1);
                    break;
                case R.id.radioSound2:
                    mediaPlayer = MediaPlayer.create(this, R.raw.sound2);
                    break;
                case R.id.radioSound3:
                    mediaPlayer = MediaPlayer.create(this, R.raw.sound3);
                    break;
            }
            if (mediaPlayer != null) mediaPlayer.start();
        });

        btnSaveSound.setOnClickListener(v -> {
            String selectedSound = "sound1";

            switch (radioGroupSounds.getCheckedRadioButtonId()) {
                case R.id.radioSound1:
                    selectedSound = "sound1";
                    break;
                case R.id.radioSound2:
                    selectedSound = "sound2";
                    break;
                case R.id.radioSound3:
                    selectedSound = "sound3";
                    break;
            }

            dbHelper.saveSelectedSound(selectedSound);
            Toast.makeText(this, "Sound saved!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }
}
