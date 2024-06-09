package com.example.talkdoc.ui.list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.talkdoc.PatientInfo;
import com.example.talkdoc.R;
import com.example.talkdoc.SplashActivity;
import com.example.talkdoc.TranslationActivity;
import com.example.talkdoc.UserInfo;
import com.example.talkdoc.databinding.FragmentListBinding;
import com.example.talkdoc.server.GetPatientInfoTask;

import java.util.ArrayList;

public class ListFragment extends Fragment
{
    private FragmentListBinding binding;
    private CustomAdapter listViewAdapter;
    private ArrayList<PatientInfo> patientList;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        System.out.println(UserInfo.getInstance().getPatientName());

        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView listView = root.findViewById(R.id.listView);

        patientList = new ArrayList<>();
        listViewAdapter = new CustomAdapter(requireContext(), android.R.layout.simple_list_item_1, patientList);
        listView.setAdapter(listViewAdapter);

        new GetPatientInfoTask(new GetPatientInfoTask.OnPatientInfoReceived()
        {
            @Override
            public void onReceived(ArrayList<PatientInfo> updatedPatientList) {
                patientList.clear();
                patientList.addAll(updatedPatientList);
                listViewAdapter.notifyDataSetChanged();
            }
        }).execute("http://14.63.125.208:7000/api/user");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
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
        });

        return root;
    }

    private void showInputDialog()
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_input, null);

        final EditText editTextInput = dialogView.findViewById(R.id.edit_text_input);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("환자 이름 입력")
                .setView(dialogView)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = editTextInput.getText().toString().trim();
                        if (!inputName.isEmpty()) {
                            UserInfo.getInstance().setPatientName(inputName);
                        } else {
                            Toast.makeText(getContext(), "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private PatientInfo searchPatientByName(String name) {
        for (PatientInfo patient : patientList) {
            if (patient.getName().equalsIgnoreCase(name)) {
                return patient;
            }
        }

        return null;
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
            imageView.setImageBitmap(items.get(position).getImage());

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