package com.example.btth_app_03;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AppointmentDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvLocation, tvTime, tvReminderLabel;
    private Switch switchReminder;
    private Button btnBack, btnDelete;
    private DatabaseHelper db;
    private int appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvLocation = findViewById(R.id.tvLocation);
        tvTime = findViewById(R.id.tvTime);
        tvReminderLabel = findViewById(R.id.tvReminderLabel);
        switchReminder = findViewById(R.id.switchReminder);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);

        db = new DatabaseHelper(this);

        appointmentId = getIntent().getIntExtra("id", -1);
        if (appointmentId == -1) {
            Toast.makeText(this, "Không tìm thấy lịch hẹn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadData();

        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            db.updateReminder(appointmentId, isChecked);
            updateReminderLabel(isChecked);
        });

        btnBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> {
            db.deleteAppointment(appointmentId);
            Toast.makeText(this, "Đã xóa lịch hẹn", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadData() {
        Appointment a = db.getAppointmentById(appointmentId);
        if (a == null) {
            Toast.makeText(this, "Không tìm thấy lịch hẹn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTitle.setText(a.getTitle());
        tvLocation.setText(a.getLocation());

        // Format time: yyyy-MM-dd to dd/MM/yyyy
        String formattedDate = a.getDate();
        if (formattedDate != null && formattedDate.contains("-")) {
            String[] parts = formattedDate.split("-");
            if (parts.length == 3) {
                formattedDate = parts[2] + "/" + parts[1] + "/" + parts[0];
            }
        }
        
        tvTime.setText(formattedDate + " · " + a.getTime());

        boolean isReminderOn = a.getReminder() == 1;
        switchReminder.setChecked(isReminderOn);
        updateReminderLabel(isReminderOn);
    }

    private void updateReminderLabel(boolean isChecked) {
        if (isChecked) {
            tvReminderLabel.setText("Đang bật");
        } else {
            tvReminderLabel.setText("Đang tắt");
        }
    }
}
