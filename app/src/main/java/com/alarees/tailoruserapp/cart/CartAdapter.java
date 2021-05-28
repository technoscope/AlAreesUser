package com.alarees.tailoruserapp.cart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.account.SigninFragment;
import com.alarees.tailoruserapp.database.DatabaseHelper;
import com.alarees.tailoruserapp.home.HomeFragment;
import com.alarees.tailoruserapp.measurement.HowToFragment;
import com.alarees.tailoruserapp.paymentgetway.CheckoutActivityJava;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import static com.alarees.tailoruserapp.DashboardActivity.badgetext;
import static com.alarees.tailoruserapp.cart.CartFragment.prceedbtn;
import static com.alarees.tailoruserapp.cart.CartFragment.txtNoitem;
import static com.alarees.tailoruserapp.cart.CartFragment.txtTotals;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    ArrayList<CartUnstitchedModel> models;
    Context context;
    DatabaseHelper helper;
    CartUnstitchedModel model;
    DatabaseReference mRef;
    FragmentManager manager;
    View view;

    public CartAdapter(FragmentManager manager, DatabaseHelper helper, ArrayList<CartUnstitchedModel> unsmodels, Context context, View parentview) {
        this.helper = helper;
        this.models = unsmodels;
        this.context = context;
        model = new CartUnstitchedModel();
        this.manager = manager;
        mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
        this.view = parentview;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cart_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setUnstichedView(holder, position);
        unstichedtotal(models, holder);
    }
    void setUnstichedView(ViewHolder holder, int position) {
        try {
            String url = models.get(position).getClothimg();
            Uri uri = Uri.parse(url);
            holder.itemimage.setImageURI(uri);
        } catch (Exception e) {
        }
//        Glide.with(context)
//                .load(Uri.parse(models.get(position).getClothimg()))
//                .into(holder.itemimage);
        holder.itemname.setText(models.get(position).getClothName());
        holder.qty.setText(models.get(position).getQuantity());
        double quantity = Double.parseDouble(holder.qty.getText().toString());
        double price = Double.parseDouble(models.get(position).getPrice());
        double to = price * quantity;
        if (models.get(position).getCollarType() != null) {
            holder.txtmeter.setVisibility(View.VISIBLE);
            holder.inc_qty.setVisibility(View.INVISIBLE);
            holder.dec_qty.setVisibility(View.INVISIBLE);
            holder.dec_qty1.setVisibility(View.VISIBLE);
            holder.dec_qty1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    helper.deleteRData(models.get(position).getCartId(), models.get(position).getClothimg());
                    models.remove(position);
                    unstichedtotal(models, holder);
                    notifyDataSetChanged();
                }
            });
        }

        holder.itemprice.setText(String.valueOf(to));
        holder.inc_qty.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                double quantity = Double.parseDouble(holder.qty.getText().toString()) + 1;
                double price = Double.parseDouble(models.get(position).getPrice());
                double result = quantity * price;
                holder.itemprice.setText(String.valueOf(result));
                holder.qty.setText(String.valueOf(quantity));
                model = new CartUnstitchedModel();
                model.setQuantity(holder.qty.getText().toString());
                model.setPrice(models.get(position).getPrice());
                model.setCartId(models.get(position).getCartId());
                model.setOrderId(models.get(position).getOrderId());
                model.setClothimg(models.get(position).getClothimg());
                model.setCollarType(models.get(position).getCollarType());
                model.setCuffType(models.get(position).getCuffType());
                model.setPlacketType(models.get(position).getCuffType());
                model.setPocketType(models.get(position).getCuffType());
                model.setClothName(models.get(position).getClothName());
                models.set(position, model);
                unstichedtotal(models, holder);
                helper.insertUnstitchedData(model);
            }
        });
        prceedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (models.size() > 0) {
                    cProceedAction();
                }
            }
        });

        holder.dec_qty.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (Double.parseDouble(holder.qty.getText().toString()) > 1) {
                    double quantity = Double.parseDouble(holder.qty.getText().toString()) - 1;
                    double price = Double.parseDouble(models.get(position).getPrice());
                    double result = quantity * price;
                    holder.qty.setText(String.valueOf(quantity));
                    holder.itemprice.setText(String.valueOf(quantity * price));
                    model = new CartUnstitchedModel();
                    model.setQuantity(holder.qty.getText().toString());
                    model.setPrice(models.get(position).getPrice());
                    model.setCartId(models.get(position).getCartId());
                    model.setOrderId(models.get(position).getOrderId());
                    model.setClothimg(models.get(position).getClothimg());
                    model.setCollarType(models.get(position).getCollarType());
                    model.setCuffType(models.get(position).getCuffType());
                    model.setPlacketType(models.get(position).getCuffType());
                    model.setPocketType(models.get(position).getCuffType());
                    model.setClothName(models.get(position).getClothName());
                    models.set(position, model);
                    unstichedtotal(models, holder);
                    helper.insertUnstitchedData(model);
                } else if (Double.parseDouble(holder.qty.getText().toString()) == 1) {
                    try {
                        helper.deleteRData(models.get(position).getCartId(), models.get(position).getClothimg());
                        models.remove(position);
                        unstichedtotal(models, holder);
                        notifyDataSetChanged();
                    } catch (Exception e) {
                    }

                }
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.getAction() == android.view.KeyEvent.ACTION_UP) {
                    backtoparent();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemprice, qty, itemname, txtmeter;
        ImageView  inc_qty, dec_qty,dec_qty1;
        SimpleDraweeView itemimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmeter = itemView.findViewById(R.id.inmeter);
            itemname = itemView.findViewById(R.id.txt_title);
            qty = itemView.findViewById(R.id.txt_count);
            itemprice = itemView.findViewById(R.id.txt_price);
            itemimage = itemView.findViewById(R.id.p_imageView);
            inc_qty = itemView.findViewById(R.id.img_add);
            dec_qty = itemView.findViewById(R.id.img_minus);
            dec_qty1=itemView.findViewById(R.id.img_minus1);
        }
    }

    @SuppressLint("SetTextI18n")
    public void unstichedtotal(ArrayList<CartUnstitchedModel> unstitchedcartList, ViewHolder holder) {
        double total = 0;
        for (int i = 0; i < unstitchedcartList.size(); i++) {
            CartUnstitchedModel cartunsmodel = unstitchedcartList.get(i);
            double temp = Double.parseDouble(String.valueOf(Double.parseDouble(cartunsmodel.getPrice()) * Double.parseDouble(cartunsmodel.getQuantity())));
            total = total + temp;
        }
        // GetService.totalTemp = total;
        badgetext.setText(" " + unstitchedcartList.size());
        txtTotals.setText(" " + total);
        txtNoitem.setText(unstitchedcartList.size() + " items Added");
    }

    private void cProceedAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm order")
                .setMessage("Are you sure, you want to proceed an order?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo Order place to Database after payment proceed
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild("MeasurementParams")) {
                                        Intent intent = new Intent(context, CheckoutActivityJava.class);
                                        intent.putExtra("price", txtTotals.getText().toString());
                                        context.startActivity(intent);
                                    } else if (models.get(0).getPocketType() == null) {
                                        Intent intent = new Intent(context, CheckoutActivityJava.class);
                                        intent.putExtra("price", txtTotals.getText().toString());
                                        context.startActivity(intent);
                                    } else {
                                        FragmentTransaction transaction = manager.beginTransaction();
                                        transaction.replace(R.id.nav_host_fragment, new HowToFragment());
                                        transaction.addToBackStack("CartFragment");
                                        transaction.commit();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.nav_host_fragment, new SigninFragment());
                            transaction.addToBackStack("CartFragment");
                            transaction.commit();
                        }
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        //Creating dialog box
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void backtoparent() {
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.nav_host_fragment, new HomeFragment());
        ft.addToBackStack("HomeFragment");
        ft.commit();
    }
}