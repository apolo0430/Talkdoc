package com.example.talkdoc.ui.checkup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.talkdoc.R;
import com.example.talkdoc.databinding.FragmentTeethCheckupBinding;
import com.example.talkdoc.server.GetTeethResultTask;

public class TeethCheckupFragment extends Fragment
{
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private FragmentTeethCheckupBinding binding;
    private ImageView imageView;
    private TextView resultTextView;
    private Bitmap imageBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentTeethCheckupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageView = binding.imageView; // imageView는 fragment_teeth_checkup.xml에 정의되어 있어야 합니다.
        Button button = binding.buttonTakePicture; // buttonTakePicture는 fragment_teeth_checkup.xml에 정의되어 있어야 합니다.
        resultTextView = binding.resultTextView; // resultTextView는 fragment_teeth_checkup.xml에 정의되어 있어야 합니다.

        button.setOnClickListener(v -> openCamera());

        return root;
    }

    private void openCamera()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            // 서버로 이미지를 전송하고 결과를 받아오는 비동기 작업 실행
            new GetTeethResultTask(resultTextView).execute(imageBitmap, "http://192.168.221.249:5000/predict");
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}
