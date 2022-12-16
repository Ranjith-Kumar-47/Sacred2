package com.example.playvideota;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.playvideota.adapter.YoutuberAdapter;
import com.example.playvideota.api.MySingleton;
import com.example.playvideota.databinding.ActivityMainBinding;
import com.example.playvideota.fragment.GeetaSlokFragment;
import com.example.playvideota.fragment.PanchangFragment;
import com.example.playvideota.fragment.RashiphalFragment;
import com.example.playvideota.fragment.TvFragment;
import com.example.playvideota.model.YoutuberModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.iammert.library.readablebottombar.ReadableBottomBar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private String api = "AIzaSyBA5stcvWxiMf5PhX6HRQJJMhC2a6ovzxo";


    ActivityMainBinding binding;

    RecyclerView youtuberRV;
    ArrayList<YoutuberModel> list;
    YoutuberAdapter youtuberAdapter;
    ArrayList<String> youtubeAcountList;
    String personName = "";
    String personPhoto = "";

    private final String CHANNEL_ID = "simple_notification";
    private final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_main);
        setContentView(binding.getRoot());
        toolbarButtonFunction();


        // setting the root fragment for home page
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, new TvFragment());
        transaction.commit();


        binding.bottomNavigation.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {

                // this is used to change one fragment to another.
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (i) {
                    case 0:
                        ExecutorService service = Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    transaction.replace(R.id.fragmentContainer, new TvFragment());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
//                        transaction.replace(R.id.fragmentContainer, new ContestFragment());
                        break;
                    case 1:
                        ExecutorService service1 = Executors.newSingleThreadExecutor();
                        service1.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    transaction.replace(R.id.fragmentContainer, new PanchangFragment());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
//                        transaction.replace(R.id.fragmentContainer, new ProblemFragment());
                        break;
                    case 2:
                        ExecutorService service2 = Executors.newSingleThreadExecutor();
                        service2.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    transaction.replace(R.id.fragmentContainer, new RashiphalFragment());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
//                        transaction.replace(R.id.fragmentContainer, new ProfileFragment());
                        break;


                    case 3:
//                        ExecutorService service3 = Executors.newSingleThreadExecutor();
//                        service3.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    transaction.replace(R.id.fragmentContainer, new GeetaSlokFragment());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        });
                        transaction.replace(R.id.fragmentContainer, new GeetaSlokFragment());
                        break;
                }

                transaction.commit();
            }
        });

        ImageView logoImage = binding.logoImage;
        logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotification();
                addNotification();
            }
        });

    }







    private void createNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Vedic Astrology Dainik Rashifal";
            String desc = "Check out your today Rashiphal ";

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(desc);

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

        }

    }

    private void addNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.logov2);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logov2));
        builder.setContentTitle("Vedic Astrology Dainik Rashifal");
        builder.setContentText("Check out your today Rashiphal ");
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Add as notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

    }


    private void toolbarButtonFunction() {
        ImageView adminButton = findViewById(R.id.adminButton);
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdminDashboard.class);
                startActivity(intent);
            }
        });

        ImageView shareButton = (ImageView) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "this is sharing app");
                intent.putExtra(Intent.EXTRA_TEXT, "your application link here");
                startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });

    }

    @Override
    public void onBackPressed() {
//        moveTaskToBack(true);
        Dialog customDialog = new Dialog(this);
        customDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.getWindow().getAttributes().windowAnimations
                = androidx.appcompat.R.style.Animation_AppCompat_Dialog;
        customDialog.setContentView(R.layout.exit_dialog_layout);

        ImageButton yesBtn = customDialog.findViewById(R.id.btnYes);
        ImageButton noBtn = customDialog.findViewById(R.id.btnNo);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "this is sharing app");
                intent.putExtra(Intent.EXTRA_TEXT, "your application link here");
                startActivity(Intent.createChooser(intent, "Share Via"));
//                finishAffinity();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
//                customDialog.cancel();
            }
        });

        customDialog.show();
    }

    private void youtubeAccount() {
        youtubeAcountList = new ArrayList<>();

        youtubeAcountList.add("UCRUAdVm9ZOF4JheOd8qIQHA");
        youtubeAcountList.add("UCDe0DwkMVFfSIoiYdQUPQmQ");
        youtubeAcountList.add("UCPITDRnLbTRyyEUVwZHbMww");
        youtubeAcountList.add("UCSzOZ97LOpU-_AVlGfmD4rQ");
        youtubeAcountList.add("UCHq7ZxlzRRXimaBmk5QAxSQ");
        youtubeAcountList.add("UCUUIz69kK3Ib5bD4hWLKAwA");
        youtubeAcountList.add("UC8Igqo3g1U40n66BLb-xHuQ");
        youtubeAcountList.add("UCfwa_zKl8-zC9rQDWIEixgg");
        youtubeAcountList.add("UCfwa_zKl8-zC9rQDWIEixgg");
        youtubeAcountList.add("CT_QwW7Tbew5qrYNb2auqAQ");
        youtubeAcountList.add("UC04m8d9t8UeWZ5DuvQVnqiw");
        youtubeAcountList.add("UC6vQRTCxutg6fJLUGkDKynQ");
        youtubeAcountList.add("UCOizxR3GwY7dmehMCAdvv9g");
        youtubeAcountList.add("UCyIkg79GpPVF77qYKoAINtw");
        youtubeAcountList.add("UCDqkux3AH7P9hRjmunoUeAQ");
        youtubeAcountList.add("UC7ZivIYRB0fMSGh-THcTYbw");
        youtubeAcountList.add("UCaayLD9i5x4MmIoVZxXSv_g");
        youtubeAcountList.add("UCHKGDg0GJKBsA9mFraDOLHA");


//        youtubeAcountList.add("AasthaChannel UCRUAdVm9ZOF4JheOd8qIQHA");
//        youtubeAcountList.add("Aniruddhacharya ji UCDe0DwkMVFfSIoiYdQUPQmQ");
//        youtubeAcountList.add("Satsang UCPITDRnLbTRyyEUVwZHbMww");
//        youtubeAcountList.add("Bhakthi TV UCSzOZ97LOpU-_AVlGfmD4rQ");
//        youtubeAcountList.add("Shri Devkinandan Thakur Ji UCHq7ZxlzRRXimaBmk5QAxSQ");
//        youtubeAcountList.add("Pandit Pradeep Ji Mishra Sehore Wale UCUUIz69kK3Ib5bD4hWLKAwA");
//        youtubeAcountList.add("Bhakti Sangeet HDN UC8Igqo3g1U40n66BLb-xHuQ");
//        youtubeAcountList.add("JayaKishori Motivation UCfwa_zKl8-zC9rQDWIEixgg");
//        youtubeAcountList.add("Iamjayakishori UCfwa_zKl8-zC9rQDWIEixgg");
//        youtubeAcountList.add("SanskarTV CT_QwW7Tbew5qrYNb2auqAQ");
//        youtubeAcountList.add("SADHNA GOLD UC04m8d9t8UeWZ5DuvQVnqiw");
//        youtubeAcountList.add("Saregama Bhakti UC6vQRTCxutg6fJLUGkDKynQ");
//        youtubeAcountList.add("Gauri Gopal Tv UCOizxR3GwY7dmehMCAdvv9g");
//        youtubeAcountList.add("Swami Raghvacharya UCyIkg79GpPVF77qYKoAINtw");
//        youtubeAcountList.add("BhaktiSagar Tv UCDqkux3AH7P9hRjmunoUeAQ");
//        youtubeAcountList.add("Shemaroo Bhakti UC7ZivIYRB0fMSGh-THcTYbw");
//        youtubeAcountList.add("T-Series Bhakti Sagar UCaayLD9i5x4MmIoVZxXSv_g");
//        youtubeAcountList.add("Pen Bhakti UCHKGDg0GJKBsA9mFraDOLHA");

    }

    private void loadYoutubeAccount() {
        for (int j = 0; j < youtubeAcountList.size(); j++) {
            String youtubeAccountUrl = "https://youtube.googleapis.com/youtube/v3/channels?part=snippet,brandingSettings&id=" + youtubeAcountList.get(j) + "&key=AIzaSyBA5stcvWxiMf5PhX6HRQJJMhC2a6ovzxo";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, youtubeAccountUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            jsonObject.getString("id");
                            System.out.println("ID : " + jsonObject.getString("id"));

                            JSONObject snippetJsonObject = jsonObject.getJSONObject("snippet");
                            snippetJsonObject.getString("title");

                            System.out.println("TITLE : " + snippetJsonObject.getString("title"));

                            JSONObject thumbnailJsonObject = snippetJsonObject.getJSONObject("thumbnails");
                            JSONObject mediumJsonObject = thumbnailJsonObject.getJSONObject("high");

                            mediumJsonObject.getString("url");
                            System.out.println("URL : " + mediumJsonObject.getString("url"));

                            // getting channel branding image
                            JSONObject channelBrandingJsonObject = jsonObject.getJSONObject("brandingSettings");
                            JSONObject bannerImageJsonObject = channelBrandingJsonObject.getJSONObject("image");
                            bannerImageJsonObject.getString("bannerExternalUrl");


                            YoutuberModel youtuberModel = new YoutuberModel(
                                    mediumJsonObject.getString("url"),
                                    snippetJsonObject.getString("title"),
                                    jsonObject.getString("id"),
                                    bannerImageJsonObject.getString("bannerExternalUrl")

                            );
                            list.add(youtuberModel);
                        }
                        YoutuberAdapter youtubeAccountAdapter = new YoutuberAdapter(getApplicationContext(), list);
                        youtuberRV.setAdapter(youtubeAccountAdapter);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error

                }
            });

            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        }

        YoutuberAdapter youtubeAccountAdapter1 = new YoutuberAdapter(getApplicationContext(), list);
        youtuberRV.setAdapter(youtubeAccountAdapter1);


    }



    private void settingAdapter() {
        youtuberRV = findViewById(R.id.youtuberRV);
        youtuberRV.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(youtuberRV.VERTICAL);
        youtuberRV.setLayoutManager(gridLayoutManager);

        youtuberAdapter = new YoutuberAdapter(this, list);
        youtuberRV.setAdapter(youtuberAdapter);

    }


    private void gridListData() {
        list = new ArrayList<>();
    }
}