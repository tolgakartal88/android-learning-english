package com.tprojects.tplearningenglish.ui.reading;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tprojects.tplearningenglish.databinding.FragmentReadingBinding;

public class ReadingFragment extends Fragment {
    FragmentReadingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        ReadingViewModel readingViewModel =
                new ViewModelProvider(this).get(ReadingViewModel.class);

        binding = FragmentReadingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
