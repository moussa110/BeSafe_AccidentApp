package com.example.besafe;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.util.List;

public  class EmerAdapter extends RecyclerView.Adapter<EmerAdapter.MyViewHolder> {

    private static String Name;
    private static String phone;

    Context mContext;
    List<Emergency> mData;
    Dialog myDialog;
    Dialog  EditDialog;

    public EmerAdapter(Context mContext, List<Emergency> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v;
        v= LayoutInflater.from(mContext).inflate(R.layout.emergency_layout,parent,false);
        final MyViewHolder vhoder=new MyViewHolder(v);

        myDialog =new Dialog(mContext);
        myDialog.setContentView(R.layout.dialog_emercontact);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditDialog =new Dialog(mContext);
        EditDialog.setContentView(R.layout.dialog_emeredit);
        EditDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        vhoder.layoutemer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView diaName=(TextView)myDialog.findViewById(R.id.dialogName);
                TextView diaPhone=(TextView)myDialog.findViewById(R.id.dialogPhone);

                diaName.setText(mData.get(vhoder.getAdapterPosition()).getName());
                diaPhone.setText(mData.get(vhoder.getAdapterPosition()).getPhone());
                Button edit=(Button)myDialog.findViewById(R.id.btnedit);
                Button delete=(Button)myDialog.findViewById(R.id.btndelete);


                Name=diaName.getText().toString();
                phone=diaPhone.getText().toString();

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Database db=new Database ();
                        Connection conn=db.ConnectDB ();

                        if (conn==null){
                            Toast.makeText (mContext,"تحقق من الاتصال بالانترنت", Toast.LENGTH_SHORT).show ();
                        }else {
                            String msg=db.RUNDML ("delete from EmgContacts where emPhone=N'"+phone+"'");
                            if (msg.equals ("ok")){
                                Toast.makeText(mContext, "اسحب لاسفل للتحديث ", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText (mContext, msg, Toast.LENGTH_SHORT).show ();
                            }
                        }
                        EditDialog.cancel();
                        myDialog.cancel();
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText editname=(EditText)EditDialog.findViewById(R.id.EdName);
                        final EditText editphone=(EditText)EditDialog.findViewById(R.id.EdPhone);
                        Button btnUpdate=(Button)EditDialog.findViewById(R.id.btnupdate);

                        editname.setText(Name);
                        editphone.setText(phone);

                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Database db=new Database ();
                                Connection conn=db.ConnectDB ();

                                if (conn==null){
                                    Toast.makeText (mContext,"تحقق من الاتصال بالانترنت", Toast.LENGTH_SHORT).show ();
                                }else {
                                    String msg=db.RUNDML ("update EmgContacts set emPhone=N'"+editphone.getText().toString()+"',name=N'"+editname.getText().toString()+"' where emPhone=N'"+phone+"'");
                                    if (msg.equals ("ok")){
                                        Toast.makeText(mContext, " اسحب لاسفل للتحديث ", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText (mContext, msg, Toast.LENGTH_SHORT).show ();
                                    }
                                }
                                EditDialog.cancel();
                                myDialog.cancel();
                            }
                        });

                        EditDialog.show();

                    }
                });
                myDialog.show();

            }
        });

        return vhoder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.EmerPhone.setText(mData.get(position).getPhone());
        holder.EmerName.setText(mData.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layoutemer;
        private TextView EmerName;
        private TextView EmerPhone;
        private TextView UserPhone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            EmerPhone=(TextView)itemView.findViewById(R.id.txtEmePhone);
            EmerName=(TextView)itemView.findViewById(R.id.txtEmeName);
            layoutemer=(RelativeLayout) itemView.findViewById(R.id.emerge_layout);
        }
    }
}

