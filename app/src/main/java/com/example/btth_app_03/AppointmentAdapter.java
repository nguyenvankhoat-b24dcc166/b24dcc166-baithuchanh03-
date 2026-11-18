package com.example.btth_app_03;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointmentList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int id);
    }

    public AppointmentAdapter(List<Appointment> appointmentList, OnItemClickListener listener) {
        this.appointmentList = appointmentList;
        this.listener = listener;
    }

    public void setAppointments(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        
        holder.tvTitle.setText(appointment.getTitle());
        holder.tvLocation.setText("Địa điểm: " + appointment.getLocation());
        holder.tvDateTime.setText(appointment.getDate() + " · " + appointment.getTime());
        
        if (appointment.getReminder() == 1) {
            holder.tvReminderBadge.setText("Có nhắc");
            holder.tvReminderBadge.setTextColor(Color.parseColor("#E65100")); // Dark Orange
            holder.tvReminderBadge.setBackgroundColor(Color.parseColor("#FFF3E0")); // Light Orange
        } else {
            holder.tvReminderBadge.setText("Không nhắc");
            holder.tvReminderBadge.setTextColor(Color.parseColor("#424242")); // Dark Gray
            holder.tvReminderBadge.setBackgroundColor(Color.parseColor("#E0E0E0")); // Light Gray
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(appointment.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList == null ? 0 : appointmentList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvLocation, tvDateTime, tvReminderBadge;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvReminderBadge = itemView.findViewById(R.id.tvReminderBadge);
        }
    }
}
