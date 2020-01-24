package com.e.findingmemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fabAdd;
    private EditText edtAdd;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private Button optionBtn;
    private TextView tvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initialComponent();
        generateLV();


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

    }


    private void initialComponent(){
        /*View view = View.inflate(this, R.layout.for_list, null);
        tvList = view.findViewById(R.id.tvList);*/

        fabAdd = findViewById(R.id.fab_create);
        listView = findViewById(R.id.listView);
    }

    private void addTask(){
        View view = View.inflate(this, R.layout.add_layout, null);
        edtAdd = view.findViewById(R.id.edt_add);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add Task");
        alert.setMessage("Create new task?");
        alert.setView(view);
        alert.setNegativeButton("NO", null);
        alert.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int newKey = arrayList.size();
                String input = edtAdd.getText().toString();
                arrayList.add(newKey, input);
                addToSP(newKey, input);

                arrayAdapter.notifyDataSetChanged();

            }

        });

        alert.create().show();
    }

    private void addToSP(int key, String value){
        SharedPreferences sp = getSharedPreferences("sp_input", MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        String keyString = String.valueOf(key);
        spEdit.putString(keyString,value);
        spEdit.apply();
    }

    private void generateLV(){
        arrayList = new ArrayList<String>();
        showSP();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.for_list, R.id.tvList, arrayList);
        listView.setAdapter(arrayAdapter);
    }

    private void showSP(){
        SharedPreferences sp = getSharedPreferences("sp_input", MODE_PRIVATE);
        if (sp.getAll().size() != 0){
            for (int i = 0; i < sp.getAll().size(); i++){
                String key = String.valueOf(i);
                arrayList.add(sp.getString(key, null));
            }
        }
    }

/*    private void option(){
        View view = View.inflate(this,R.layout.option_long_click, null);
        optionBtn = view.findViewById(R.id.edit);

        AlertDialog.Builder option = new AlertDialog.Builder(this);
        option.setView(view);

        option.create().show();
    }*/

}