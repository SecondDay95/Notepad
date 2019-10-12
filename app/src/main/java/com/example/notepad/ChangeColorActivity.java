package com.example.notepad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ChangeColorActivity extends AppCompatActivity {

    ListView lvColors;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_color);

        lvColors = (ListView) findViewById(R.id.lv_colors);
        lvColors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences = ChangeColorActivity.this.getSharedPreferences("Color", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.clear();
                if (position == 0) {
                    editor.putString("ChooseColor", "Black");
                }
                if (position == 1) {
                    editor.putString("ChooseColor", "Red");
                }
                if (position == 2) {
                    editor.putString("ChooseColor", "Green");
                }
                if (position == 3) {
                    editor.putString("ChooseColor", "Blue");
                }
                if (position == 4) {
                    editor.putString("ChooseColor", "Gray");
                }
                editor.commit();
                Intent intent = new Intent(ChangeColorActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
