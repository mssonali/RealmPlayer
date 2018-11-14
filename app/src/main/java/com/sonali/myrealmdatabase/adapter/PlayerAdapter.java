package com.sonali.myrealmdatabase.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sonali.myrealmdatabase.R;
import com.sonali.myrealmdatabase.model.Player;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.MyViewHolder> {

    List<Player> alPlayers;
    Context context;
    Realm realm;


    public PlayerAdapter(Context context, List<Player> alPlayers, Realm realm) {
        this.context = context;
        this.alPlayers = alPlayers;
        this.realm = realm;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_player_item, null);
        PlayerAdapter.MyViewHolder viewHolder = new PlayerAdapter.MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Player p = alPlayers.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(p.getpImag(), 0, p.getpImag().length);

        holder.tvName.setText(p.getpName());
        holder.tvYear.setText(p.getpYear());
        holder.profile_image.setImageBitmap(bitmap);
        holder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showMyDialog("Delete", "Are you sure want to delete?", position);
            }
        });
    }

    private void showMyDialog(String title, String msg, final int position) {
        new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.dialog)
                .setButtonsColorRes(R.color.black)
                .setIcon(R.drawable.info)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            realm.beginTransaction();
                            RealmResults<Player> rows = realm.where(Player.class).equalTo("id", alPlayers.get(position).getId()).findAll();
                            rows.deleteAllFromRealm();
                            realm.commitTransaction();
                            realm.close();
                            showMyInformativeDialog("Delete","Deleted",position);

                        }catch (Exception e){
                            Log.i("realm_error",e.toString());
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void showMyInformativeDialog(String title, String msg, final int position) {
        new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.dialog)
                .setButtonsColorRes(R.color.black)
                .setIcon(R.drawable.info)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (int i = 0; i < alPlayers.size(); i++) {
                            if (alPlayers.get(i).getId().equalsIgnoreCase(alPlayers.get(position).getId())) {
                                alPlayers.remove(i);
                                // ((MyScanListActivity) context).deleteOrganizationScanById(orgScanId);
                                notifyDataSetChanged();

                                break;

                            }
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public int getItemCount() {
        return alPlayers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvScore, tvYear;
        LinearLayout llDelete;
        CircleImageView profile_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            //tvScore = itemView.findViewById(R.id.tvScore);
            tvYear = itemView.findViewById(R.id.tvYear);
            llDelete = itemView.findViewById(R.id.llDelete);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }
}
