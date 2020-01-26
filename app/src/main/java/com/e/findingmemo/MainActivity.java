package com.e.findingmemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fabAdd;
    private EditText edtAdd;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initialComponent();
        generateLV();


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask2();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final  int key = position;
                PopupMenu popUp = new PopupMenu(getApplicationContext(), view, Gravity.END);
                popUp.getMenuInflater().inflate(R.menu.option_lv, popUp.getMenu());
                popUp.show();
                popUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_edit:
                                editLV(key);
                                break;

                            case R.id.menu_remove:
                                remove(key);
                                break;
                        }
                        return false;
                    }
                });
                return false;
            }
        });

    }


    private void initialComponent(){
        /*View view = View.inflate(this, R.layout.for_list, null);
        tvList = view.findViewById(R.id.tvList);*/

        fabAdd = findViewById(R.id.fab_create);
        listView = findViewById(R.id.listView);
    }

    // CARA 1
    /*private void addTask(){
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
                Boolean isEmptyInput = false;

                if(TextUtils.isEmpty(edtAdd.getText().toString())) {
                    isEmptyInput = true;
                    edtAdd.setError("Field tidak boleh kosong");
                    //Toast.makeText(getApplicationContext(), "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else {
                    arrayList.add(newKey, input);
                    addToSP(newKey, input);
                    arrayAdapter.notifyDataSetChanged();
                    //succesDialog();

                    Toast.makeText(getApplicationContext(), "Data telah di tambahkan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.create().show();
    }
*/
    //CARA 2
    private void addTask2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View view = View.inflate(this, R.layout.layout_warning_dialog, null);
        edtAdd = view.findViewById(R.id.edtTxt);
        Button btnYes = view.findViewById(R.id.buttonYes);
        Button btnNo = view.findViewById(R.id.buttonNo);

        final AlertDialog alert = builder.create();
        alert.setView(view);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newKey = arrayList.size();
                String input = edtAdd.getText().toString();
                Boolean isEmptyInput = false;

                if(TextUtils.isEmpty(edtAdd.getText().toString())) {
                    isEmptyInput = true;
                    edtAdd.setError("Field tidak boleh kosong");
                    //Toast.makeText(getApplicationContext(), "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else {
                    arrayList.add(newKey, input);
                    addToSP(newKey, input);
                    arrayAdapter.notifyDataSetChanged();
                    //succesDialog();
                    alert.dismiss();


                    Toast.makeText(getApplicationContext(), "Data telah di tambahkan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
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
        if (sp.getAll().size() > 0){
            for (int i = 0; i < sp.getAll().size(); i++){
                String key = String.valueOf(i);
                arrayList.add(sp.getString(key, null));
            }
        }
    }

    private void remove(int position){
        final int which_item = position;

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Delete Forever")
                .setMessage("Are you sure?")
                .setIcon(R.drawable.ic_delete_forever_red_24dp)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arrayList.remove(which_item);
                        reGenerate();

                        arrayAdapter.notifyDataSetChanged();
                    }
                })

                .setNegativeButton("NO", null)
                .create()
                .show();
    }

    private void editLV(int position){
        final int which_item = position;

        View view = View.inflate(this, R.layout.add_layout, null);
        edtAdd = view.findViewById(R.id.edt_add);
        edtAdd.setText(arrayList.get(which_item));

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Edit");
        alert.setView(view);
        alert.setNegativeButton("Cancel", null);
        alert.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String textEdt = edtAdd.getText().toString();
                arrayList.set(which_item, textEdt);
                reGenerate();
                arrayAdapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Data telah di ubah", Toast.LENGTH_SHORT).show();
            }
        });

        alert.create().show();
    }

    private void reGenerate(){
        SharedPreferences sp = getSharedPreferences("sp_input", MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.clear();
        spEdit.apply();

        for (int i=0; i<arrayList.size(); i++){
            String key = String.valueOf(i);
            spEdit.putString(key, arrayList.get(i));
        }
        spEdit.apply();

    }


/*    private void option(){
        View view = View.inflate(this,R.layout.option_long_click, null);
        optionBtn = view.findViewById(R.id.edit);

        AlertDialog.Builder option = new AlertDialog.Builder(this);
        option.setView(view);

        option.create().show();
    }*/
}