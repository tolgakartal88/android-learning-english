package com.tprojects.tplearningenglish.ui.words;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tprojects.tplearningenglish.data.dto.Words;
import com.tprojects.tplearningenglish.data.repositories.WordsRepository;

import java.util.List;

public class WordsViewModel extends ViewModel {

    private WordsRepository repository;
    private final MutableLiveData<List<Words>> wordsLiveData = new MutableLiveData<>();

    /**
     * ViewModel ilk oluşturulduğunda çağrılır
     */
    public void init(WordsRepository repository) {
        this.repository = repository;
        loadWords();
    }

    private void loadWords() {
        wordsLiveData.setValue(repository.getWords());
    }

    public LiveData<List<Words>> getWords() {
        return wordsLiveData;
    }

    public void deleteWord(Words word) {
        repository.deleteWord(word.getId());
        wordsLiveData.setValue(repository.getWords());
    }
    public void updateWord(Words word) {
        repository.updateWord(
                word.getId(),
                word.getWord(),
                word.getMean()
        );
        wordsLiveData.setValue(repository.getWords());
    }

    public void insertWord(String word, String mean) {
        repository.insertWord(word, mean);
        wordsLiveData.setValue(repository.getWords());
    }

    public void updateWord(int id, String word, String mean) {
        repository.updateWord(id, word, mean);
        wordsLiveData.setValue(repository.getWords());
    }

    public void searchWords(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadWords();
        } else {
            wordsLiveData.setValue(repository.searchWords(query));
        }
    }
}
