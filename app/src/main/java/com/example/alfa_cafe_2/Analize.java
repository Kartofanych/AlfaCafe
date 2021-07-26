package com.example.alfa_cafe_2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analize extends Activity {
    private PieChart pie;
    private FirebaseDatabase bd;
    private DatabaseReference info;
    public SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "null";
    public static final String APP_PREFERENCES_KOL = "zero";
    private long[] types_value = new long[10];
    private LayoutInflater inflater;
    private Drawable[] foods = new Drawable[10];
    private String[] types = {"Супы", "Десерты", "Напитки", "Салаты", "Горячее", "Выпечка", "Закуски", "Гарниры", "Соусы", "Другое"};

    private LinearLayout lin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analize);
        lin = findViewById(R.id.lin);

        Drawable food1 = getDrawable(R.drawable.food1);
        Drawable food2 = getDrawable(R.drawable.food2);
        Drawable food3 = getDrawable(R.drawable.food3);
        Drawable food4 = getDrawable(R.drawable.food4);
        Drawable food5 = getDrawable(R.drawable.food5);
        Drawable food6 = getDrawable(R.drawable.food6);
        Drawable food7 = getDrawable(R.drawable.food7);
        Drawable food8 = getDrawable(R.drawable.food8);
        Drawable food9 = getDrawable(R.drawable.food9);
        Drawable food10 = getDrawable(R.drawable.food10);
        foods[0] = food1;
        foods[1] = food2;
        foods[2] = food3;
        foods[3] = food4;
        foods[4] = food5;
        foods[5] = food6;
        foods[6] = food7;
        foods[7] = food8;
        foods[8] = food9;
        foods[9] = food10;

        pie = findViewById(R.id.pie_chart);
        inflater = getLayoutInflater();
        bd = FirebaseDatabase.getInstance();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        info = bd.getReference("Users").child(mSettings.getString(APP_PREFERENCES_NAME, "null").replace(".","1"));
        getPoints();
    }
    private void getPoints(){
        info.child("classes").orderByValue().addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                long sum = 0;
                for(DataSnapshot classes: snapshot.getChildren()){
                    if (classes.getKey().equals("Супы")) {
                        types_value[0] = (long) classes.getValue();
                    } else if (classes.getKey().equals("Десерты")) {
                        types_value[1] = (long) classes.getValue();
                    } else if (classes.getKey().equals("Напитки")) {
                        types_value[2] = (long) classes.getValue();
                    } else if (classes.getKey().equals("Салаты")) {
                        types_value[3] = (long) classes.getValue();
                    } else if (classes.getKey().equals("Горячее")) {
                        types_value[4] = (long) classes.getValue();
                    } else if (classes.getKey().equals("Выпечка")) {
                        types_value[5] = (long) classes.getValue();
                    } else if (classes.getKey().equals("Закуски")) {
                        types_value[6] = (long) classes.getValue();
                    } else if (classes.getKey().equals("Гарниры")) {
                        types_value[7] = (long) classes.getValue();
                    } else if (classes.getKey().equals("Соусы")) {
                        types_value[8] = (long) classes.getValue();
                    } else if (classes.getKey().equals("Другое")) {
                        types_value[9] = (long) classes.getValue();
                    }
                    sum += (long) classes.getValue();
                }
                Create_Pie(sum);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    int[] MY_COLORS = {Color.parseColor("#ff5023"), Color.parseColor("#f5c6af"), Color.parseColor("#e3523b"),
            Color.parseColor("#6eb825"), Color.parseColor("#855538"),Color.parseColor("#ff8000"), Color.parseColor("#d3843d")
            ,Color.parseColor("#f8cf26"),Color.parseColor("#ffeacc"), Color.parseColor("#dadada")};

    private void Create_Pie(long sum){
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int i = 0; i < 10; i++) {
            if(types_value[i] != 0) {
                pieEntries.add(new PieEntry(types_value[i]));
                colors.add(MY_COLORS[i]);
            }
        }

        long tempNum;
        Drawable tempName;
        int temp_color;
        String temp_types;

        for (int i = 0; i < types_value.length; i++)
        {
            for (int j = i + 1; j < types_value.length; j++)
            {
                if (types_value[i] > types_value[j])
                {
                    temp_types = types[i];
                    tempNum = types_value[i];
                    tempName=foods[i];
                    temp_color = MY_COLORS[i];

                    types_value[i] = types_value[j];
                    foods[i] = foods[j];
                    MY_COLORS[i] = MY_COLORS[j];
                    types[i] = types[j];

                    types_value[j] = tempNum;
                    foods[j] = tempName;
                    MY_COLORS[j] = temp_color;
                    types[j] = temp_types;
                }
            }
        }
        for(int i = 0; i < types_value.length; i++){
            Log.d("1", String.valueOf(types_value[i]) + " " + String.valueOf(foods[i]));
        }


        for(int i = types_value.length-1; i >= 0; i--){
            View view = inflater.inflate(R.layout.pie_element, null, false);
            ImageView dot = view.findViewById(R.id.dot);
            dot.setBackgroundColor(MY_COLORS[i]);
            ImageView type_img = view.findViewById(R.id.img_class);
            type_img.setImageDrawable(foods[i]);
            TextView cost = view.findViewById(R.id.cost);
            TextView type = view.findViewById(R.id.type);
            cost.setText(String.valueOf(types_value[i]) + "₽");
            type.setText(types[i]);
            lin.addView(view);


        }
        PieDataSet pieDataset = new PieDataSet(pieEntries, "");

        pieDataset.setColors(colors);


        PieData pieData = new PieData(pieDataset);

        pie.getLegend().setEnabled(false);
        pie.setData(pieData);
        pie.getData().setDrawValues(false);
        pie.setCenterText("Всего:\n" + String.valueOf(sum) + "₽");
        Typeface tf = ResourcesCompat.getFont(this, R.font.open_sans_semibold);
        pie.setDrawHoleEnabled(true);
        Log.d("1",String.valueOf(AppCompatDelegate.getDefaultNightMode()));

        int nightModeFlags =
                this.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:

                pie.setHoleColor(Color.BLACK);
                pie.setCenterTextColor(Color.WHITE);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                pie.setHoleColor(Color.WHITE);
                pie.setCenterTextColor(Color.BLACK);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }

        //if(AppCompatDelegate.getDefaultNightMode())

        pie.setCenterTextTypeface(tf);
        pie.setCenterTextSize(18f);
        pie.getDescription().setEnabled(false);
        //pie.animate();
        pie.spin( 2000,0,-720f, Easing.EaseOutCirc);

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(                                //скрываем нижнюю панель с кнопками
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |                      //скрываем нижнюю панель с кнопками
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |                           //скрываем нижнюю панель с кнопками
                        View.SYSTEM_UI_FLAG_FULLSCREEN |                                //скрываем нижнюю панель с кнопками
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //скрываем нижнюю панель с кнопками
    }
}
