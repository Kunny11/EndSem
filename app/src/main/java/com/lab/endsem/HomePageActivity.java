package com.lab.endsem;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lab.endsem.db.UserDatabase;

public class HomePageActivity extends AppCompatActivity {

    private EditText editTextHours, editTextMinutes, editTextSeconds;
    private TextView timerDisplay;
    private Button btnStart, btnPause, btnReset, btnNotificationSettings, btnHistory;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean timerRunning = false;

    private MediaPlayer mediaPlayer;
    private UserDatabase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        editTextHours = findViewById(R.id.editTextHours);
        editTextMinutes = findViewById(R.id.editTextMinutes);
        editTextSeconds = findViewById(R.id.editTextSeconds);
        timerDisplay = findViewById(R.id.timerDisplay);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnReset = findViewById(R.id.btnReset);
        btnNotificationSettings = findViewById(R.id.btnNotificationSettings);
        btnHistory = findViewById(R.id.btnHistory);

        dbHelper = new UserDatabase(this);

        btnStart.setOnClickListener(v -> startTimer());
        btnPause.setOnClickListener(v -> pauseTimer());
        btnReset.setOnClickListener(v -> resetTimer());

        btnNotificationSettings.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, SoundSettings.class);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, History.class);
            startActivity(intent);
        });
    }

    private void startTimer() {
        int hours = 0, minutes = 0, seconds = 0;

        try {
            hours = editTextHours.getText().toString().isEmpty() ? 0 : Integer.parseInt(editTextHours.getText().toString());
            minutes = editTextMinutes.getText().toString().isEmpty() ? 0 : Integer.parseInt(editTextMinutes.getText().toString());
            seconds = editTextSeconds.getText().toString().isEmpty() ? 0 : Integer.parseInt(editTextSeconds.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for hours, minutes, and seconds.", Toast.LENGTH_SHORT).show();
            return;
        }

        timeLeftInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;

        if (timeLeftInMillis <= 0) {
            Toast.makeText(this, "Please set a valid time duration.", Toast.LENGTH_SHORT).show();
            return;
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
                btnReset.setEnabled(true);

                playNotificationSound();
                Toast.makeText(HomePageActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
            }
        }.start();

        timerRunning = true;
        btnStart.setEnabled(false);
        btnPause.setEnabled(true);
        btnReset.setEnabled(true);
    }

    private void playNotificationSound() {
        String selectedSound = dbHelper.getSelectedSound();
        int soundResId = R.raw.sound1;

        switch (selectedSound) {
            case "sound1":
                soundResId = R.raw.sound1;
                break;
            case "sound2":
                soundResId = R.raw.sound2;
                break;
            case "sound3":
                soundResId = R.raw.sound3;
                break;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, soundResId);
        mediaPlayer.start();
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerRunning = false;
        btnStart.setEnabled(true);
        btnPause.setEnabled(false);
        btnReset.setEnabled(true);
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 0;
        updateTimerDisplay();
        timerRunning = false;
        btnStart.setEnabled(true);
        btnPause.setEnabled(false);
        btnReset.setEnabled(false);
    }

    private void updateTimerDisplay() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeft = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerDisplay.setText(timeLeft);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
