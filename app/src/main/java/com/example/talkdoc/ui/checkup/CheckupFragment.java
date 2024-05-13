package com.example.talkdoc.ui.checkup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.talkdoc.databinding.FragmentCheckupBinding;

public class CheckupFragment extends Fragment
{
    private FragmentCheckupBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        CheckupViewModel checkupViewModel = new ViewModelProvider(this).get(CheckupViewModel.class);

        binding = FragmentCheckupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCheckup;
        checkupViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}