package com.example.proyectoemdicel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.android.volley.toolbox.ImageLoader;
import com.example.proyectoemdicel.Fragments.Fragmentspersonas;
import com.example.proyectoemdicel.Fragments.Fragmentsprincipal;
import com.example.proyectoemdicel.adapters.productoAdaptador;
import com.example.proyectoemdicel.helpers.QueueUtils;
import com.example.proyectoemdicel.models.Producto;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ViewFlipper v_flipper;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    QueueUtils.QueueObject queue = null;
    ArrayList<Producto> items;
    ImageLoader queueImage = null;
    ListView productoList;
    productoAdaptador productoAdaptador;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ///////////////////////////////////////////////////////////
        v_flipper = findViewById(R.id.v_flipper);
        int images[] = {R.drawable.celular, R.drawable.celular2, R.drawable.celular3};
        v_flipper = findViewById(R.id.v_flipper);
        for (int i = 0; i < images.length; i++) {
            flipperImages(images[i]);
        }
        for (int image : images) {
            flipperImages(image);
        }

        ///////////////////////////////////////////////////////////
        Button iralogin = findViewById(R.id.iralogin);
        iralogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent o = new Intent(MainActivity.this,loginActivity.class);
                startActivity(o);
            }
        });
        /////////////////////////////////////////////////////////
        toolbar = findViewById(R.id.cabezera);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navegador);
        /////////////////////////////////////////////////////
        //lo sgt se implementa luego de haber implementado NavigationView.OnNavigationItemSelectedListener
        navigationView.setNavigationItemSelectedListener(this);
        /////////////////////////////////////////////////////////
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        //////////////////////////////////////////////////////////
        //activar fragment principal
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contenedor, new Fragmentsprincipal());
        fragmentTransaction.commit();
        /////////////////////////////////////////////////////////
        queue = QueueUtils.getInstance(this.getApplicationContext());
        queueImage = queue.getImageLoader();
        items = new ArrayList<>();
        Producto.injectContactsFromCloud(queue, items, this);
        ////////////////////////////////////////////////
        productoList = findViewById(R.id.productoList);
        productoAdaptador = new productoAdaptador(this, items, queueImage);
        productoList.setAdapter(productoAdaptador);
    }
    //////////////////////////////////////////////////////////////////////////////////
    public void flipperImages ( int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(2000); //2 sec
        v_flipper.setAutoStart(true);

        //animation
        v_flipper.setInAnimation(this, android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void refreshList() {
        if (productoAdaptador != null) {
            productoAdaptador.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (menuItem.getItemId() == R.id.home) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contenedor, new Fragmentsprincipal());
            fragmentTransaction.commit();
        }
        if (menuItem.getItemId() == R.id.personas) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contenedor, new Fragmentspersonas());
            fragmentTransaction.commit();
        }
        return false;
    }
    ////////////////////////////////////////////////////////////////////////////////////
}


