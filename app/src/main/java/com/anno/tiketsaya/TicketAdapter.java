package com.anno.tiketsaya;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyTicket> myTickets;

    public TicketAdapter(Context c,ArrayList<MyTicket> p){
        context = c;
        myTickets = p;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //menimban layout rv dengan dengan item rv
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_myticket,viewGroup,false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.nama_wisata.setText(myTickets.get(i).getNama_wisata());
        myViewHolder.lokasi.setText(myTickets.get(i).getLokasi());
        myViewHolder.jumlah_ticket.setText(myTickets.get(i).getJumlah_ticket() + " Tickets");

        final String getNamaWisata = myTickets.get(i).getNama_wisata();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomyticketdetails = new Intent(context,MyTicketDetail.class);
                gotomyticketdetails.putExtra("nama_wisata",getNamaWisata);
                context.startActivity(gotomyticketdetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myTickets.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nama_wisata, lokasi , jumlah_ticket;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_wisata = itemView.findViewById(R.id.itm_nama_wisata);
            lokasi = itemView.findViewById(R.id.itm_lokasi);
            jumlah_ticket = itemView.findViewById(R.id.itm_jumlahticket);

        }
    }

}
