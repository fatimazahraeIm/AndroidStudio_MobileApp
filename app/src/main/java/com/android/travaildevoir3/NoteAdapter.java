package com.android.travaildevoir3;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Note> notes;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = new ArrayList<>(notes); // Utilisez une copie pour éviter les problèmes de référence
    }

    // Clear all items in the adapter
    public void clear() {
        notes.clear();
        notifyDataSetChanged(); // Notifiez le ListView que les données ont changé
    }

    // Add all items to the adapter
    public void addAll(ArrayList<Note> newNotes) {
        notes.addAll(newNotes);
        notifyDataSetChanged(); // Notifiez le ListView que les données ont changé
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_note, parent, false);
        }

        TextView textViewMatiere = convertView.findViewById(R.id.textViewMatiere);
        TextView textViewScore = convertView.findViewById(R.id.textViewScore);
        ImageView imageViewStatus = convertView.findViewById(R.id.imageViewStatus);

        Note note = notes.get(position);
        textViewMatiere.setText(note.getMatiere());
        textViewScore.setText(String.valueOf(note.getScore()));
        imageViewStatus.setImageResource(note.isGoodScore() ? R.drawable.ic_like : R.drawable.ic_dislike);

        return convertView;
    }
}
