package com.alarees.tailoruserapp.more.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alarees.tailoruserapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    ArrayList<OrderModel> orderModels;
    DatabaseReference myRef;
    OrderStatus orderStatus;

    public OrdersAdapter(ArrayList<OrderModel> orderModels) {
        this.orderModels = orderModels;
        orderStatus = new OrderStatus();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_view, parent, false);
        return new OrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.orderdate.setText(orderModels.get(position).getOrderdate());
        holder.orderid.setText(orderModels.get(position).getOrderId());
        if (orderModels.get(position).getCuffType() == null) {
            holder.ordertype.setText("Readymade");
        } else {
            holder.ordertype.setText("Unstiched");
        }
        myRef = FirebaseDatabase.getInstance().getReference("AdminDatabase").child("Users").child(orderModels.get(position).getUserId());
        myRef.child("Notification").child(orderModels.get(position).getOrderId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderStatus = new OrderStatus();
                orderStatus = snapshot.getValue(OrderStatus.class);
                if(orderStatus!=null) {
                    if (orderStatus.getReceived().equals("0") && orderStatus.getCompleted().equals("0") && orderStatus.getProgress().equals("your order is Progress")) {
                        holder.status.setText("in progress");
                    } else if (orderStatus.getReceived().equals("your order is received successfully") && orderStatus.getCompleted().equals("0") && orderStatus.getProgress().equals("0")) {
                        holder.status.setText("received");

                    } else if (orderStatus.getReceived().equals("0") && orderStatus.getCompleted().equals("your order is Completed we will contact you soon") && orderStatus.getProgress().equals("0")) {
                        holder.status.setText("completed");

                    }
                }else {
                    holder.status.setText("pending");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return orderModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderid, ordertype, orderdate, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderid = itemView.findViewById(R.id.id_orderid);
            ordertype = itemView.findViewById(R.id.order_ordertype);
            orderdate = itemView.findViewById(R.id.order_date);
            status = itemView.findViewById(R.id.id_status);
        }
    }
}
