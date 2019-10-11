package com.example.notepad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    Context context;
    List<Note> noteList = new ArrayList<>();
    Listener listener;
    int position1;

    public NotesAdapter () {

    }

    public NotesAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    public static interface Listener {
        public void onClick (int position);
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView cv = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_recycler_view, viewGroup, false );
        NotesViewHolder nvh = new NotesViewHolder(cv);
        return nvh;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesViewHolder notesViewHolder, final int position) {
        notesViewHolder.tvTitle.setText(noteList.get(position).getTitle());
        notesViewHolder.tvNote.setText(noteList.get(position).getNote());
        CardView cardView = notesViewHolder.cardView;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    position1 = position;
                    listener.onClick(position);
                }
            }
        });
        notesViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                position1 = position;
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView tvTitle,tvNote;
        CardView cardView;
        public NotesViewHolder(CardView itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.info_text);
            tvNote = itemView.findViewById(R.id.description_text);
            cardView = itemView;
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, v.getId(), 0, "Delete");

        }
    }

    public void setPosition1(int position1) {
        this.position1 = position1;
    }

    public int getPosition1() {
        return position1;
    }
}
