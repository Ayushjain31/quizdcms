package com.example.vedantiladda.quiz.QuizMaster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.vedantiladda.quiz.R;
import com.example.vedantiladda.quiz.dto.ContestQuestionDTO;

import java.util.Collections;
import java.util.List;

public class Quiz_mater_question_recycler_view_adapter extends RecyclerView.Adapter<Quiz_mater_question_recycler_view_adapter.View_Holder> {
    List<ContestQuestionDTO> QmQuestionlist= Collections.emptyList();
    Context context;
    IPostsAdapterCommunicatorQuestionQuizMaster iPostsAdapterCommunicatorQuestionQuizMaster;
    public Quiz_mater_question_recycler_view_adapter(List<ContestQuestionDTO> QmQuestionlist, Context context, IPostsAdapterCommunicatorQuestionQuizMaster iPostsAdapterCommunicatorQuestionQuizMaster){
        this.context=context;
        this.QmQuestionlist=QmQuestionlist;
        this.iPostsAdapterCommunicatorQuestionQuizMaster=iPostsAdapterCommunicatorQuestionQuizMaster;
    }


    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_quiz_mater_question_recycler_view_adapter,parent,false);
        View_Holder holder =new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final View_Holder holder, final int position) {
        holder.questionContent.setText(QmQuestionlist.get(position).getQuestionDTO().getQuestionText());
        Log.i("RV",QmQuestionlist.get(position).getQuestionDTO().getQuestionText());



       // holder.push.setEnabled(QmQuestionlist.get(position).getVisible());


        //holder.time.setText();


        holder.push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // intent method
                  holder.push.setEnabled(false);
                 //


                String contestQuestionId=QmQuestionlist.get(position).getContestQuestionId();
                iPostsAdapterCommunicatorQuestionQuizMaster.itemClick(contestQuestionId);

            }
        });
    }

    @Override
    public int getItemCount() {
        return QmQuestionlist.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        TextView questionContent;
        TextView time;
        Button push;
        public View_Holder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            questionContent=(TextView)itemView.findViewById(R.id.question_id);
            //time=(TextView)itemView.findViewById(R.id.timeduration_id);
            push=(Button) itemView.findViewById(R.id.push_question_id);
        }
    }
    public interface IPostsAdapterCommunicatorQuestionQuizMaster{
            void itemClick(String contestQuestionId);
    }
}
