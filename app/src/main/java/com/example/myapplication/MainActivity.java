package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private MenuItem signUpMenuItem, signInMenuItem, logoutMenuItem;
    DBHelper DBHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Get the Sign Up, Sign In and Logout menu item
        signUpMenuItem = navigationView.getMenu().findItem(R.id.nav_Signup);
        signInMenuItem = navigationView.getMenu().findItem(R.id.nav_Signin);
        logoutMenuItem = navigationView.getMenu().findItem(R.id.nav_logout);

        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

        //Check if user logged in
//        if (isLoggedIn) {
//            signInMenuItem.setVisible(false);
//            logoutMenuItem.setVisible(true);
//        } else {
//            signInMenuItem.setVisible(true);
//            logoutMenuItem.setVisible(false);
//        }

        if (savedInstanceState == null) {
            if (isLoggedIn) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_Signin);
            }
        }

        // Check if host exists in the database
        if (!DBHelper.isHostExists()) {
            // No host exists
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignupFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_Signup);

            signUpMenuItem.setVisible(true);
        } else {
            //Hide Sign Up menu item
            signUpMenuItem.setVisible(false);

            // Host exists
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
            }
        }
    }

    public void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (id == R.id.nav_Signin) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
        }else if (id == R.id.nav_Signup) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignupFragment()).commit();
        } else if (id == R.id.nav_Game) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GameFragment()).commit();
        } else if (id == R.id.nav_Report) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportFragment()).commit();
        } else if (id == R.id.nav_logout) {
            // Logout and clear session
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
