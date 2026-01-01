package com.tprojects.tplearningenglish.ui.words;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;
import com.tprojects.tplearningenglish.R;
import com.tprojects.tplearningenglish.data.dto.Words;

public class WordAdapter
        extends ListAdapter<Words, WordAdapter.WordViewHolder> {

    public interface OnWordActionListener {
        void onWordGet(Words word);
        void onWordClick(Words word);
        void onWordDelete(Words word);
        void onWordEdit(Words word);


    }

    private final OnWordActionListener listener;
    private TextToSpeech textToSpeech;
    public WordAdapter(OnWordActionListener listener, Context context) {
        super(DIFF_CALLBACK);
        this.listener = listener;

        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setSpeechRate(0.9f);
            }
        });

        setHasStableIds(true);
    }

    // 🔥 DiffUtil burada
    private static final DiffUtil.ItemCallback<Words> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Words>() {

                @Override
                public boolean areItemsTheSame(
                        @NonNull Words oldItem,
                        @NonNull Words newItem
                ) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull Words oldItem,
                        @NonNull Words newItem
                ) {
                    return oldItem.equals(newItem);
                }
            };

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull WordViewHolder holder,
            int position
    ) {
        Words word = getItem(position);

        holder.txtWord.setText(word.getWord());
        holder.txtMean.setText(word.getMean());

        // 🇬🇧 İngilizce okuma
        holder.btnSpeakEn.setOnClickListener(v -> {
            textToSpeech.setLanguage(Locale.US);
            textToSpeech.speak(
                    word.getWord(),
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "EN_" + word.getId()
            );
        });

        // 🇹🇷 Türkçe okuma
        holder.btnSpeakTr.setOnClickListener(v -> {
            textToSpeech.setLanguage(new Locale("tr", "TR"));
            textToSpeech.speak(
                    word.getMean(),
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "TR_" + word.getId()
            );
        });

        holder.itemView.setOnClickListener(v -> {

        });

        holder.itemView.setOnLongClickListener(v -> {
            showPopupMenu(v, word);
            return true;
        });
    }
    public void releaseTts() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
    private void showPopupMenu(View view, Words word) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_word_item);

        // 🔹 Başlık = kelime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            popupMenu.getMenu().setGroupDividerEnabled(true);
        }
        popupMenu.getMenu().add(
                0,
                0,
                0,
                word.getWord()
        ).setEnabled(false);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (listener == null) return false;

            int id = item.getItemId();
            if (id == R.id.action_edit) {
                listener.onWordEdit(word);
                return true;
            } else if (id == R.id.action_delete) {
                listener.onWordDelete(word);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }


    static class WordViewHolder extends RecyclerView.ViewHolder {

        TextView txtWord, txtMean;
        ImageButton btnSpeakEn, btnSpeakTr;

        WordViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWord = itemView.findViewById(R.id.txtWord);
            txtMean = itemView.findViewById(R.id.txtMean);
            btnSpeakEn = itemView.findViewById(R.id.btnSpeakEn);
            btnSpeakTr = itemView.findViewById(R.id.btnSpeakTr);
        }
    }
}
