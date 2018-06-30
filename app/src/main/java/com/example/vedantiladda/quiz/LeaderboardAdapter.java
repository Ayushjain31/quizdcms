package com.example.vedantiladda.quiz;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vedantiladda.quiz.dto.OverallDTO;
import com.example.vedantiladda.quiz.dto.UserDTO;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardHolder> {

    //private List<UserDTO> users;
    private List<OverallDTO> overallDTOS;
    private LeaderboardAdapter.LeaderboardCommunicator communicator;

    public LeaderboardAdapter(List<OverallDTO> overallDTOS, LeaderboardAdapter.LeaderboardCommunicator communicator) {
        this.overallDTOS = overallDTOS;
        this.communicator = communicator;
    }

    @NonNull
    @Override
    public LeaderboardAdapter.LeaderboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_content_holder, parent,false);
        return new LeaderboardAdapter.LeaderboardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LeaderboardAdapter.LeaderboardHolder holder, int position) {
        final OverallDTO overallDTO = overallDTOS.get(position);
        holder.username.setText(overallDTO.getUserId());
        holder.points.setText(String.valueOf(overallDTO.getFinalPoints()));
        holder.rank.setText(String.valueOf(overallDTO.getRank()));
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OverallDTO overallDTO1 = overallDTOS.get(holder.getAdapterPosition());
                String id = overallDTO1.getUserId();
                communicator.onClickTextView(id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return overallDTOS.size();
    }

    public class LeaderboardHolder extends RecyclerView.ViewHolder{
        public ConstraintLayout constraintLayout;
        public TextView username;
        public TextView points;
        public TextView rank;

        public LeaderboardHolder(View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.lConstarintLayout);
            username = itemView.findViewById(R.id.UserName);
            points = itemView.findViewById(R.id.Points);
            rank = itemView.findViewById(R.id.Rank);
        }
    }

    public interface LeaderboardCommunicator{
        public void onClickTextView(String id);
    }
}

