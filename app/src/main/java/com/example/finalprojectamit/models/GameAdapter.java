package com.example.finalprojectamit.models;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectamit.R;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MiniGameHolder> implements RecyclerView.OnItemTouchListener {

    public ArrayList<Game> mList;
    private final Context context;
    private final Activity activity;


    public GameAdapter(Activity activity, Context context, ArrayList<Game> mList) {
        this.activity = activity;
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MiniGameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mini_game_tile, parent, false);
        return new MiniGameHolder(v);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SuspiciousIndentation"})
    @Override
    public void onBindViewHolder(@NonNull MiniGameHolder holder, @SuppressLint("RecyclerView") int position) { //changes in real time - onbind
        holder.tvDate.setText(String.valueOf(mList.get(position).getDate()));
        holder.tvCity.setText(String.valueOf(mList.get(position).getCity()));
        holder.lBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGameDialog(mList.get(position));
            }
        });
    }

    public void showGameDialog(Game game) { //showing the game values
        Dialog d = new Dialog(context); // creating a new dialog
        d.setContentView(R.layout.game_dialog); // connecting the dialog to a xml file
        d.setCancelable(true); // the user can exit the dialog

        d.setTitle("" + game.getDate());
        TextView threeP = d.findViewById(R.id.tvThreePoints);
        TextView twoP = d.findViewById(R.id.tvTwoPoints);
        TextView fouls = d.findViewById(R.id.tvFouls);
        TextView blocks = d.findViewById(R.id.tvBlocks);
        TextView assists = d.findViewById(R.id.tvAssists);
        TextView ballsLoose = d.findViewById(R.id.tvBallLooses);
        TextView date = d.findViewById(R.id.tvDate);
        TextView city = d.findViewById(R.id.tvCity);

        threeP.setText(String.valueOf((int) game.getThreePoints()));
        twoP.setText(String.valueOf((int) game.getTwoPoints()));
        fouls.setText(String.valueOf((int) game.getFoul()));
        blocks.setText(String.valueOf((int) game.getBlocks()));
        assists.setText(String.valueOf((int) game.getAssists()));
        ballsLoose.setText(String.valueOf((int) game.getBallsLoose()));
        date.setText(game.getDate());
        city.setText(game.getCity());

        d.show();
    }

    @Override
    public int getItemCount() { //getting the amount of games
        return mList.size();
    }

    public static class MiniGameHolder extends RecyclerView.ViewHolder {

        LinearLayout lBackground;
        TextView tvDate, tvCity;

        public MiniGameHolder(@NonNull View itemView) {
            super(itemView);
            lBackground = itemView.findViewById(R.id.lBackground);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCity = itemView.findViewById(R.id.tvCity);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

}
