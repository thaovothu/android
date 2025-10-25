package com.midterm.bt3_22_8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton btnAdd;
    private TextView tvCount;
    private MyViewModel model;
    private ListView lvCount;
    private ArrayList<Integer> arrayList;
    private ArrayAdapter<Integer> arrayAdapter;
    private ActivityResultLauncher<Intent> editLauncher;
    private int currentPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnAdd = findViewById(R.id.btn_add);
        tvCount = findViewById(R.id.tv_count);
        lvCount = findViewById(R.id.lv_count);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        lvCount.setAdapter(arrayAdapter);

        model = new ViewModelProvider(this).get(MyViewModel.class);

        model.getNumber().observe(this, integer -> {
            tvCount.setText("" + integer);
            arrayList.add(integer);
            arrayAdapter.notifyDataSetChanged();
        });

        editLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String updatedValue = data.getStringExtra("result");
                        if (updatedValue != null && currentPosition != -1) {
                            try {
                                arrayList.set(currentPosition, Integer.parseInt(updatedValue));
                                arrayAdapter.notifyDataSetChanged();
                            } catch (NumberFormatException e) {
                                // Handle case where the returned string is not a valid integer
                            }
                        }
                    }
                });

        btnAdd.setOnClickListener(view -> model.increaseNumber());
        lvCount.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                arrayList.remove(position);
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });

        lvCount.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                Intent intent = new Intent (MainActivity.this, DetailActivity.class);
                intent.putExtra("number",arrayList.get(position).toString());
                editLauncher.launch(intent);
            }
        });
    }
}
