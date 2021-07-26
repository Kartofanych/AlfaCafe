package com.example.alfa_cafe_2.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.example.alfa_cafe_2.ApiClient;
import com.example.alfa_cafe_2.MainActivity;
import com.example.alfa_cafe_2.R;
import com.example.alfa_cafe_2.SplashActivity;
import com.example.alfa_cafe_2.databinding.FragmentHomeBinding;
import com.example.alfa_cafe_2.ui.Request.StringRequest;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.util.Base64;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private PhotoView img;
    private RelativeLayout download;
    private LottieAnimationView lottie;
    private Animation anim1, anim2;
    private TextView try_again;
    private ImageView add;
    private LinearLayout lin;
    private LayoutInflater inf;
    private RelativeLayout clear, less_go;
    private FirebaseDatabase bd;
    private DatabaseReference users, food;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "null";
    public static final String APP_PREFERENCES_KOL = "zero";
    public SharedPreferences mSettings;
    private Uri image_uri;
    private Button lets_go, dont_go;
    private ArrayList<Drawable> foods = new ArrayList<Drawable>();
    private String[] typez = {"Супы", "Десерты", "Напитки", "Салаты", "Горячее", "Выпечка", "Закуски", "Гарниры", "Соусы", "Другое"};
    private ImageView kitty_top;
    private ImageView txt1;
    private RelativeLayout plus;
    private View bottomSheet;
    private BottomSheetBehavior SmallSheet;
    private Drawable food11;
    private ImageView ex_img;
    private ScrollView scroll;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        inf = getLayoutInflater();
        scroll = root.findViewById(R.id.scroll);
        Bundle bundle = this.getArguments();
        kitty_top = root.findViewById(R.id.kitty_top);
        lets_go = root.findViewById(R.id.lets_go);
        dont_go = root.findViewById(R.id.not_go);
        less_go = root.findViewById(R.id.less_go);
        txt1 = root.findViewById(R.id.txt1);

        //extra_thing
        plus = root.findViewById(R.id.plus);
        ex_name = root.findViewById(R.id.ex_name);
        ex_money = root.findViewById(R.id.ex_money);
        ex_img = root.findViewById(R.id.img_classsss);
        ex_plus = root.findViewById(R.id.plus_it);


        bottomSheet = root.findViewById(R.id.bottom_sheet_success);
        SmallSheet = BottomSheetBehavior.from(bottomSheet);
        SmallSheet.setHideable(true);//Important to add
        SmallSheet.setSkipCollapsed(true);
        SmallSheet.setState(BottomSheetBehavior.STATE_HIDDEN);

        mSettings = getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
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
        food11 = getContext().getDrawable(R.drawable.star);
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

        download = root.findViewById(R.id.download);
        lottie = root.findViewById(R.id.lottie);

        anim1 = AnimationUtils.loadAnimation(getContext(),R.anim.alpha);
        anim2 = AnimationUtils.loadAnimation(getContext(),R.anim.alpha_to_zero);

        try_again = root.findViewById(R.id.try_again);
        clear = root.findViewById(R.id.clear);

        bd = FirebaseDatabase.getInstance();
        users = bd.getReference("Users");
        food = bd.getReference("Food");
        lin = root.findViewById(R.id.lin);
        add = root.findViewById(R.id.add);

        if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        img = root.findViewById(R.id.avatar);

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ImagePicker.with(HomeFragment.this)	//Final image size will be less than 1 MB(Optional)
                        .crop()	    			//Final image size will be less than 1 MB(Optional)
                        .start();
            }
        });
        if(bundle != null){

            if(bundle.getString("key").equals("1")){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ImagePicker.with(HomeFragment.this)	//Final image size will be less than 1 MB(Optional)
                                .crop()	    			//Final image size will be less than 1 MB(Optional)
                                .start();
                    }
                }, 500);

            }
        }
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Log.d("1", "1ffq");
            image_uri = data.getData();
            download.setVisibility(View.VISIBLE);
            try_again.setText("Downloading...");
            lottie.setAnimation(R.raw.red_dress);
            lottie.playAnimation();

            InputStream imageStream = null;
            try {
                imageStream = HomeFragment.this.getActivity().getContentResolver().openInputStream(image_uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            Request(selectedImage);

        }
    }
    private TextView ex_name, ex_money, ex_plus;
    private int lenght = 0;
    private void Request(Bitmap bitmap){

        String info =
                String.format("Info: size = %s x %s, bytes = %s (%s), config = %s",
                        bitmap.getWidth(),
                        bitmap.getHeight(),
                        bitmap.getByteCount(),
                        bitmap.getRowBytes(),
                        bitmap.getConfig());
        Log.d("1", info);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String image_str = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("1", String.valueOf(image_str.length()));


        Call<StringRequest> request = ApiClient.getUserService().request(image_str);
        request.enqueue(new Callback<StringRequest>() {
            @Override
            public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
                if(response.isSuccessful()) {
                    clear.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            plus.setVisibility(View.VISIBLE);
                            less_go.setVisibility(View.VISIBLE);
                            StringRequest req = response.body();


                            ex_name.setText(req.getTop());
                            final int mon = 30 + (int)(Math.random() * ((70 - 30) + 1));
                            ex_money.setText(String.valueOf(mon) + "руб.");
                            ex_plus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    View vv = inf.inflate(R.layout.element_of_list, null, false);
                                    TextView type = vv.findViewById(R.id.type);
                                    type.setText("Дополнительное блюдо");
                                    TextView name = vv.findViewById(R.id.name);
                                    name.setText(ex_name.getText().toString());
                                    TextView cost = vv.findViewById(R.id.cost);
                                    cost.setText(String.valueOf(mon) + "руб.");
                                    ImageView type_img = vv.findViewById(R.id.img_class);
                                    type_img.setImageDrawable(food11);
                                    lin.addView(vv);
                                    plus.setVisibility(View.GONE);
                                    lenght = 1;
                                }
                            });


                            byte[] encodeByte = Base64.decode(req.getByte(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);


                            kitty_top.setVisibility(View.GONE);
                            txt1.setVisibility(View.GONE);
                            img.setImageBitmap(bitmap);
                            img.setBackgroundColor(Color.WHITE);


                            if(lin.getChildCount() != 0){
                                lin.removeAllViews();
                            }
                            String[] food = new String[req.getStrings().length+lenght];
                            String[] types = new String[req.getStrings().length+lenght];
                            long[] money = new long[req.getStrings().length+lenght];
                            if(lenght == 1){
                                food[food.length-1] = req.getTop();
                                types[food.length-1] = req.getTop();
                                money[food.length-1] = 5;
                            }
                            for (int i = 0; i < req.getStrings().length; i++) {
                                View view = inf.inflate(R.layout.element_of_list, null, false);
                                TextView type = view.findViewById(R.id.type);
                                TextView name = view.findViewById(R.id.name);
                                TextView cost = view.findViewById(R.id.cost);
                                ImageView type_img = view.findViewById(R.id.img_class);
                                name.setText(req.getStrings()[i]);
                                type.setText(req.getTypes()[i]);
                                cost.setText(String.valueOf(req.getPrices()[i]) + " руб.");
                                food[i] = req.getStrings()[i];
                                types[i] = req.getTypes()[i];
                                money[i] = req.getPrices()[i];


                                if(types[i].equals("Супы")){
                                    type_img.setImageDrawable(foods.get(0));

                                }else if(types[i].equals("Десерты")){
                                    type_img.setImageDrawable(foods.get(1));
                                }else if(types[i].equals("Напитки")){
                                    type_img.setImageDrawable(foods.get(2));
                                }else if(types[i].equals("Салаты")){
                                    type_img.setImageDrawable(foods.get(3));
                                }else if(types[i].equals("Горячее")){
                                    type_img.setImageDrawable(foods.get(4));
                                }else if(types[i].equals("Выпечка")){
                                    type_img.setImageDrawable(foods.get(5));
                                }else if(types[i].equals("Закуски")){
                                    type_img.setImageDrawable(foods.get(6));
                                }else if(types[i].equals("Гарниры")){
                                    type_img.setImageDrawable(foods.get(7));
                                }else if(types[i].equals("Соусы")){
                                    type_img.setImageDrawable(foods.get(8));
                                }else if(types[i].equals("Другое")){
                                    type_img.setImageDrawable(foods.get(9));
                                }
                                lin.addView(view);
                            }
                            scroll.scrollTo(0,0);
                            download.setVisibility(View.GONE);
                            SmallSheet.setState(BottomSheetBehavior.STATE_EXPANDED);

                            Log.d("1", String.valueOf(food.length));
                            download.setAnimation(anim2);
                            anim2.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    download.setVisibility(View.GONE);
                                    SmallSheet.setState(BottomSheetBehavior.STATE_EXPANDED);

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            lets_go.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FirestoreRequest(food, types, money);
                                    Toast.makeText(getContext(), "Request successful!", Toast.LENGTH_LONG).show();
                                    SmallSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
                                    plus.setVisibility(View.GONE);
                                }
                            });
                            dont_go.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ImagePicker.with(HomeFragment.this)	//Final image size will be less than 1 MB(Optional)
                                            .crop()
                                            .start();
                                    SmallSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
                                    plus.setVisibility(View.GONE);

                                }
                            });
                        }
                    }, 1000);
                }else{
                    Log.d("1", "error");
                    download.setVisibility(View.VISIBLE);
                    try_again.setText("Try again");
                    try_click();
                    lottie.setAnimation(R.raw.trouble);
                    lottie.playAnimation();
                }

                    //Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        // img.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                    //img.setImageBitmap(Bitmap.createScaledBitmap(bmp, 400, 400, false));
                //img.setImageDrawable(new BitmapDrawable(mContext.getResources(), bm));
               // img.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));

            }

            @Override
            public void onFailure(Call<StringRequest> call, Throwable t) {
                Log.d("error", t.toString());
                download.setVisibility(View.VISIBLE);
                try_again.setText("Try again");
                try_click();
                lottie.setAnimation(R.raw.trouble);
                lottie.playAnimation();

            }
        });
    }
    private void try_click(){
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download.setVisibility(View.GONE);
            }
        });
    }
    long summa = 0;
    private void setClasses(String[] classes, long[] price){
        for(int i = 0; i < classes.length; i++) {
            summa = 0;
            int finalI = i;

            users.child(mSettings.getString(APP_PREFERENCES_NAME, "null").replace(".", "1")).child("classes").child(classes[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    summa = (long) snapshot.getValue();
                    summa = summa + price[finalI];
                    users.child(mSettings.getString(APP_PREFERENCES_NAME, "null").replace(".", "1")).child("classes").child(classes[finalI]).setValue(summa);

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }



    }
    private void FirestoreRequest(String[] food, String[] types, long[] money){

        for(int i = 0; i < food.length; i++) {
            Map<String, Object> user = new HashMap<>();
            user.put("food", food[i]);
            user.put("type", types[i]);
            user.put("money",money[i]);
            Date currentDate = new Date();
// Форматирование времени как "день.месяц.год"
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String dateText = dateFormat.format(currentDate);
            user.put("date", dateText);
            users.child(mSettings.getString(APP_PREFERENCES_NAME, "null").replace(".", "1")).child(dateText.replace(".", " ")).push().setValue(user);
        }

        FoodRequest(food, types);
        setClasses(types, money);


    }
    int sum = 0;
    private void FoodRequest(String[] foodArr, String[] typeee){
        for(int i = 0; i < foodArr.length; i++) {
            int finalI = i;
            sum = 0;
            Log.d("1", foodArr[i]);
            food.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.child(typeee[finalI]).hasChild(foodArr[finalI].replaceAll("[#./-]", "u"))){
                        sum = snapshot.child(typeee[finalI]).child(foodArr[finalI].replaceAll("[#./-]", "u")).getValue(Integer.class);
                        sum = sum + 1;
                        food.child(typeee[finalI]).child(foodArr[finalI].replaceAll("[#./-]", "u")).setValue(sum);
                    }else{
                        food.child(typeee[finalI]).child(foodArr[finalI].replaceAll("[#./-]", "u")).setValue(1);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}