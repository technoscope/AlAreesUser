package com.alarees.tailoruserapp.more;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.account.MyAccountDetailFragment;
import com.alarees.tailoruserapp.account.PhoneAuth;
import com.alarees.tailoruserapp.account.SignUpFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Fragment> fragments;
    Context context;
    FragmentManager manager;
    ArrayList<Integer> moduleicons;
    DatabaseReference myRef;
    ArrayList<String> fragmentname;
    SharedPreferences mypref;

    public MyItemRecyclerViewAdapter(Context context, ArrayList<Fragment> fragments, ArrayList<Integer> moduleicons, FragmentManager manager, ArrayList<String> fragmentname) {
        this.mypref = context.getSharedPreferences("language", Context.MODE_PRIVATE);
        this.fragments = fragments;
        this.context = context;
        this.manager = manager;
        this.moduleicons = moduleicons;
        this.myRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
        this.myRef.keepSynced(true);
        this.fragmentname = fragmentname;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_more_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //String title=((TitledFragment)fragments.get(position)).getTitle();
        String title = fragmentname.get(position);
        int a = 1;
        holder.mNameofmodule.setText(title);
        holder.icon.setBackgroundResource(moduleicons.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.equals("My Account")||title.equals("الملف الشخصي")) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String userID = user.getUid();

                        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(userID)) {
                                    loadFragment(new MyAccountDetailFragment());
                                } else {
                                    PhoneAuth.authflag = 1;
                                    loadFragment(new SignUpFragment());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        loadFragment(fragments.get(position));
                    }
                } else {
                    loadFragment(fragments.get(position));
                }
            }
        });
        if (mypref.getInt("en", 0) == 1 && mypref.getInt("ar", 0) == 0) {

            holder.arrowrtl.setImageResource(R.drawable.icon_arrow_right);
        } else if (mypref.getInt("ar", 0) == 1 && mypref.getInt("en", 0) == 0) {
            holder.arrowrtl.setImageResource(R.drawable.icon_arrow_left);

        }
    }

    public int getItemCount() {
        return fragments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mView;
        public TextView mNameofmodule;
        public ImageView icon;
        public ImageView arrowrtl;

        public ViewHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.id_view);
            mNameofmodule = (TextView) view.findViewById(R.id.name_of_module);
            icon = view.findViewById(R.id.fragment_icon);
            arrowrtl = view.findViewById(R.id.arrow);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameofmodule.getText() + "'";
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack("MoreItemFragment");
        transaction.commit();
    }
}