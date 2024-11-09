package com.lab.endsem;

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
    private Button btnStart, btnPause, btnReset;

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

        dbHelper = new UserDatabase(this);

        btnStart.setOnClickListener(v -> startTimer());
        btnPause.setOnClickListener(v -> pauseTimer());
        btnReset.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        int hours = Integer.parseInt(editTextHours.getText().toString());
        int minutes = Integer.parseInt(editTextMinutes.getText().toString());
        int seconds = Integer.parseInt(editTextSeconds.getText().toString());

        timeLeftInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;

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
