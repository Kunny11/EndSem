package com.lab.endsem.adapter;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lab.endsem.R;
import com.lab.endsem.db.TimerDatabase;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimerViewHolder> {

    private final Cursor cursor;

    public TimeAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public TimerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timer, parent, false);
        return new TimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimerViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String duration = cursor.getString(cursor.getColumnIndexOrThrow(TimerDatabase.COLUMN_DURATION));
            String endTime = cursor.getString(cursor.getColumnIndexOrThrow(TimerDatabase.COLUMN_END_TIME));

            holder.textViewDuration.setText(duration);
            holder.textViewEndTime.setText(endTime);
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class TimerViewHolder extends RecyclerView.ViewHolder {
        public final TextView textViewDuration;
        public final TextView textViewEndTime;

        public TimerViewHolder(View itemView) {
            super(itemView);
            textViewDuration = itemView.findViewById(R.id.textViewDuration);
            textViewEndTime = itemView.findViewById(R.id.textViewEndTime);
        }
    }
}
