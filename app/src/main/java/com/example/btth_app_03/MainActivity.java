package com.example.btth_app_03;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalAppointments, tvReminders;
    private RecyclerView recyclerView;
    private Button btnAddAppointment;
    
    private AppointmentAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalAppointments = findViewById(R.id.tvTotalAppointments);
        tvReminders = findViewById(R.id.tvReminders);
        recyclerView = findViewById(R.id.recyclerView);
        btnAddAppointment = findViewById(R.id.btnAddAppointment);

        dbHelper = new DatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentAdapter(new ArrayList<>(), id -> {
            Intent intent = new Intent(MainActivity.this, AppointmentDetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        btnAddAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddAppointmentActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments();
        updateStats();
    }

    private void loadAppointments() {
        List<Appointment> list = dbHelper.getAllAppointments();
        adapter.setAppointments(list);
    }

    private void updateStats() {
        int total = dbHelper.countAll();
        int reminders = dbHelper.countReminders();
        
        tvTotalAppointments.setText(String.valueOf(total));
        tvReminders.setText(String.valueOf(reminders));
    }
}
