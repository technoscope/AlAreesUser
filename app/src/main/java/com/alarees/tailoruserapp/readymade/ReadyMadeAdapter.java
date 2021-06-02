package com.alarees.tailoruserapp.readymade;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.Transliterator;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.readymade.model.ReadymadeModel;
import com.bumptech.glide.Glide;
import com.alarees.tailoruserapp.cart.CartFragment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class ReadyMadeAdapter extends RecyclerView.Adapter<ReadyMadeAdapter.ViewHolder> {
    Context context;
    ArrayList<ReadymadeModel> models;
    FragmentManager manager;
    Dialog dialog;
    static ReadymadeModel model;
    SharedPreferences mypref;

    public ReadyMadeAdapter(FragmentManager manager, Context context, ArrayList<ReadymadeModel> models) {
        this.manager = manager;
        this.context = context;
        this.models = models;
        this.mypref = context.getSharedPreferences("language", Context.MODE_PRIVATE);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.readymade_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        model = new ReadymadeModel();
        holder.totalqty.setText(models.get(position).getTotalQty());
        holder.nameofitem.setText(models.get(position).getItemName());
        holder.itemprice.setText(models.get(position).getItemPrice());
        holder.itemimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(context, models.get(position).getImgUrl());
            }
        });
        try {
            String url = models.get(position).getImgUrl();
            Uri uri = Uri.parse(url);
            holder.itemimg.setImageURI(uri);
        } catch (Exception e) {
        }
//        Glide.with(context)
//                .load(Uri.parse(models.get(position).getImgUrl()))
//                .into(holder.itemimg);
        holder.viewclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailog(context, position);

            }
        });
        if (mypref.getInt("en", 0) == 1 && mypref.getInt("ar", 0) == 0) {

            holder.arrowrtl.setImageResource(R.drawable.icon_arrow_right);
        } else if (mypref.getInt("ar", 0) == 1 && mypref.getInt("en", 0) == 0) {
            holder.arrowrtl.setImageResource(R.drawable.icon_arrow_left);

        }else {
            holder.arrowrtl.setImageResource(R.drawable.icon_arrow_right);
        }

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView itemimg;
        TextView nameofitem, itemprice, totalqty;
        LinearLayout viewclick;
        ImageView arrowrtl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewclick = itemView.findViewById(R.id.view_click);
            itemimg = itemView.findViewById(R.id.ready_item_img);
            nameofitem = itemView.findViewById(R.id.name_of_item);
            itemprice = itemView.findViewById(R.id.price_of_item);
            totalqty = itemView.findViewById(R.id.total_qty);
            arrowrtl = itemView.findViewById(R.id.readymade_arrow);
        }
    }

    public void dialog(Context context, String uri) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.imagedialog);
        SimpleDraweeView dialogiamge = dialog.findViewById(R.id.dialogimage);
        ImageView dialogButtonCancel = dialog.findViewById(R.id.cancel);
        try {
            String url = uri;
            Uri urii = Uri.parse(url);
            dialogiamge.setImageURI(urii);
        } catch (Exception e) {
            Toast.makeText(context, "error image", Toast.LENGTH_SHORT).show();
        }
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    public void showDailog(Context context, int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dailog_readymade);

        TextView small = (TextView) dialog.findViewById(R.id.rdymd_small);
        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                forData(small.getText().toString(), position);
                  Toast.makeText(context, "Small selected", Toast.LENGTH_SHORT).show();
            }
        });
        TextView medium = (TextView) dialog.findViewById(R.id.rdymd_medium);
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forData(medium.getText().toString(), position);
                dialog.dismiss();
                Toast.makeText(context, "Medium selected", Toast.LENGTH_SHORT).show();


            }
        });
        TextView large = (TextView) dialog.findViewById(R.id.rdymd_large);
        large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forData(large.getText().toString(), position);
                dialog.dismiss();
                Toast.makeText(context, "Large selected", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void forData(String name, int position) {
        FragmentTransaction ft = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("size", name);
        bundle.putString("itemname", models.get(position).getItemName());
        bundle.putString("itemprice", models.get(position).getItemPrice());
        bundle.putString("itemdes", models.get(position).getItemDes());
        bundle.putString("itemimg", models.get(position).getImgUrl());
        bundle.putString("smallqty", models.get(position).getSmallQty());
        bundle.putString("largeqty", models.get(position).getLargeQty());
        bundle.putString("mediumqty", models.get(position).getMediumQty());
        bundle.putString("itemid", models.get(position).getItemid());
        bundle.putString("totalqty", models.get(position).getTotalQty());
        bundle.putString("fromReadymade", "ready");
        CartFragment cf = new CartFragment();
        cf.setArguments(bundle);
        ft.replace(R.id.nav_host_fragment, cf);
        ft.addToBackStack("ReadyMadeitemsFragment");
        ft.commit();
    }


}
