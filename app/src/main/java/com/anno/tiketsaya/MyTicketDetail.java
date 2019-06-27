package com.anno.tiketsaya;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTicketDetail extends AppCompatActivity {

    DatabaseReference reference;
    TextView nama_wisata,lokasi,time_wisata,date_wisata,keterangan_wisata;
    LinearLayout btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);

        nama_wisata = findViewById(R.id.nama_wisata_detail);
        time_wisata = findViewById(R.id.time_detail);
        date_wisata = findViewById(R.id.date_detail);
        lokasi = findViewById(R.id.lokasi_detail);
        keterangan_wisata = findViewById(R.id.keterangan_detail);
        btn_back = findViewById(R.id.btn_back);

        //mengambil data dari intent TicketAdapter
        Bundle bundle = getIntent().getExtras();
        final String nama_wisata_baru = bundle.getString("nama_wisata");

        //mengabil data dari firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                time_wisata.setText(dataSnapshot.child("time_wisata").getValue().toString());
                date_wisata.setText(dataSnapshot.child("date_wisata").getValue().toString());
                keterangan_wisata.setText(dataSnapshot.child("ketentuan").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

      btn_back.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              onBackPressed();
          }
      });

    }
}
