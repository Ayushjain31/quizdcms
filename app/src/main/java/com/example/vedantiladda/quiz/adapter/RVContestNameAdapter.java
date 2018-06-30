package com.example.vedantiladda.quiz.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vedantiladda.quiz.R;
import com.example.vedantiladda.quiz.dto.ContestDTO;

import java.util.List;

public class RVContestNameAdapter extends RecyclerView.Adapter<RVContestNameAdapter.PostsViewHolder> {

    private List<ContestDTO> contestDTOList;
    private ContestDTO contestDTO;
    private ContestADapterCommunicator contestADapterCommunicator;

    public RVContestNameAdapter(List<ContestDTO> contestDTOList, ContestADapterCommunicator contestADapterCommunicator) {
        this.contestDTOList = contestDTOList;
        this.contestADapterCommunicator = contestADapterCommunicator;
    }

    @NonNull
    @Override
    public RVContestNameAdapter.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_staticcontest_name_adapter, viewGroup, false);
        return new RVContestNameAdapter.PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVContestNameAdapter.PostsViewHolder postsViewHolder, final int position) {
        contestDTO = contestDTOList.get(position);
        postsViewHolder.contestName.setText(String.valueOf(contestDTO.getContestName()));
        postsViewHolder.bonus.setText("Bonus: " + String.valueOf(contestDTO.getBonus()));
        postsViewHolder.noOfQuestions.setText("Questions: " + String.valueOf(contestDTO.getNumberOfQuestions()));
        postsViewHolder.duration.setText(String.valueOf("End Date: "+ contestDTO.getEndDate()));
        postsViewHolder.contestName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContestDTO contestDTO = contestDTOList.get(position);
                contestADapterCommunicator.onItemClick(contestDTO);

            }
        });


    }

    @Override
    public int getItemCount() {
        return contestDTOList.size();
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder{

        private TextView contestName, duration, noOfQuestions, bonus;

        public PostsViewHolder(View itemView){
            super(itemView);
            contestName = itemView.findViewById(R.id.contest_name);
            duration = itemView.findViewById(R.id.duration);
            noOfQuestions = itemView.findViewById(R.id.no_of_questions);
            bonus = itemView.findViewById(R.id.bonus);


        }
    }

    public interface ContestADapterCommunicator{
        public void onItemClick(ContestDTO contestDTO);
    }
}
