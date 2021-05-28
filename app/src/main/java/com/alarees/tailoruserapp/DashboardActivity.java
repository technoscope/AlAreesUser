package com.alarees.tailoruserapp;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alarees.tailoruserapp.account.SignUpFragment;
import com.alarees.tailoruserapp.account.SigninFragment;
import com.alarees.tailoruserapp.cart.CartFragment;
import com.alarees.tailoruserapp.home.HomeFragment;
import com.alarees.tailoruserapp.measurement.ProgressFragment;
import com.alarees.tailoruserapp.more.MoreItemFragment;
import com.alarees.tailoruserapp.more.orders.OrderStatus;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class DashboardActivity extends AppCompatActivity implements IPickResult {
    private Map<Integer, Fragment> fragments;
    public static TextView badgetext;
    @SuppressLint("StaticFieldLeak")
    public static ImageView cartimg;
    DatabaseReference myRef;
    OrderStatus orderStatus;
    static String CHANNEL_ID = "1";
    int i = 1;
    Context context;
    Resources resources;
        //new change333
    SharedPreferences sharedpreferences;
    Editor editor;
    Spinner spinner;
    ArrayAdapter<String> spinneradapter;
    ArrayList<String> list;
    SharedPreferences mypref;
    //ne chnages
    Editor shareeditor;

    public DashboardActivity() {
        this.fragments = new HashMap<Integer, Fragment>() {{
            put(R.id.nav_home, new HomeFragment());
            put(R.id.nav_cart, new CartFragment());
            put(R.id.nav_more, new MoreItemFragment());
        }};
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();
        createNotificationChannel();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        Fresco.initialize(this);
        mypref = getSharedPreferences("language", Context.MODE_PRIVATE);
        list = new ArrayList<>();
        if(mypref.getInt("en",0)==0&&mypref.getInt("ar",0)==0) {
            list = new ArrayList<>();
            list.add("en");
            list.add("ar");
        }else if(mypref.getInt("en",0)==1&&mypref.getInt("ar",0)==0){
            list = new ArrayList<>();
            list.add("en");
            list.add("ar");
            setApplicationLocale("en");
        }else if(mypref.getInt("ar",0)==1&&mypref.getInt("en",0)==0){
            list = new ArrayList<>();
            list.add("ar");
            list.add("en");
            setApplicationLocale("ar");

        }

        String flag = getIntent().getStringExtra("flag");
//        context = LocaleHelper.setLocale(DashboardActivity.this, "ar");
//        resources = context.getResources();
        sharedpreferences = getSharedPreferences("Notification", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.nav_home);
        showBadge(this, navigation, R.id.nav_cart, "0");
        moreview(this, navigation, R.id.nav_more);
        try {
            if (flag.equals("1")) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new ProgressFragment());
                ft.addToBackStack("SideFragment");
                ft.commit();
            } else if (flag.equals("2")) {
                Bundle bundle=new Bundle();
                bundle.putInt("phoneauth",10);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SignUpFragment signUpFragment=new SignUpFragment();
                signUpFragment.setArguments(bundle);
                ft.replace(R.id.nav_host_fragment, signUpFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        } catch (Exception e) {
        }
        i = 1;
        try {
            myRef = FirebaseDatabase.getInstance().getReference("AdminDatabase").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            if (myRef != null) {
                myRef.child("Notification").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            myRef = FirebaseDatabase.getInstance().getReference("AdminDatabase").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            myRef.child("Notification").child(data.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    orderStatus = new OrderStatus();
                                    orderStatus = snapshot.getValue(OrderStatus.class);
                                    if (orderStatus != null) {
                                        if (orderStatus.getReceived().equals("0") && orderStatus.getCompleted().equals("0") && orderStatus.getProgress().equals("your order is Progress")) {
                                            //   holder.status.setText("in progress");

                                            editor.putInt("progress", i + sharedpreferences.getInt("progress", 1));
                                            editor.apply();
                                            editor.commit();
                                            if (sharedpreferences.getInt("progress", 2) == 2) {
                                                notification("your order is in progress");
                                            }
                                        } else if (orderStatus.getReceived().equals("your order is received successfully") && orderStatus.getCompleted().equals("0") && orderStatus.getProgress().equals("0")) {
                                            // holder.status.setText("received");
                                            editor.putInt("received", i + sharedpreferences.getInt("received", 1));
                                            editor.apply();
                                            editor.commit();
                                            if (sharedpreferences.getInt("received", 2) == 2) {
                                                notification("your order is received successfully we will notify after completion");
                                            }

                                        } else if (orderStatus.getReceived().equals("0") && orderStatus.getCompleted().equals("your order is Completed we will contact you soon") && orderStatus.getProgress().equals("0")) {
                                            // holder.status.setText("completed");
                                            editor.putInt("completed", i + sharedpreferences.getInt("completed", 3));
                                            editor.apply();
                                            editor.commit();
                                            if (sharedpreferences.getInt("completed", 2) == 2) {
                                                notification("your order is Completed please visit our shop to get your order");
                                            }
                                        }
                                    } else {
                                        // holder.status.setText("pending");

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        } catch (Exception e) {
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        try {
            loadFragment(fragments.get(item.getItemId()));
//            setTitle(((TitledFragment) fragments.get(item.getItemId())).getTitle());
            return true;
        } catch (Exception e) {
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.addToBackStack("HomeFragment");
        ft.commit();
    }

    private String getCallerFragment() {
        FragmentManager fm = getFragmentManager();
        int count = getFragmentManager().getBackStackEntryCount();
        return fm.getBackStackEntryAt(count - 1).getName();
    }

    @SuppressLint("ResourceAsColor")
    public static void showBadge(Context context, BottomNavigationView bottomNavigationView, @IdRes int itemId, String value) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.custom_action_cart_view, bottomNavigationView, false);
        badgetext = badge.findViewById(R.id.txt_Count);
        badgetext.setText(value);
        cartimg = badge.findViewById(R.id.img_cart);

//      cartimg.setBackgroundResource(R.drawable.ic_baseline_shopping_cart_green);
//      cartimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                }
//            }
//        });
        itemView.addView(badge);
    }

    public static void moreview(Context context, BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.custom_more_icon_view, bottomNavigationView, false);

        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 4) {
            itemView.removeViewAt(4);
        }
    }

    @Override
    public void onPickResult(PickResult r) {
        //   String a=getCallerFragment();
//        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getApplicationContext().getString(R.string.channel_name);
            String description = getApplicationContext().getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void notification(String msg) {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);//context.getPackageManager().getLaunchIntentForPackage("com.example.boncafe");
        PendingIntent myPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_home)
                .setContentTitle("Al Arees")
                .setContentText(msg)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(myPendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }

    private void setApplicationLocale(String locale) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(locale.toLowerCase()));
        } else {
            config.locale = new Locale(locale.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);

        SigninFragment fragment = new SigninFragment();
        fragment.onActivityResult(requestCode, resultCode, imageData);
        //}
    }
}