package com.example.tripper.User;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripper.Common.LoginSignUp.LocationContributorStartupScreen;
import com.example.tripper.Common.LoginSignUp.Login;
import com.example.tripper.Databases.FeaturedPlacesController;
import com.example.tripper.Databases.SessionManager;
import com.example.tripper.HelperClasses.HomeAdapter.CategoriesAdapter;
import com.example.tripper.HelperClasses.HomeAdapter.CategoriesHelperClass;
import com.example.tripper.HelperClasses.HomeAdapter.FeaturedAdapter;
import com.example.tripper.HelperClasses.HomeAdapter.FeaturedHelperClass;
import com.example.tripper.HelperClasses.HomeAdapter.MostViewedAdapter;
import com.example.tripper.HelperClasses.HomeAdapter.MostViewedHelperClass;
import com.example.tripper.HelperClasses.apiController;
import com.example.tripper.HelperClasses.myPlaceAdapter;
import com.example.tripper.HelperClasses.responseModelPlaces;
import com.example.tripper.LocationContributor.LocationContributorDashboard;
import com.example.tripper.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variables
    RecyclerView featuredRecycler, mostViewedRecycler, categoriesRecycler;
    RecyclerView.Adapter adapter;
    GradientDrawable gradient1, gradient2, gradient3, gradient4, gradient5;
    ImageView menuIcon;
    AppCompatTextView locationName;
    ImageView locationImage;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);

        //Recycler Hooks
        featuredRecycler = findViewById(R.id.featured_recycler);
        categoriesRecycler = findViewById(R.id.categories_recycler);
        mostViewedRecycler = findViewById(R.id.most_viewed_recycler);
        menuIcon = findViewById(R.id.menu_icon);

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        locationName=findViewById(R.id.locationName);
        locationImage=findViewById(R.id.locationImage);

        locationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });

        locationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });





        SessionManager sessionManager=new SessionManager(this,SessionManager.SESSION_LOCATION);
        if(!sessionManager.getLocationSession()){
            locationImage.setVisibility(View.VISIBLE);
            locationName.setVisibility(View.INVISIBLE);
        }else{
            locationName.setVisibility(View.VISIBLE);
            locationName.setText(SessionManager.SESSION_LOCATION);
            locationImage.setVisibility(View.INVISIBLE);
        }


        navigationDrawer();


        //RecyclerView Calls
        featuredRecycler();
        mostViewedRecycler();
        categoriesRecycler();
    }

    private void getLocation() {
        startActivity(new Intent(this,UserLocation.class));
    }

    //Navigation Drawer Functions
    private void navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        SessionManager sessionManager=new SessionManager(this,SessionManager.SESSION_USERSESSION);
        if(sessionManager.checkLogin()){
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
        }else{
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
        }

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_all_categories:
                startActivity(new Intent(getApplicationContext(), AllCategories.class));
                break;
            case R.id.nav_add_missing_place:
                startActivity(new Intent(getApplicationContext(),LocationContributorStartupScreen.class));
                finish();
                break;
            case R.id.nav_login:
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;
            case R.id.nav_profile:
                startActivity(new Intent(getApplicationContext(), LocationContributorDashboard.class));
                finish();
                break;
        }
        return true;
    }

    //RecyclerView Functions
    private void mostViewedRecycler() {
        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<MostViewedHelperClass> mostViewedLocation = new ArrayList<>();
        mostViewedLocation.add(new MostViewedHelperClass(R.drawable.manali, "Manali"));
        mostViewedLocation.add(new MostViewedHelperClass(R.drawable.manali, "Manali"));
        mostViewedLocation.add(new MostViewedHelperClass(R.drawable.manali, "Manali"));
        mostViewedLocation.add(new MostViewedHelperClass(R.drawable.manali, "Manali"));

        adapter = new MostViewedAdapter(mostViewedLocation);
        mostViewedRecycler.setAdapter(adapter);

    }

    private void featuredRecycler() {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        Call<List<FeaturedHelperClass>> call = FeaturedPlacesController
                .getInstance()
                .getApiSet()
                .mostRated();

        call.enqueue(new Callback<List<FeaturedHelperClass>>() {
            @Override
            public void onResponse(Call<List<FeaturedHelperClass>> call,
                                   Response<List<FeaturedHelperClass>> response) {
                List<FeaturedHelperClass> data = response.body();
                adapter = new FeaturedAdapter(data);
                featuredRecycler.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<FeaturedHelperClass>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }


    });
    }

    private void categoriesRecycler() {
        gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff8360c3, 0XFF2ebf91});
        gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff1f4037, 0xff99f2c8});
        gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7F7FD5, 0xFF86A8E7, 0xff91EAE4});
        gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffc31432, 0xff240b36});
        gradient5 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff659999, 0xfff4791f});
        ArrayList<CategoriesHelperClass> categoriesHelperClasses = new ArrayList<>();
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient1, R.drawable.restaurant_logo, "Restaurant"));
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient2, R.drawable.hotles_icon, "Hotels"));
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient3, R.drawable.shop_icon, "Shops"));
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient4, R.drawable.airport_icon, "Airports"));
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient5, R.drawable.restaurant_logo, "Restaurant"));

        categoriesRecycler.setHasFixedSize(true);
        adapter = new CategoriesAdapter(categoriesHelperClasses);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(adapter);
    }
}