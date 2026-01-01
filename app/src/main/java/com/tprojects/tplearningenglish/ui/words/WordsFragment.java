package com.tprojects.tplearningenglish.ui.words;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tprojects.tplearningenglish.R;
import com.tprojects.tplearningenglish.data.dto.Words;
import com.tprojects.tplearningenglish.data.repositories.WordsRepository;

import java.util.ArrayList;

public class WordsFragment extends Fragment {

    private WordsViewModel viewModel;
    private WordAdapter adapter;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private ActivityResultLauncher<Intent> speechResultLauncher;
    private SearchView searchView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sonucu yakalamak için Launcher'ı tanımlıyoruz
        speechResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData()
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && !matches.isEmpty()) {
                            searchView.setQuery(matches.get(0),false);
                        }
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_words, container, false);
        DividerItemDecoration divider =
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerWords);
        recyclerView.addItemDecoration(divider);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageButton btnAddWord = view.findViewById(R.id.btnAddWord);
        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearch(query);
                viewModel.searchWords(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                handleSearch(query);
                viewModel.searchWords(query);
                return true;
            }

            private void handleSearch(String text) {

                if (text == null || text.trim().isEmpty()) {
                    viewModel.getWords();   // 🔄 TÜM LİSTE
                } else {
                    viewModel.searchWords(text);
                }
            }
        });

        searchView.setOnCloseListener(() -> {
            viewModel.getWords();
            return false;
        });

        btnAddWord.setOnClickListener(v -> showAddEditDialog(null));


        adapter = new WordAdapter(new WordAdapter.OnWordActionListener() {
            @Override
            public void onWordGet(Words word) {

            }

            @Override
            public void onWordClick(Words word) {
                Log.d("Tıklanan kelime: ", word.getWord().toString());
            }

            @Override
            public void onWordDelete(Words word) {
                showDeleteConfirmDialog(word);
            }

            @Override
            public void onWordEdit(Words word) {
                showWordDialog(word);
            }
        }, requireContext());

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            RecyclerView.ViewHolder target
                    ) {
                        return false;
                    }

                    @Override
                    public void onSwiped(
                            RecyclerView.ViewHolder viewHolder,
                            int direction
                    ) {
                        int position = viewHolder.getAdapterPosition();
                        Words word = adapter.getCurrentList()
                                .get(viewHolder.getAdapterPosition());
                        showDeleteConfirmDialog(word, position);
                    }
                });
        helper.attachToRecyclerView(recyclerView);


        recyclerView.setAdapter(adapter);

        btnAddWord.setOnClickListener(v -> {
            showAddEditDialog(null); // insert
        });

        ImageButton btnTr = view.findViewById(R.id.btnVoiceTr);
        ImageButton btnEn = view.findViewById(R.id.btnVoiceEn);

        btnTr.setOnClickListener(v -> startSpeechToText("tr-TR"));
        btnEn.setOnClickListener(v -> startSpeechToText("en-US"));

        speechResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData()
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && !matches.isEmpty()) {
                            searchView.setQuery(matches.get(0),false);
                        }
                    }
                }
        );
        viewModel = new ViewModelProvider(this).get(WordsViewModel.class);
        WordsRepository repository = new WordsRepository(requireContext());

        if (viewModel.getWords().getValue() == null) {
            viewModel.init(repository);
        }

        viewModel.getWords().observe(getViewLifecycleOwner(), words -> {
            adapter.submitList(words);
        });

        return view;
    }
    private void showDeleteConfirmDialog(Words word, int position) {

        new AlertDialog.Builder(requireContext())
                .setTitle("Silme Onayı")
                .setMessage("“" + word.getWord() + "” silinsin mi?")
                .setPositiveButton("Evet", (dialog, which) -> {
                    viewModel.deleteWord(word);
                })
                .setNegativeButton("İptal", (dialog, which) -> {
                    adapter.notifyItemChanged(position); // 🔄 GERİ AL
                })
                .setOnCancelListener(dialog -> {
                    adapter.notifyItemChanged(position); // BACK tuşu
                })
                .show();
    }
    private void showWordDialog(@Nullable Words editWord) {
        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_word, null);

        EditText edtWord = dialogView.findViewById(R.id.edtWord);
        EditText edtMean = dialogView.findViewById(R.id.edtMean);

        boolean isEdit = editWord != null;

        if (isEdit) {
            edtWord.setText(editWord.getWord());
            edtMean.setText(editWord.getMean());
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(isEdit ? "Kelime Güncelle" : "Kelime Ekle")
                .setView(dialogView)
                .setPositiveButton("Kaydet", (dialog, which) -> {
                    String word = edtWord.getText().toString().trim();
                    String mean = edtMean.getText().toString().trim();

                    if (word.isEmpty() || mean.isEmpty()) return;

                    if (isEdit) {
                        viewModel.updateWord(editWord.getId(), word, mean);
                    } else {
                        viewModel.insertWord(word, mean);
                    }
                })
                .setNegativeButton("İptal", null)
                .show();
    }

    private void showAddEditDialog(@Nullable Words word) {

        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_word, null);

        EditText etWord = view.findViewById(R.id.edtWord);
        EditText etMean = view.findViewById(R.id.edtMean);

        boolean isEdit = word != null;

        if (isEdit) {
            etWord.setText(word.getWord());
            etMean.setText(word.getMean());
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(isEdit ? "Kelime Düzenle" : "Kelime Ekle")
                .setView(view)
                .setPositiveButton(isEdit ? "Güncelle" : "Ekle", null)
                .setNegativeButton("İptal", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnPositive.setOnClickListener(v -> {

                String wordText = etWord.getText().toString().trim();
                String meanText = etMean.getText().toString().trim();

                if (wordText.isEmpty() || meanText.isEmpty()) {
                    Toast.makeText(requireContext(),
                            "Alanlar boş olamaz", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isEdit) {
                    word.setWord(wordText);
                    word.setMean(meanText);
                    viewModel.updateWord(word);
                } else {
                    viewModel.insertWord(wordText, meanText);
                }

                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void showDeleteConfirmDialog(Words word) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Silme Onayı")
                .setMessage("\"" + word.getWord() + "\" kelimesini silmek istiyor musunuz?")
                .setPositiveButton("Sil", (dialog, which) -> {
                    viewModel.deleteWord(word);
                })
                .setNegativeButton("İptal", null)
                .show();
    }

    private void startSpeechToText(String langCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, langCode);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Dinleniyor...");

        try {
            speechResultLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.releaseTts();
    }

}
