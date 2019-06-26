package com.anno.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {

    Button btn_buy_ticket,btn_minus,btn_plus;
    TextView textjumlahtiket,textTotalHarga,textMyBalance,
            nama_wisata_checkout,location_checkout,ketentuan_checkout;
    Integer valueJumlahTiket = 1;
    Integer mybalance = 0;
    Integer valuetotalharga = 0;
    Integer valuehargatiket = 0;
    ImageView notice_uang;
    Integer sisa_balance = 0;

    LinearLayout btnback;

    DatabaseReference reference,reference2,reference3,reference4;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    //generate nomor integer secara random
    //karena kita ingin membuat transaksi secara unik
    Integer nomor_transaksi = new Random().nextInt();

    String date_wisata = "";
    String time_wisata = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();

        //mengambil data dari intent ticketdetailact
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        btn_buy_ticket = findViewById(R.id.btn_buy_ticket);
        btn_minus = findViewById(R.id.btn_minus);
        btn_plus = findViewById(R.id.btn_plus);
        textjumlahtiket = findViewById(R.id.textjumlah_ticket);
        textTotalHarga = findViewById(R.id.text_totalharga);
        textMyBalance = findViewById(R.id.mybalance);
        notice_uang = findViewById(R.id.notice_uang);
        btnback = findViewById(R.id.btn_back);

        nama_wisata_checkout = findViewById(R.id.nama_wisata_checkout);
        location_checkout = findViewById(R.id.location_checkout);
        ketentuan_checkout = findViewById(R.id.ketentuan_checkout);

        //setting value baru untuk beberapa komponen
        textjumlahtiket.setText(valueJumlahTiket.toString());
        textTotalHarga.setText("$US " + valuetotalharga+"");



        //secara default hide btn minus;
        btn_minus.animate().alpha(0).setDuration(300).start();
        btn_minus.setEnabled(false);
        notice_uang.setVisibility(View.GONE);

        //mengambil data user dari firebase
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mybalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                textMyBalance.setText("$US " + mybalance+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_wisata_checkout.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                location_checkout.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan_checkout.setText(dataSnapshot.child("ketentuan").getValue().toString());

                date_wisata = (dataSnapshot.child("date_wisata").getValue().toString());
                time_wisata = (dataSnapshot.child("time_wisata").getValue().toString());

                //data ingeter diubah menjadi string oleh to string lalu diubah lagi jadi integer
                valuehargatiket = Integer.valueOf(dataSnapshot.child("harga_ticket").getValue().toString());



                valuetotalharga = valuehargatiket * valueJumlahTiket;
                textTotalHarga.setText("US$ "+ valuetotalharga+"");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueJumlahTiket+=1;
                textjumlahtiket.setText(valueJumlahTiket.toString());
                if (valueJumlahTiket > 1) {
                    btn_minus.animate().alpha(1).setDuration(300).start();
                    btn_minus.setEnabled(true);
                }
                valuetotalharga = valuehargatiket * valueJumlahTiket;
                textTotalHarga.setText("US$ " + valuetotalharga+"");
                if (valuetotalharga > mybalance){
                    btn_buy_ticket.animate().translationY(250).alpha(0).setDuration(350).start();
                    btn_buy_ticket.setEnabled(false);
                    textMyBalance.setTextColor(Color.parseColor("#D1206B"));
                    notice_uang.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueJumlahTiket-=1;
                textjumlahtiket.setText(valueJumlahTiket.toString());
                if (valueJumlahTiket < 2){
                    btn_minus.animate().alpha(0).setDuration(300).start();
                    btn_minus.setEnabled(false);
                }
                valuetotalharga = valuehargatiket * valueJumlahTiket;
                textTotalHarga.setText("US$ " + valuetotalharga+"");
                if (valuetotalharga < mybalance){
                    btn_buy_ticket.animate().translationY(0).alpha(1).setDuration(350).start();
                    btn_buy_ticket.setEnabled(true);
                    textMyBalance.setTextColor(Color.parseColor("#203DD1"));
                }
            }
        });



        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_buy_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menyimpan data user kepada firebase dan membuat tabel baru "MyTickets"
                reference3 = FirebaseDatabase.getInstance().getReference().child("MyTickets").child(username_key_new).
                        child(nama_wisata_checkout.getText().toString() + nomor_transaksi );
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference3.getRef().child("id_ticket").setValue(nama_wisata_checkout.getText().toString() + nomor_transaksi);
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata_checkout.getText().toString());
                        reference3.getRef().child("lokasi").setValue(location_checkout.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan_checkout.getText().toString());
                        reference3.getRef().child("jumlah_ticket").setValue(valueJumlahTiket.toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);


                        Intent intent = new Intent(TicketCheckoutAct.this,SuccessBuyTicketAct.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //uppdate data balance kepada users (yang saat ini login)
                //mengambil data user dari firebase
                reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        sisa_balance = mybalance - valuetotalharga;
                        reference4.getRef().child("user_balance").setValue(sisa_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }
    //ambil data dari register one username_key dan masukkan kedalam username_key_new
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");

    }
}
