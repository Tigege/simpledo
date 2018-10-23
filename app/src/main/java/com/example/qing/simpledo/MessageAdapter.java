package com.example.qing.simpledo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private Context mContext;
    private List<messageClass> mDatas;
    private CheckItemListener mCheckListener;


    public MessageAdapter(Context mContext, List<messageClass> mDatas, CheckItemListener mCheckListener){
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.mCheckListener = mCheckListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.item_one,parent,false);
        ViewHolder holder =new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final messageClass mess=mDatas.get(position);

        holder.meEdit.setText(mess.getEditmessages());
        holder.messcheckBox.setChecked(mess.getCheck());

        if(mess.getCheck()){
            holder.meEdit.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            //    holder.meEdit.getPaint().setAntiAlias(true);
        }else{
            holder.meEdit.getPaint().setFlags(0);  //取消划线
        }

        //监听是否划线复选框
        holder.item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, mess.getCheck().toString(), Toast.LENGTH_SHORT).show();
                if(null!=mCheckListener){
                    mCheckListener.itemChecked(mess,!mess.getCheck());
                }
                notifyDataSetChanged();
            }
        });
        holder.messcheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, mess.getCheck().toString(), Toast.LENGTH_SHORT).show();
                if(null!=mCheckListener){
                    mCheckListener.itemChecked(mess,!mess.getCheck());
                }
                notifyDataSetChanged();
            }
        });

        //长按
        holder.item1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                Toast.makeText(mContext, "long touch", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
                dialog.setTitle("提示:");
                dialog.setMessage("    你要删除改条计划吗？");
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mCheckListener.messageDel(mess);
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CheckBox messcheckBox;
        private TextView meEdit;
        private LinearLayout item1;

        public ViewHolder(View view){
            super(view);
            messcheckBox=(CheckBox) view.findViewById(R.id.checkbox1);
            meEdit=(TextView) view.findViewById(R.id.item_name_tv);
            item1=(LinearLayout)view.findViewById(R.id.item);
        }
    }

    public interface CheckItemListener {
        void itemChecked(messageClass checkBean, boolean isChecked);
        void messageChecked(messageClass chenckBean,String inform);
        void messageAdd(String mess);
        void messageDel(messageClass chenckBean);
    }

}