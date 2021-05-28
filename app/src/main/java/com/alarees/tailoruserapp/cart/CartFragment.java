package com.alarees.tailoruserapp.cart;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.account.SigninFragment;
import com.alarees.tailoruserapp.database.DatabaseHelper;
import com.alarees.tailoruserapp.unstitched.collar.CollarFragment;
import com.alarees.tailoruserapp.unstitched.cuff.CuffFragment;
import com.alarees.tailoruserapp.unstitched.placket.PlacketFragment;
import com.alarees.tailoruserapp.unstitched.pocket.PocketFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import static com.alarees.tailoruserapp.DashboardActivity.badgetext;

public class CartFragment extends Fragment {
    @BindView(R.id.cart_list_id)
    RecyclerView carts;
    ArrayList<CartUnstitchedModel> unstitchedcartList;
    DatabaseHelper databaseHelper;
    @BindView(R.id.lvl_data)
    LinearLayout lvlData;
    @BindView(R.id.lvl_nodata)
    LinearLayout lvlNodata;
    public static TextView prceedbtn;
    public static TextView txtTotals;
    public static TextView txtNoitem;
    Unbinder unbinder;
    String unst;
    String st;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        txtNoitem = view.findViewById(R.id.txt_noitem);
        txtTotals = view.findViewById(R.id.totleAmount);
        prceedbtn = view.findViewById(R.id.btn_procedchackout);
        unstitchedcartList=new ArrayList<>();
        fragmentManager=getFragmentManager();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {

            databaseHelper = new DatabaseHelper(this.getContext());
            carts.setHasFixedSize(true);
            carts.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            carts = view.findViewById(R.id.cart_list_id);
            try {
                if (getArguments().getString("fromUnstitched").equals("un")) {
                    CartUnstitchedModel cartmodel = new CartUnstitchedModel();
                    cartmodel.setOrderId("1");
                    cartmodel.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    //meter formaula
                    Double height=Double.parseDouble(getArguments().getString("height"));
                    if(height>=0&&height<76.2){
                        cartmodel.setQuantity("3");
                    }else if(height>=76.2&&height<127){
                        cartmodel.setQuantity("4");

                    }else if(height>=127&&height<168){
                        cartmodel.setQuantity("5.5");

                    }else if(height>168){
                        cartmodel.setQuantity("5.5");
                    }
                    cartmodel.setMeasurementId(getArguments().getString("nodekey"));
                    cartmodel.setCollarType(CollarFragment.collarName);
                    cartmodel.setPlacketType(PlacketFragment.placketName);
                    cartmodel.setCuffType(CuffFragment.cuffName);
                    cartmodel.setPocketType(PocketFragment.pocketName);
                    cartmodel.setPrice(CollarFragment.itemprice);
                    cartmodel.setClothName(CollarFragment.itemname);
                    cartmodel.setClothimg(CollarFragment.itemimgg);
                    databaseHelper.insertData(cartmodel);
                }
            } catch (Exception e) {
            }
            try {
                if (getArguments().getString("fromReadymade").equals("ready")) {
                    CartUnstitchedModel readymodel = new CartUnstitchedModel();
                    readymodel.setOrderId("1");
                    readymodel.setClothName(getArguments().getString("itemname"));
                    readymodel.setClothimg(getArguments().getString("itemimg"));
                    readymodel.setPrice(getArguments().getString("itemprice"));
                    readymodel.setQuantity("1");
                    readymodel.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    readymodel.setSize(getArguments().getString("size"));
                    databaseHelper.insertData(readymodel);
                }
            } catch (Exception e) {
            }
            unstitchedcartList = new ArrayList<>();
            Unstitchedcartget();
            // Readymadecartget();
            carts.setAdapter(new CartAdapter(fragmentManager, databaseHelper, unstitchedcartList, getContext(), view));
        }else{
            Toast.makeText(getContext(), getResources().getString(R.string.toast_msg_create_account), Toast.LENGTH_SHORT).show();
            FragmentManager manager=getFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.nav_host_fragment, new SigninFragment());
            ft.addToBackStack("HomeFragment");
            ft.commit();
        }
    }

    public void Unstitchedcartget() {
        Cursor res = databaseHelper.getUnstitchedData();
        if (res != null) {
            if (res.getCount() != 0) {
                lvlData.setVisibility(View.VISIBLE);
                lvlNodata.setVisibility(View.GONE);
                while (res.moveToNext()) {
                    CartUnstitchedModel rModel = new CartUnstitchedModel();
                    rModel.setCartId(res.getString(0));
                    rModel.setUserId(res.getString(1));
                    rModel.setOrderId(res.getString(2));
                    rModel.setClothName(res.getString(3));
                    rModel.setPrice(res.getString(4));
                    rModel.setCuffType(res.getString(5));
                    rModel.setPlacketType(res.getString(6));
                    rModel.setPocketType(res.getString(7));
                    rModel.setCollarType(res.getString(8));
                    rModel.setQuantity(res.getString(9));
                    rModel.setClothimg(res.getString(10));
                    rModel.setMeasurementId(res.getString(11));
                    unstitchedcartList.add(rModel);
                    unstichedtotal(unstitchedcartList);
                }
                Log.e("size", " : " + unstitchedcartList.size());
                //carts.setAdapter(new CartAdapter(databaseHelper, unstitchedcartList, getContext()));
            } else if (res.getCount() == 0) {

                lvlData.setVisibility(View.GONE);
                lvlNodata.setVisibility(View.VISIBLE);
            }
        }
    }

    public void Readymadecartget() {
        Cursor res = databaseHelper.getUnstitchedData();
        if (res != null) {
            if (res.getCount() != 0) {
                lvlData.setVisibility(View.VISIBLE);
                lvlNodata.setVisibility(View.GONE);
                while (res.moveToNext()) {
                    CartUnstitchedModel rModel = new CartUnstitchedModel();
                    rModel.setCartId(res.getString(0));
                    rModel.setUserId(res.getString(1));
                    rModel.setOrderId(res.getString(2));
                    rModel.setClothName(res.getString(3));
                    rModel.setPrice(res.getString(4));
                    rModel.setQuantity(res.getString(9));
                    rModel.setClothimg(res.getString(10));
                    unstitchedcartList.add(rModel);
                    unstichedtotal(unstitchedcartList);

                }
                //Log.e("size", " : " + readymadecartList.size());
                //  carts.setAdapter(new CartAdapter(databaseHelper, readymadecartList, getContext(), 1));
            } else if (res != null) {
                lvlData.setVisibility(View.GONE);
                lvlNodata.setVisibility(View.VISIBLE);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void unstichedtotal(ArrayList<CartUnstitchedModel> unstitchedcartList) {
        double total = 0;
        for (int i = 0; i < unstitchedcartList.size(); i++) {
            CartUnstitchedModel productlist = unstitchedcartList.get(i);
            double temp = Double.parseDouble(String.valueOf(Double.parseDouble(productlist.getPrice()) * Double.parseDouble(productlist.getQuantity())));
            total = total + temp;
        }
        // GetService.totalTemp = total;

        txtTotals.setText(" " + total);
        badgetext.setText(" " + unstitchedcartList.size());
        txtNoitem.setText(unstitchedcartList.size() + " items Added");
    }
    public String gettotal(ArrayList<CartUnstitchedModel> unstitchedcartList) {
        double total = 0;
        for (int i = 0; i < unstitchedcartList.size(); i++) {
            CartUnstitchedModel productlist = unstitchedcartList.get(i);
            double temp = Double.parseDouble(String.valueOf(Double.parseDouble(productlist.getPrice()) * Double.parseDouble(productlist.getQuantity())));
            total = total + temp;
        }
        // GetService.totalTemp = total;

        return  String.valueOf(total);
    }
//    private void cProceedAction(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Confirm order")
//                .setMessage("Are you sure, you want to proceed an order?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(getContext(), PaymentGetways.class);
//                        intent.putExtra("price", gettotal(unstitchedcartList));
//                        getActivity().startActivity(intent);
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//        //Creating dialog box
//        AlertDialog dialog  = builder.create();
//        dialog.show();
//    }
}