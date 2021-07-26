package com.example.alfa_cafe_2.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.alfa_cafe_2.AboutUs;
import com.example.alfa_cafe_2.MainActivity;
import com.example.alfa_cafe_2.R;
import com.example.alfa_cafe_2.SplashActivity;
import com.example.alfa_cafe_2.databinding.FragmentDashboardBinding;
import com.example.alfa_cafe_2.ui.home.HomeFragment;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    private FirebaseDatabase db;
    private DatabaseReference ref;
    private DatabaseReference food_ref;
    private DatabaseReference otz;
    private Button do_it;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "null";
    public static final String APP_PREFERENCES_KOL = "zero";
    public SharedPreferences mSettings;
    private LayoutInflater infl;
    private LinearLayout lin;
    private String[] typez = {"Супы", "Десерты", "Напитки", "Салаты", "Горячее", "Выпечка", "Закуски", "Гарниры", "Соусы", "Другое"};
    private String[][] recomendations = new String[9][3];
    private Button scan_it;
    private View layout;
    private TextView first, second, third;
    private ArrayList<Drawable> food = new ArrayList<Drawable>();
    private EditText name, email, text;
    private Button send;
    private View bottomSheet, bottom_big_sheet;
    private LinearLayout big_lin;
    private ImageView group;
    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        infl = getLayoutInflater();

        scan_it = root.findViewById(R.id.scan_itt);
        name = root.findViewById(R.id.name);
        email = root.findViewById(R.id.email);
        text = root.findViewById(R.id.otz_txt);
        send = root.findViewById(R.id.send);

        barChart = root.findViewById(R.id.BarChart);
        bottomSheet = root.findViewById( R.id.bottom_sheet );
        bottom_big_sheet = root.findViewById(R.id.bottom_big_sheet);

        first = root.findViewById(R.id.first);
        second = root.findViewById(R.id.second);
        third = root.findViewById(R.id.third);
        group = root.findViewById(R.id.group);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), AboutUs.class));
            }
        });

        SmallSheet = BottomSheetBehavior.from(bottomSheet);
        BigSheet = BottomSheetBehavior.from(bottom_big_sheet);
        big_lin = root.findViewById(R.id.lin_big);
        SmallSheet.setHideable(true);//Important to add
        SmallSheet.setSkipCollapsed(true);
        SmallSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        BigSheet.setHideable(true);//Important to add
        BigSheet.setSkipCollapsed(true);
        BigSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        do_it = root.findViewById(R.id.do_it);

        mSettings = getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Users");
        food_ref = db.getReference("Food");
        otz = db.getReference("Stars");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendText();
            }
        });
        setBigLinItems();


        Drawable food1 = getContext().getDrawable(R.drawable.food1);
        Drawable food2 = getContext().getDrawable(R.drawable.food2);
        Drawable food3 = getContext().getDrawable(R.drawable.food3);
        Drawable food4 = getContext().getDrawable(R.drawable.food4);
        Drawable food5 = getContext().getDrawable(R.drawable.food5);
        Drawable food6 = getContext().getDrawable(R.drawable.food6);
        Drawable food7 = getContext().getDrawable(R.drawable.food7);
        Drawable food8 = getContext().getDrawable(R.drawable.food8);
        Drawable food9 = getContext().getDrawable(R.drawable.food9);
        food.add(food1);
        food.add(food2);
        food.add(food3);
        food.add(food4);
        food.add(food5);
        food.add(food6);
        food.add(food7);
        food.add(food8);
        food.add(food9);
        lin = root.findViewById(R.id.lin);


        do_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                big_sheet();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Check_it();
                FirebaseRequest();
            }
        }, 500);
        layout = infl.inflate(R.layout.fade_popup,
                (ViewGroup) root.findViewById(R.id.fadePopup));

        params.setMargins(40, 40, 0, 40);

        scan_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.navigation_home);
            }
        });
        SmallSheet.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                if(SmallSheet.getState() == BottomSheetBehavior.STATE_HIDDEN){
                    //arraylist.clear();
                   barChart.invalidate();
                   barChart.clear();
                }
            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {

            }
        });



        return root;
    }
    private void Check_it(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(!snapshot.child(mSettings.getString(APP_PREFERENCES_NAME, "null").replace(".", "1")).hasChild("classes")){
                    for(int i = 0; i < typez.length; i++) {
                        ref.child(mSettings.getString(APP_PREFERENCES_NAME, "null").replace(".", "1")).child("classes").child(String.valueOf(typez[i])).setValue(0);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    private void FirebaseRequest(){
        for(int i = 0; i < 9; i++){
            LinearLayout view = (LinearLayout) infl.inflate(R.layout.element_of_types, null, false);
            view.setLayoutParams(params);
            ImageView icon = view.findViewById(R.id.img_type);
            if(i == 0){
                icon.setImageDrawable(food.get(0));
            }else if(i == 1){
                icon.setImageDrawable(food.get(1));
            }else if(i == 2){
                icon.setImageDrawable(food.get(2));
            }else if(i == 3){
                icon.setImageDrawable(food.get(3));
            }else if(i == 4){
                icon.setImageDrawable(food.get(4));
            }else if(i == 5){
                icon.setImageDrawable(food.get(5));
            }else if(i == 6){
                icon.setImageDrawable(food.get(6));
            }else if(i == 7){
                icon.setImageDrawable(food.get(7));
            }else if(i == 8){
                icon.setImageDrawable(food.get(8));
            }

            TextView type = view.findViewById(R.id.type);
            type.setText(typez[i]);
            Button bt = view.findViewById(R.id.more);
            int finalI = i;
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dimBackground(typez[finalI]);
                }
            });
            lin.addView(view);
        }
    }
    private BottomSheetBehavior BigSheet;

    private void big_sheet(){
        BigSheet.setState(BottomSheetBehavior.STATE_EXPANDED);

    }
    private BottomSheetBehavior SmallSheet;
    private BarChart barChart;
    private int j = 0;
    private void dimBackground(String Key) {
        ArrayList<Integer> valueList = new ArrayList<Integer>();
        int[] a = new int[3];
        String[] keys = new String[3];
        j = 0;
        food_ref.child(Key).orderByValue().limitToLast(3).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                j = 0;
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    valueList.add(childSnapshot.getValue(Integer.class));
                    a[j] = childSnapshot.getValue(Integer.class);
                    keys[j] = childSnapshot.getKey();
                    j++;
                    Log.d("all", childSnapshot.getValue(Integer.class).toString());

                }
                barChart.setDrawBarShadow(false);
                barChart.setDrawValueAboveBar(true);
                barChart.setMaxVisibleValueCount(50);
                barChart.setPinchZoom(false);
                barChart.setDrawGridBackground(false);
                first.setText(keys[0].replace("u", "/"));
                second.setText(keys[1].replace("u", "/"));
                third.setText(keys[2].replace("u", "/"));

                ArrayList<BarEntry> barEntries = new ArrayList<>();
                barEntries.add(new BarEntry(1,valueList.get(2) * 1f));
                barEntries.add(new BarEntry(2,valueList.get(1) * 1f));
                barEntries.add(new BarEntry(3,valueList.get(0) * 1f));


                BarDataSet barDataSet = new BarDataSet(barEntries, "Title");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                BarData barData = new BarData(barDataSet);
                barData.setBarWidth(0.9f);
                barData.setDrawValues(false);


                barChart.setData(barData);
                barChart.getDescription().setEnabled(false);
                barChart.getLegend().setEnabled(false);
                barChart.getXAxis().setEnabled(false);
                barChart.getAxisLeft().setEnabled(false);
                Typeface tf = ResourcesCompat.getFont(getContext(), R.font.open_sans_semibold);
                barChart.setNoDataTextTypeface(tf);
                barDataSet.setValueTypeface(tf);

                barChart.setDrawValueAboveBar(false);


                XAxis labels = barChart.getXAxis();
                YAxis labels1 = barChart.getAxisRight();
                int nightModeFlags =
                        getContext().getResources().getConfiguration().uiMode &
                                Configuration.UI_MODE_NIGHT_MASK;
                switch (nightModeFlags) {
                    case Configuration.UI_MODE_NIGHT_YES:

                        labels.setTextColor(getResources().getColor(android.R.color.white));
                        labels1.setTextColor(getResources().getColor(android.R.color.white));
                        barChart.setNoDataTextColor(Color.WHITE);
                        barDataSet.setValueTextColor(Color.WHITE);
                        break;

                    case Configuration.UI_MODE_NIGHT_NO:

                        labels.setTextColor(getResources().getColor(android.R.color.black));
                        labels1.setTextColor(getResources().getColor(android.R.color.black));
                        barChart.setNoDataTextColor(Color.BLACK);
                        barDataSet.setValueTextColor(Color.BLACK);
                        break;

                    case Configuration.UI_MODE_NIGHT_UNDEFINED:
                        break;
                }


                YAxis y = barChart.getAxisRight();
                y.setLabelCount(1);
                int b = Math.max(a[0], Math.max(a[1], a[2]));
                y.setAxisMaxValue(b*1.5f);
                y.setAxisMinValue(0);
                YAxis y1 = barChart.getAxisLeft();
                y1.setLabelCount(1);
                y1.setAxisMaxValue(b*1.5f);
                y1.setAxisMinValue(0);

                barChart.animateY(1000, Easing.EaseInCubic);

                }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        SmallSheet.setState(BottomSheetBehavior.STATE_EXPANDED);

    }
    private class Message{
        String name_;
        String email_;
        String txt_;

        public String getName_() {
            return name_;
        }

        public void setName_(String name_) {
            this.name_ = name_;
        }

        public String getEmail_() {
            return email_;
        }

        public void setEmail_(String email_) {
            this.email_ = email_;
        }

        public String getTxt_() {
            return txt_;
        }

        public void setTxt_(String txt_) {
            this.txt_ = txt_;
        }
    }
    private void SendText(){
        Message message = new Message();
        message.setName_(name.getText().toString());
        message.setEmail_(email.getText().toString());
        message.setTxt_(text.getText().toString());
        otz.push().setValue(message);
        Toast.makeText(getContext(), "Спасибо за отзыв!", Toast.LENGTH_LONG).show();
    }

    private void setBigLinItems(){

        food_ref.child("Супы").orderByValue().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    CreateElement(snapshot.getKey(), String.valueOf(dataSnapshot.getKey()).replaceAll("[{}]", "").replace("u", "/"));
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        food_ref.child("Горячее").orderByValue().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    CreateElement(snapshot.getKey(), String.valueOf(dataSnapshot.getKey()).replaceAll("[{}]", "").replace("u", "/"));
                }            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        food_ref.child("Напитки").orderByValue().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    CreateElement(snapshot.getKey(), String.valueOf(dataSnapshot.getKey()).replaceAll("[{}]", "").replace("u", "/"));
                }            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        food_ref.child("Десерты").orderByValue().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    CreateElement(snapshot.getKey(), String.valueOf(dataSnapshot.getKey()).replaceAll("[{}]", "").replace("u", "/"));
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        food_ref.child("Закуски").orderByValue().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    CreateElement(snapshot.getKey(), String.valueOf(dataSnapshot.getKey()).replaceAll("[{}]", "").replace("u", "/"));
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
   private void CreateElement(String key, String value){
        View vv = infl.inflate(R.layout.element_of_list, null, false);
        TextView type = vv.findViewById(R.id.type);
        TextView name = vv.findViewById(R.id.name);
        name.setMaxLines(2);
        name.setText(value);
        TextView cost = vv.findViewById(R.id.cost);
        cost.setVisibility(View.GONE);
        ImageView type_img = vv.findViewById(R.id.img_class);
        if(key.equals("Супы")){
            type.setText("Супы");
            type_img.setImageDrawable(food.get(0));
        }else if(key.equals("Горячее")){
            type.setText("Горячее");
            type_img.setImageDrawable(food.get(4));
        }else if(key.equals("Напитки")){
            type_img.setImageDrawable(food.get(2));
            type.setText("Напитки");
        }else if(key.equals("Десерты")){
            type_img.setImageDrawable(food.get(1));
            type.setText("Десерты");
        }else if(key.equals("Закуски")){
            type_img.setImageDrawable(food.get(6));
            type.setText("Закуски");
        }
        big_lin.addView(vv);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}