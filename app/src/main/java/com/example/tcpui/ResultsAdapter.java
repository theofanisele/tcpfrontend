package com.example.tcpui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder> {
    private List<Results> resultsList;

    public ResultsAdapter(List<Results> resultsList) {
        this.resultsList = resultsList;
    }

    @Override
    public ResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_results, parent, false);
        return new ResultsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsViewHolder holder, int position) {
        Results results = resultsList.get(position);
        holder.name.setText(results.getName());
        holder.totalDistance.setText(String.valueOf(results.getTotalDistance()));
        holder.totalElevationGain.setText(String.valueOf(results.getTotalElevationGain()));
        holder.totalTime.setText(String.valueOf(results.getTotalTime()));
        holder.averageSpeed.setText(String.valueOf(results.getAverageSpeed()));
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public class ResultsViewHolder extends RecyclerView.ViewHolder {
        TextView name, totalDistance, totalElevationGain, totalTime, averageSpeed;

        ResultsViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            totalDistance = itemView.findViewById(R.id.total_distance);
            totalElevationGain = itemView.findViewById(R.id.total_elevation_gain);
            totalTime = itemView.findViewById(R.id.total_time);
            averageSpeed = itemView.findViewById(R.id.average_speed);
        }
    }
}

