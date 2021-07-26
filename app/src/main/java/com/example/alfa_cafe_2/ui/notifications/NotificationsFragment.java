package com.example.alfa_cafe_2.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.alfa_cafe_2.Analize;
import com.example.alfa_cafe_2.MainActivity;
import com.example.alfa_cafe_2.R;
import com.example.alfa_cafe_2.SplashActivity;
import com.example.alfa_cafe_2.databinding.FragmentNotificationsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    private RelativeLayout analize;
    private FirebaseDatabase bd;
    private DatabaseReference users;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "null";
    public static final String APP_PREFERENCES_KOL = "zero";
    public SharedPreferences mSettings;
    private LayoutInflater infl;
    private LinearLayout lin;
    private ArrayList<Drawable> foods = new ArrayList<Drawable>();


    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Drawable food1 = getContext().getDrawable(R.drawable.food1);
        Drawable food2 = getContext().getDrawable(R.drawable.food2);
        Drawable food3 = getContext().getDrawable(R.drawable.food3);
        Drawable food4 = getContext().getDrawable(R.drawable.food4);
        Drawable food5 = getContext().getDrawable(R.drawable.food5);
        Drawable food6 = getContext().getDrawable(R.drawable.food6);
        Drawable food7 = getContext().getDrawable(R.drawable.food7);
        Drawable food8 = getContext().getDrawable(R.drawable.food8);
        Drawable food9 = getContext().getDrawable(R.drawable.food9);
        Drawable food10 = getContext().getDrawable(R.drawable.food10);
        foods.add(food1);
        foods.add(food2);
        foods.add(food3);
        foods.add(food4);
        foods.add(food5);
        foods.add(food6);
        foods.add(food7);
        foods.add(food8);
        foods.add(food9);
        foods.add(food10);
        lin = root.findViewById(R.id.lin);
        infl = getLayoutInflater();
        bd = FirebaseDatabase.getInstance();
        mSettings = getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        users = bd.getReference("Users").child(mSettings.getString(APP_PREFERENCES_NAME, "null").replace(".","1"));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirestoreRequest();
            }
        }, 500);

        analize = root.findViewById(R.id.rel2);
        analize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Analize.class));

            }
        });

        return root;
    }
    private void FirestoreRequest(){
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(snapshot.getChildrenCount()>1) {
                        LinearLayout[] linn = new LinearLayout[(int) snapshot.getChildrenCount() - 1];
                        int i = 0;
                        for (DataSnapshot dates : snapshot.getChildren()) {
                            if (!dates.getKey().equals("recommends") && !dates.getKey().equals("classes")) {
                                Log.d("1", dates.toString());
                                linn[i] = (LinearLayout) infl.inflate(R.layout.list, null, false);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                params.setMargins(0, 40, 0, 0);
                                linn[i].setLayoutParams(params);
                                Log.d("1", String.valueOf(i));
                                TextView date = linn[i].findViewById(R.id.date);
                                date.setText(dates.getKey());
                                for (DataSnapshot things : dates.getChildren()) {
                                    Log.d("2", things.toString());
                                    View view = infl.inflate(R.layout.element_of_list, null, false);
                                    TextView type = view.findViewById(R.id.type);
                                    TextView name = view.findViewById(R.id.name);
                                    TextView money = view.findViewById(R.id.cost);
                                    ImageView type_img = view.findViewById(R.id.img_class);
                                    name.setText(things.child("food").getValue(String.class));
                                    type.setText(things.child("type").getValue(String.class));
                                    money.setText(String.valueOf(things.child("money").getValue(Long.class)) + " руб.");
                                    if (type.getText().toString().equals("Супы")) {
                                        type_img.setImageDrawable(foods.get(0));
                                    } else if (type.getText().toString().equals("Десерты")) {
                                        type_img.setImageDrawable(foods.get(1));
                                    } else if (type.getText().toString().equals("Напитки")) {
                                        type_img.setImageDrawable(foods.get(2));
                                    } else if (type.getText().toString().equals("Салаты")) {
                                        type_img.setImageDrawable(foods.get(3));
                                    } else if (type.getText().toString().equals("Горячее")) {
                                        type_img.setImageDrawable(foods.get(4));
                                    } else if (type.getText().toString().equals("Выпечка")) {
                                        type_img.setImageDrawable(foods.get(5));
                                    } else if (type.getText().toString().equals("Закуски")) {
                                        type_img.setImageDrawable(foods.get(6));
                                    } else if (type.getText().toString().equals("Гарниры")) {
                                        type_img.setImageDrawable(foods.get(7));
                                    } else if (type.getText().toString().equals("Соусы")) {
                                        type_img.setImageDrawable(foods.get(8));
                                    } else if (type.getText().toString().equals("Другое")) {
                                        type_img.setImageDrawable(foods.get(9));
                                    }
                                    linn[i].addView(view);
                                }
                                i++;
                            }
                        }
                        for (int k = linn.length - 1; k >= 0; k--) {
                            lin.addView(linn[k]);
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}