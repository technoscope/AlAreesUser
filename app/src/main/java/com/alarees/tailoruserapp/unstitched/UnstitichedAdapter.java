package com.alarees.tailoruserapp.unstitched;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.unstitched.collar.CollarFragment;
import com.alarees.tailoruserapp.unstitched.model.UnstitchedModel;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

class UnstitichedAdapter extends RecyclerView.Adapter<UnstitichedAdapter.ViewHolder> {
    Context context;
    ArrayList<UnstitchedModel> models;
    FragmentManager manager;
    static UnstitchedModel model;
    Dialog dialog;
    SharedPreferences mypref;

    public UnstitichedAdapter(FragmentManager manager, Context context, ArrayList<UnstitchedModel> models) {
        this.manager = manager;
        this.context = context;
        this.models = models;
        this.mypref = context.getSharedPreferences("language", Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.unstitched_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        model = new UnstitchedModel();
        holder.totalqty.setText(models.get(position).getQuantity());
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
                Bundle bundle = new Bundle();
//                holder.productview.setBackgroundResource(R.drawable.hallow_round_black_bg);
                bundle.putString("itemname", models.get(position).getItemName());
                bundle.putString("itemprice", models.get(position).getItemPrice());
                bundle.putString("itemdes", models.get(position).getItemDes());
                bundle.putString("itemimg", models.get(position).getImgUrl());
                bundle.putString("itemid", models.get(position).getItemid());
                bundle.putString("qty", models.get(position).getQuantity());
                FragmentTransaction ft = manager.beginTransaction();
                CollarFragment pf = new CollarFragment();
                pf.setArguments(bundle);
                ft.replace(R.id.nav_host_fragment, pf);
                ft.addToBackStack("UnstitichedItemsFragment");
                ft.commit();
            }
        });
        if (mypref.getInt("en", 0) == 1 && mypref.getInt("ar", 0) == 0) {

            holder.arrowrtl.setImageResource(R.drawable.icon_arrow_right);
        } else if (mypref.getInt("ar", 0) == 1 && mypref.getInt("en", 0) == 0) {
            holder.arrowrtl.setImageResource(R.drawable.icon_arrow_left);
        } else {
            holder.arrowrtl.setImageResource(R.drawable.icon_arrow_right);
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView itemimg;
        TextView nameofitem, itemprice, totalqty;
        LinearLayout viewclick;
        ImageView arrowrtl;
        //  LinearLayout productview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewclick = itemView.findViewById(R.id.uns_view_click);
            itemimg = itemView.findViewById(R.id.unstitched_item_img);
            nameofitem = itemView.findViewById(R.id.uns_name_of_item);
            itemprice = itemView.findViewById(R.id.uns_price_of_item);
            totalqty = itemView.findViewById(R.id.uns_total_qty);
            arrowrtl = itemView.findViewById(R.id.unstitche_arrow);
            //productview = itemView.findViewById(R.id.product_view);
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
        }
        Glide.with(context)
                .load(Uri.parse(uri))
                .into(dialogiamge);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
