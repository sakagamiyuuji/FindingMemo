package com.e.findingmemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.drm.DrmStore;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fabAdd;
    private EditText edtAdd;
    private ArrayList<String> arrayList, listChecked;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    //item selected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initialComponent();
        generateLV();

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(multiChecker);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask2();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

                            case R.id.delete_all:
                                removeAll();
                                break;
                        }
                        return false;
                    }
                });
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
        View view = View.inflate(this, R.layout.layout_add_task, null);
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
        listChecked = new ArrayList<String>();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View view = View.inflate(this, R.layout.layout_add_task, null);
        edtAdd = view.findViewById(R.id.edtTxt);
        edtAdd.setText(arrayList.get(which_item));

        TextView title = view.findViewById(R.id.textTitle);
        title.setText(getString(R.string.edit));
        Button btnYes = view.findViewById(R.id.buttonYes);
        btnYes.setText(getString(R.string.apply));
        Button btnNo = view.findViewById(R.id.buttonNo);
        btnNo.setText(getString(R.string.cancel));

        final AlertDialog alert = builder.create();
        alert.setView(view);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEdt = edtAdd.getText().toString();
                boolean isEmptyInput = false;

                if(TextUtils.isEmpty(edtAdd.getText().toString())){
                    isEmptyInput = true;
                    edtAdd.setError("Field tidak boleh kosong");
                    //Toast.makeText(getApplicationContext(), "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else{
                    arrayList.set(which_item, textEdt);
                    reGenerate();
                    arrayAdapter.notifyDataSetChanged();
                    alert.dismiss();

                    Toast.makeText(getApplicationContext(), "Data telah di ubah", Toast.LENGTH_SHORT).show();
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

    private void removeAll(){
        arrayList.clear();
        arrayAdapter.notifyDataSetChanged();
        SharedPreferences sp = getSharedPreferences("sp_input", MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.clear();
        spEdit.apply();
    }

    private void removeSelected(List<String> items){
        for (String item: items){
            arrayList.remove(item);
            reGenerate();
        }
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.remove_all:
               removeAll();
               Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit:
                finish();
                break;
            default:
                return super.onContextItemSelected(item);


        }
        return true;
    }

    AbsListView.MultiChoiceModeListener multiChecker = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            if(listChecked.contains(arrayList.get(position))){
                listChecked.remove(arrayList.get(position));
                listView.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
            }

            else{
                listChecked.add(arrayList.get(position));
                listView.getChildAt(position).setBackgroundColor(Color.parseColor("#000000"));
            }
            mode.setTitle(listChecked.size()+ "Task Checked : (");
        }


        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.menucontext, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_delete:
                    AlertDialog.Builder builderHapus = new AlertDialog.Builder(MainActivity.this);
                    builderHapus.setTitle("Hapus Kegiatan Terpilih");
                    builderHapus.setMessage("Are You Sure? ");
                    builderHapus.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listView.setBackgroundColor(Color.TRANSPARENT);
                            Toast.makeText(getApplicationContext(), listChecked.size() + "Kegiatan terpilih berhasil di hapus", Toast.LENGTH_SHORT).show();
                            removeSelected(listChecked);
                            mode.finish();
                        }
                    });

                    builderHapus.setNegativeButton("Cancel", null);
                    builderHapus.create().show();

                case R.id.item_cancel_delete:
                    mode.finish();

                case R.id.menu_change:
                    editLV();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            listView.setAdapter(arrayAdapter);
            listChecked.clear();
        }
    };
}