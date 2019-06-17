package com.mockup.firebase;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private static final int RC_SIGN_IN = 7;
    List<AuthUI.IdpConfig> providers;

    TextView user;
    Button signout;
    ImageView imageuser;



    private FirebaseAuth autenticacionFire;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Inicializacion de Elelmentos IU
        user = findViewById(R.id.user);
        signout = findViewById(R.id.signout);
        imageuser = findViewById(R.id.imageViewuser);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(navigationView!=null){

        }


        autenticacionFire.getInstance();


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                showsignOptions();

                            }
                        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });



        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );

        showsignOptions();



    }





    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int title;
        switch (menuItem.getItemId()) {
            case R.id.nav_gallery:
                title = R.string.menu_gallery;
                break;
            case R.id.nav_share:
                title = R.string.menu_share;
                break;
            case R.id.nav_send:
                title = R.string.menu_send;
                break;
            default:
                throw new IllegalArgumentException("menu option not implemented!!");
        }


        return true;
    }


    private void showsignOptions() {

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser fireuser = autenticacionFire.getInstance().getCurrentUser();

        if(fireuser!=null) {


            Uri imageuir = fireuser.getPhotoUrl();

            user.setText(fireuser.getDisplayName());

            Picasso.get().load(imageuir)
                    .centerCrop()
                    .resize(200, 200)

                    .into(imageuser);
        }



//        FirebaseUser fireuser = autenticacionFire.getCurrentUser();
  //      Uri imageuir = fireuser.getPhotoUrl();
    //    user.setText(fireuser.getDisplayName());
     //   imageuser.setImageURI(imageuir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RC_SIGN_IN)
        {
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);

            idpResponse.getProviderType();
            if(resultCode ==RESULT_OK){
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                Toast.makeText(this, firebaseUser.getEmail(), Toast.LENGTH_LONG);









            }

        }
    }
}
