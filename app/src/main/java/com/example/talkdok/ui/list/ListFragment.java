package com.example.talkdok.ui.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.talkdok.PatientInfo;
import com.example.talkdok.R;
import com.example.talkdok.databinding.FragmentListBinding;

import java.util.ArrayList;

public class ListFragment extends Fragment
{
    private FragmentListBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView listView = root.findViewById(R.id.listView);

        ArrayList<PatientInfo> patientList = new ArrayList<PatientInfo>();
        for (Integer i = 1; i <= 20; i++)
        {
            if (i < 10)
                patientList.add(new PatientInfo("김XX", "00" + i.toString()));
            else
                patientList.add(new PatientInfo("김XX", "0" + i.toString()));
        }

        CustomAdapter listViewAdapter = new CustomAdapter(requireContext(), android.R.layout.simple_list_item_1, patientList);

        listView.setAdapter(listViewAdapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // 클릭된 항목의 데이터 가져오기
                PatientInfo selectedPatient = (PatientInfo) parent.getItemAtPosition(position);

                // 새로운 액티비티 시작
                Intent intent = new Intent(requireActivity(), TranslationActivity.class);
                // 선택된 항목의 데이터를 넘겨줄 수 있음
                intent.putExtra("selectedPatient", selectedPatient);
                startActivity(intent);
            }
        });*/

        return root;
    }

    private class CustomAdapter extends ArrayAdapter<PatientInfo>
    {
        private ArrayList<PatientInfo> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<PatientInfo> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.patient_list, null);
            }

            // ImageView 인스턴스
            ImageView imageView = (ImageView) v.findViewById(R.id.patient_image);

            // 리스트뷰의 아이템에 이미지를 변경한다.
            imageView.setImageResource(R.drawable.ic_android_black);
            imageView.setColorFilter(R.color.purple_200);

            TextView nameText = (TextView) v.findViewById(R.id.patient_name);
            nameText.setText(items.get(position).getName());

            TextView numText = (TextView) v.findViewById(R.id.patient_number);
            numText.setText("# " + items.get(position).getNumber());

            return v;
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}