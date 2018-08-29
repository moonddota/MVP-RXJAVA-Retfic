package app.com.skylinservice.manager.customeview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import app.com.skylinservice.R;
import app.com.skylinservice.manager.UserIdManager;
import app.com.skylinservice.sqlite.DB;
import app.com.skylinservice.sqlite.TeamTable;
import app.com.skylinservice.ui.maclist.MacLIstActivity;
import app.com.skylinservice.ui.main.MainActivity;

/**
 * Created by liuxuan on 2017/12/11.
 */

public class ReFactCardAdapter extends RecyclerView.Adapter<ReFactCardAdapter.ViewHolder> {

    private List<TeamTable> mList = new ArrayList<>();
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();
    private ImageLoader imageLoader;
    private int choosedItem;
    private Activity context;

    private String btid = "";
    private String btname = "";

    public ReFactCardAdapter(List mList, Activity context, ImageLoader imageLoader) {
        this.mList = mList;
        this.context = context;
        this.imageLoader = imageLoader;
    }


    @Override
    public ReFactCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_item, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);


        return new ViewHolder(itemView);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ReFactCardAdapter.ViewHolder holder, final int position) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.icon_default)
                .showImageForEmptyUri(R.mipmap.icon_default)     //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.mipmap.icon_default)                //加载图片出现问题，会显示该图片
                .cacheInMemory()                                               //缓存用
                .cacheOnDisc()                                                    //缓存用
                .displayer(new RoundedBitmapDisplayer(5))       //图片圆角显示，值为整数
                .build();


        if (mList.get(position).getId() == 0) {
            holder.mImageView.setImageResource(R.mipmap.icon_banner_bg);
            holder.mImageView.setVisibility(View.VISIBLE);
            holder.iv_chooesed.setVisibility(View.INVISIBLE);
            holder.setDefaultTV.setVisibility(View.INVISIBLE);
            holder.teamNameTV.setVisibility(View.INVISIBLE);
            holder.joinedDaysTV.setVisibility(View.INVISIBLE);
            holder.jointext.setVisibility(View.INVISIBLE);

        } else {
            holder.mImageView.setImageResource(R.mipmap.icon_mask);
            holder.iv_chooesed.setVisibility(mList.get(position).is_default() == 1 ? View.VISIBLE : View.INVISIBLE);
            holder.setDefaultTV.setVisibility(mList.get(position).is_default() == 1 ? View.INVISIBLE : View.VISIBLE);

            if (mList.get(position).is_default() == 1) {
                Intent intent = new Intent();
                intent.setAction("app.com.skylinservice.broadcast.changedefaultteam");
                intent.putExtra("defaut_team_id", mList.get(position).getId() + "");
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                context.sendBroadcast(intent);
            }

//            if (mList.get(position).is_default() == 1)
//            {
//                DB d = new DB(context, "teamstate.db");
//
//                SQLiteDatabase db = d.getWritableDatabase();
//
//                String[] args = {String.valueOf(new UserIdManager().getUserId())};
//
//                Cursor cursor = db.query("labelnet", new String[]{"tid"}, "_id=?", args, null, null, null);
//                int teamchanged = 0;
//                if (cursor != null && cursor.moveToFirst() && SPUtils.getInstance().getLong("choosedteamid") > 0) {
//                    if (cursor.getInt(0) != mList.get(position).getId()) {
//                        teamchanged = 1;
//                        ContentValues values = new ContentValues();
//                        values.put("teamchanged", teamchanged);
//                        values.put("tid", mList.get(position).getId());
//                        values.put("com.skylin.uav.v02.dev", teamchanged);
//                        values.put("com.skylin.uav.v02", teamchanged);
//                        values.put("com.skylin.uav.zbzy.xinshiqi", teamchanged);
//                        values.put("com.skylin.uav.zbzy", teamchanged);
//                        values.put("com.skylin.uav.basestation", teamchanged);
//                        values.put("uav.skylin.com.upgradeforfirmware", teamchanged);
//                        values.put("com.skylin.uav.rtkjizhan", teamchanged);
//                        values.put("com.skylin.uav.nongtiancehui", teamchanged);
//                        db.update("labelnet", values, "_id=?", args);
//                        db.close();
//                    }
//                } else {
//                    ContentValues values = new ContentValues();
//                    values.put("_id", new UserIdManager().getUserId());
//                    values.put("teamchanged", teamchanged);
//                    values.put("tid", mList.get(position).getId());
//                    values.put("com.skylin.uav.v02.dev", teamchanged);
//                    values.put("com.skylin.uav.v02", teamchanged);
//                    values.put("com.skylin.uav.zbzy.xinshiqi", teamchanged);
//                    values.put("com.skylin.uav.zbzy", teamchanged);
//                    values.put("com.skylin.uav.basestation", teamchanged);
//                    values.put("uav.skylin.com.upgradeforfirmware", teamchanged);
//                    values.put("com.skylin.uav.rtkjizhan", teamchanged);
//                    values.put("com.skylin.uav.nongtiancehui", teamchanged);
//                    db.insert("labelnet", null, values);
//                    values.clear();
//                    db.close();
//                }
//
//            }
            holder.teamNameTV.setText(mList.get(position).getName());

            long a = System.currentTimeMillis();
            long b = TimeUtils.string2Millis(mList.get(position).getCreateTime());
            long c = a - b;
            double d = (double) c / (double) (3600 * 1000 * 24);
            int e = (int) Math.ceil(d);
            //

            holder.joinedDaysTV.setText(e+"");
            holder.setDefaultTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getString(R.string.TeamManagerActivity_tv49))
                            .setMessage(context.getString(R.string.TeamManagerActivity_tv50));
                    builder.setPositiveButton(context.getString(R.string.activity_teammanager_btn_createteam_btn_name), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((MainActivity) context).setDefaultId(mList.get(position).getId() + "");
                        }
                    });
                    builder.setNegativeButton(context.getString(R.string.cancle), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setCancelable(false);
                    builder.show();

                }
            });

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, MacLIstActivity.class);
                    intent.putExtra("team_id", mList.get(position).getId() + "");
                    context.startActivity(intent);
                }
            });

        }
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());


    }

//    public void setChoosed() {
//        if (!btid.equals("") && !btname.equals("")) {
//            SPUtils.getInstance().put("choosedteamid", Long.valueOf(btid));
//            SPUtils.getInstance().put("choosedteamname", btname);
//            notifyDataSetChanged();
//            btid = "";
//            btname = "";
//
//        }
//    }


    private void startWebView(int postion) {
//        if (!mList.get(0).getContent().get(postion).getUrl().isEmpty()) {
//            Intent intent = new Intent(context, WebActivity.class);
//            intent.putExtra(WebActivity.urlKey, mList.get(0).getContent().get(postion).getUrl());
//            intent.putExtra("title", mList.get(0).getContent().get(postion).getTitle());
//            context.startActivity(intent);
//        } else {
//        }

    }

    public void setChoosedItem(int postion) {
        this.choosedItem = postion;
    }

    public int getChoosedItem() {
        return this.choosedItem;
    }

//    @Override
//    public int getItemCount() {
//        return mList.get(0).getContent().size();
//    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView, iv_chooesed;
        RelativeLayout cardView;

        TextView setDefaultTV, joinedDaysTV, teamNameTV, jointext;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.card_iv);

            cardView = itemView.findViewById(R.id.cardView);


            setDefaultTV = itemView.findViewById(R.id.tv_setdefault);
            teamNameTV = itemView.findViewById(R.id.tv_teamname);
            joinedDaysTV = itemView.findViewById(R.id.tv_joindays);
            jointext = itemView.findViewById(R.id.jointext);
            iv_chooesed = itemView.findViewById(R.id.iv_chooesed);


        }

    }


}
