package com.example.vedantiladda.quiz;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.vedantiladda.quiz.dto.ContestRulesDTO;
import com.example.vedantiladda.quiz.dto.QuestionDTO;

import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<QuestionDTO> questionDTOList;
    private Communicator communicator;
    private ContestRulesDTO rules;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String type;



    public PaginationAdapter(List<QuestionDTO> questionDTOList, Communicator communicator, ContestRulesDTO rules, String type) {
        this.questionDTOList = questionDTOList;
        this.communicator = communicator;
        this.rules = rules;
        this.type = type;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType==TYPE_ITEM){
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.question_holder, viewGroup,false);
            return new QuestionHolder(view);
        }
        else if(viewType==TYPE_HEADER){
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.question_header_holder, viewGroup,false);
            return new HeaderViewHolder(view);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        if(holder instanceof QuestionHolder) {
            final QuestionDTO questionDTO = getItem(i);
            Log.d("PAGINATION", questionDTO.getQuestionType());

            ((QuestionHolder)holder).questionType.setText(questionDTO.getQuestionType());
            ((QuestionHolder)holder).questionContent.setText(questionDTO.getQuestionText());
            ((QuestionHolder)holder).optionA.setText("A. " + questionDTO.getOptionOne());
            ((QuestionHolder)holder).optionB.setText("B. " + questionDTO.getOptionTwo());
            ((QuestionHolder)holder).optionC.setText("C. " + questionDTO.getOptionThree());
            ((QuestionHolder)holder).optionD.setText("D. " + questionDTO.getOptionFour());
            String answer = questionDTO.getAnswer();
            ((QuestionHolder)holder).answer.setText("Answer: " + answer);
            final String id = questionDTO.getQuestionId();
            ((QuestionHolder)holder).select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        questionDTO.setChecked(true);
                        ((QuestionHolder)holder).select.setChecked(questionDTO.getChecked());
                        communicator.onClickCheckBox(id);
                    } else {
                        questionDTO.setChecked(false);
                        ((QuestionHolder)holder).select.setChecked(questionDTO.getChecked());
                        communicator.onClickCheckBox(id);
                    }
                }
            });
            ((QuestionHolder)holder).select.setChecked(questionDTO.getChecked());
        }

        else if(holder instanceof HeaderViewHolder){
            if(type.equals("easy")) {
                ((HeaderViewHolder) holder).totalQ.setText("Easy "+ rules.getNumEasyQ().toString());
            }
            else if(type.equals("medium")){
                ((HeaderViewHolder) holder).totalQ.setText("Medium " +rules.getNumMediumQ().toString());
            }
            else if(type.equals("hard")){
                ((HeaderViewHolder) holder).totalQ.setText("Hard "+rules.getNumHardQ().toString());
            }
            ((HeaderViewHolder) holder).audioQ.setText("A "+rules.getNumAudioQ().toString());
            ((HeaderViewHolder) holder).imgQ.setText("I "+rules.getNumImageQ().toString());
            ((HeaderViewHolder) holder).videoQ.setText("V "+rules.getNumVideoQ().toString());
            ((HeaderViewHolder) holder).textQ.setText("T "+rules.getNumTextQ().toString());
        }


    }

    private QuestionDTO getItem(int position) {
        return questionDTOList.get(position);
    }

    @Override
    public int getItemCount() {
        return questionDTOList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public class QuestionHolder extends RecyclerView.ViewHolder{
        public TextView questionContent;
        public TextView questionType;
        public CheckBox select;
        public TextView optionA, optionB, optionC, optionD, answer;
        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            questionContent = itemView.findViewById(R.id.questionContent);
            questionType = itemView.findViewById(R.id.questionDifficulty);
            select = itemView.findViewById(R.id.checkBox);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            answer = itemView.findViewById(R.id.answer);

        }
    }
    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView totalQ, imgQ,textQ,audioQ,videoQ;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            totalQ = itemView.findViewById(R.id.totalQ);
            imgQ = itemView.findViewById(R.id.imgQ);
            textQ = itemView.findViewById(R.id.textQ);
            audioQ = itemView.findViewById(R.id.audioQ);
            videoQ = itemView.findViewById(R.id.videoQ);

        }
    }
    public interface Communicator {
        public void onClickCheckBox(String id);
    }

}

