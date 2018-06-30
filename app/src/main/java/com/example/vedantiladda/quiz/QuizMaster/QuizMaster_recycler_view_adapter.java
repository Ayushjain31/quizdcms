package com.example.vedantiladda.quiz.QuizMaster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vedantiladda.quiz.R;
import com.example.vedantiladda.quiz.dto.ContestDTO;

import java.util.Collections;
import java.util.List;

public class QuizMaster_recycler_view_adapter extends RecyclerView.Adapter<QuizMaster_recycler_view_adapter.View_Holder> {
    List<ContestDTO> listDC;//=Collections.emptyList();
    Context context;
    IPostsAdapterCommunicatorDynamicContests iPostsAdapterCommunicatorDynamicContests;
    public QuizMaster_recycler_view_adapter(List<ContestDTO> listDC, Context context, IPostsAdapterCommunicatorDynamicContests iPostsAdapterCommunicatorDynamicContests){
        this.context=context;
        this.listDC=listDC;
        this.iPostsAdapterCommunicatorDynamicContests=iPostsAdapterCommunicatorDynamicContests;
    }
    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_quiz_master_recycler_view_adapter,parent,false);
        View_Holder holder =new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, final int position) {
        holder.contestName.setText(listDC.get(position).getContestName());
        //holder.startTime.setText(String.valueOf(listDC.get(position).getStartDate()));
        holder.contestName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String contestId=listDC.get(position).getContestId();
            iPostsAdapterCommunicatorDynamicContests.itemClick(contestId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listDC.size();
    }


    public interface IPostsAdapterCommunicatorDynamicContests {
        void itemClick(String contestType);

        // public void categoryItemClick(String category);
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        TextView contestName;
        TextView startTime;
        public View_Holder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            contestName=(TextView)itemView.findViewById(R.id.contestName_id);
            startTime=(TextView)itemView.findViewById(R.id.startTime_id);
        }
    }
}
