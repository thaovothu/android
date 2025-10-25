package com.midterm.test.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.test.R;
import com.midterm.test.model.Answer;
import com.midterm.test.model.Question;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.VH> {

    public static class AnswerWithQuestion {
        public Answer answer;
        public Question question;
        public AnswerWithQuestion(Answer a, Question q) { answer = a; question = q; }
    }

    private List<AnswerWithQuestion> items;
    private ItemClickListener listener;

    public AnswerAdapter(List<AnswerWithQuestion> items, ItemClickListener l) {
        this.items = items;
        this.listener = l;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        AnswerWithQuestion awq = items.get(position);
        holder.tvQuestion.setText(awq.question.text);
        holder.tvAnswer.setText(awq.answer.selectedTrue ? "True" : "False");
        holder.root.setOnClickListener(v -> {
            listener.onItemClick(awq);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvQuestion, tvAnswer;
        LinearLayout root;
        VH(View v) {
            super(v);
            tvQuestion = v.findViewById(R.id.tv_q_text);
            tvAnswer = v.findViewById(R.id.tv_q_answer);
            root = v.findViewById(R.id.item_root);
        }
    }

    public interface ItemClickListener {
        void onItemClick(AnswerWithQuestion item);
    }
}
