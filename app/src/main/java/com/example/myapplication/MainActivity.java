package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private MenuItem signInMenuItem, logoutMenuItem;
    private DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Get the Sign In and Logout menu items
        signInMenuItem = navigationView.getMenu().findItem(R.id.nav_Signin);
        logoutMenuItem = navigationView.getMenu().findItem(R.id.nav_logout);

        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

        // Update the menu based on login status
        updateMenu(isLoggedIn);

        if (savedInstanceState == null) {
            if (isLoggedIn) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_Signin);
            }
        }
    }

    private void updateMenu(boolean isLoggedIn) {
        signInMenuItem.setVisible(!isLoggedIn);
        logoutMenuItem.setVisible(isLoggedIn);
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
        } else if (id == R.id.nav_Game) {
            if (checkRestrictedAccess(isLoggedIn)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GameFragment()).commit();
            }
        } else if (id == R.id.nav_result){
            if (checkRestrictedAccess(isLoggedIn)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ResultFragment()).commit();
            }
        } else if (id == R.id.nav_Report) {
            if (checkRestrictedAccess(isLoggedIn)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportFragment()).commit();
            }
        } else if (id == R.id.nav_logout) {
            // Logout and clear session
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Update the menu after logout
            updateMenu(false);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkRestrictedAccess(boolean isLoggedIn) {
        if (!isLoggedIn) {
            Toast.makeText(this, "Please log in first.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!dbHelper.isHostExists()) {
            Toast.makeText(this, "Host record does not exist. Please sign in first.", Toast.LENGTH_SHORT).show();
            return false;
        }
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
