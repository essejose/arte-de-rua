package com.essejose.artederua.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.essejose.artederua.R;
import com.essejose.artederua.model.Event;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by logonrm on 02/09/2017.
 */

public class EventAdpter extends RecyclerView.Adapter<EventAdpter.EventViewHolde> {

    private List<Event> events;
    private OnItemClickListner listener;
    private Context context;

    public EventAdpter(Context context,List<Event> event,OnItemClickListner listener ){
        this.events = event;
        this.listener = listener;
        this.context = context;
    }

    public EventAdpter() {

    }


    @Override
    public EventViewHolde onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater =  LayoutInflater.from(parent.getContext());

        View meuLayout = inflater.inflate(R.layout.linha_row,
                parent,false);


        return new EventViewHolde(meuLayout);


    }


    @Override
    public void onBindViewHolder(EventViewHolde holder, final int position) {


        Log.d("Vindo do banco ",events.get(position).getImage());


        File file = new File(events.get(position).getImage());
        Log.d("Vindo do banco file ", String.valueOf(file));



        Picasso.with(context).load("file://"+ events.get(position).getImage())
                .config(Bitmap.Config.RGB_565)
                .fit()
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_camera)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.ivLogoEvent);

        holder.tvTituloEvent.setText(events.get(position).getTitle());
        holder.tvDescriptionEvent.setText(events.get(position).getDescripion());

        holder.itemView.setOnClickListener(new View.OnClickListener(){

             public void onClick(View v){
                listener.OnItemClick(events.get(position));

            }


        });

       /* Picasso.with(holder.itemView.getContext())
                .load(APIUtils.BASE_URL + linhas.get(position).getUrlImage())
                .placeholder(android.R .drawable.star_on)
                .into(holder.ivLogo);*/

    }

    @Override
    public int getItemCount() {
        return  events.size();
    }

    public class EventViewHolde extends RecyclerView.ViewHolder {

        public ImageView ivLogoEvent;
        public TextView tvTituloEvent;
        public TextView tvDescriptionEvent;


        public EventViewHolde(View itemView) {
            super(itemView);

            ivLogoEvent      = (ImageView) itemView.findViewById(R.id.ivLogoEvent);
            tvTituloEvent    = (TextView) itemView.findViewById(R.id.tvTituloEvent);
            tvDescriptionEvent = (TextView) itemView.findViewById(R.id.tvdescriptionEvent);

        }
    }

    public  void update(List<Event> events){
        this.events = events;
        notifyDataSetChanged();
    }
}
