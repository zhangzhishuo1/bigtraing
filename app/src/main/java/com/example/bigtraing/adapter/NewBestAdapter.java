package com.example.bigtraing.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bigtraing.R;
import com.example.frame.bean.NewbestBean;

import java.util.List;

public class NewBestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<NewbestBean.ResultBean> list;
    private String n = "";
    private String y = "";
    private String r = "";

    public NewBestAdapter(Context context, List<NewbestBean.ResultBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.newbestbean_item, null);
        return new ViewHolder1(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewbestBean.ResultBean bean = list.get(position);
        ViewHolder1 viewHolder1 = (ViewHolder1) holder;
        viewHolder1.tv_title.setText(bean.getTitle());
        Glide.with(context).load(bean.getPic()).into(viewHolder1.img_pic);
        viewHolder1.tv_view_num.setText(bean.getView_num() + "人浏览");
        viewHolder1.tv_reply_num.setText(bean.getReply_num() + "人跟贴");
        String gid = bean.getGid();
        if (gid.length() == 8) {
            char[] chars = gid.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (i < 4) {
                    n += chars[i];
                } else if (i >= 4 && i < 6) {
                    y += chars[i];
                } else {
                    r += chars[i];
                }
            }
        } else if (gid.length() == 7) {
            char[] c = gid.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (i < 4) {
                    n += c[i];
                } else if (c[4] == 1 && c[5] < 3) {
                    y += (c[4] + c[5]);
                } else if (i >= 4 && i < 5) {
                    y += c[4];
                } else {
                    r += c[i];
                }
            }
        } else if (gid.length() == 6) {
            char[] chars = gid.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (i < 4) {
                    n += chars[i];
                } else if (i >= 4 && i < 5) {
                    y += chars[i];
                } else {
                    r += chars[i];
                }
            }
        }
        viewHolder1.tv_gid.setText(n + "-" + y + "-" + r);
        n = "";
        y = "";
        r = "";
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static
    class ViewHolder1 extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView tv_title;
        public TextView tv_view_num;
        public TextView tv_reply_num;
        public ImageView img_pic;
        public TextView tv_gid;

        public ViewHolder1(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            this.tv_view_num = (TextView) rootView.findViewById(R.id.tv_view_num);
            this.tv_reply_num = (TextView) rootView.findViewById(R.id.tv_reply_num);
            this.img_pic = (ImageView) rootView.findViewById(R.id.img_pic);
            this.tv_gid = (TextView) rootView.findViewById(R.id.tv_gid);
        }

    }
}
