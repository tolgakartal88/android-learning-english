package com.tprojects.tplearningenglish.ui.speaking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tprojects.tplearningenglish.databinding.FragmentSpeakingBinding;
import com.tprojects.tplearningenglish.ui.speaking.SpeakingViewModel;

public class SpeakingFragment extends Fragment {
    FragmentSpeakingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SpeakingViewModel speakingViewModel =
                new ViewModelProvider(this).get(SpeakingViewModel.class);

        binding = FragmentSpeakingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
