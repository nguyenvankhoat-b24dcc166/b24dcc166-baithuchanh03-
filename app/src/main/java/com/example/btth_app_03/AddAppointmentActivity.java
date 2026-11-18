package com.example.btth_app_03;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Calendar;

public class AddAppointmentActivity extends AppCompatActivity {

    private EditText editTitle, editLocation;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private SwitchCompat switchReminder;
    private Button btnSave;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thêm lịch hẹn");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new DatabaseHelper(this);

        editTitle = findViewById(R.id.edit_title);
        editLocation = findViewById(R.id.edit_location);
        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);
        switchReminder = findViewById(R.id.switch_reminder);
        btnSave = findViewById(R.id.btn_save);
        
        timePicker.setIs24HourView(true);

        // Đặt DatePicker mặc định là ngày hôm nay
        Calendar today = Calendar.getInstance();
        datePicker.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));

        btnSave.setOnClickListener(v -> saveAppointment());
    }

    private void saveAppointment() {
        String title = editTitle.getText().toString().trim();
        String location = editLocation.getText().toString().trim();

        if (title.isEmpty()) {
            editTitle.setError("Vui lòng nhập tiêu đề");
            editTitle.requestFocus();
            return;
        }

        if (location.isEmpty()) {
            editLocation.setError("Vui lòng nhập địa điểm");
            editLocation.requestFocus();
            return;
        }

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar selectedCal = Calendar.getInstance();
        selectedCal.set(year, month, day, hour, minute, 0);
        selectedCal.set(Calendar.MILLISECOND, 0);

        if (selectedCal.getTimeInMillis() <= System.currentTimeMillis()) {
            Toast.makeText(this, "Thời gian phải trong tương lai", Toast.LENGTH_SHORT).show();
            return;
        }

        String dateStr = String.format("%04d-%02d-%02d", year, month + 1, day);
        String timeStr = String.format("%02d:%02d", hour, minute);
        int reminder = switchReminder.isChecked() ? 1 : 0;

        Appointment appointment = new Appointment(title, location, dateStr, timeStr, reminder);
        long result = db.insertAppointment(appointment);

        if (result != -1) {
            Toast.makeText(this, "Thêm lịch hẹn thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi thêm lịch hẹn", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
