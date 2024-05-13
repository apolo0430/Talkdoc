package com.example.talkdoc.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.talkdoc.R;
import com.example.talkdoc.UserInfo;
import com.example.talkdoc.databinding.FragmentMypageBinding;

public class MyPageFragment extends Fragment
{
    private FragmentMypageBinding binding;
    private UserInfo userInfo;

    private ImageView userImage;
    private TextView userName;
    private TextView userRelationship;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        userInfo = new UserInfo();

        binding = FragmentMypageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userName = root.findViewById(R.id.user_name);
        userRelationship = root.findViewById(R.id.user_relationship);

        userInfo.setName("박XX");
        userInfo.setRelationship("의사");

        userName.setText(userInfo.getName());
        userRelationship.setText(userInfo.getRelationship());

        return root;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}