package hr.dostavifrende.dostavifrende;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //GOLD

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ActiveUsersFragment()).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    android.support.v4.app.Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_active_users:
                            selectedFragment = new ActiveUsersFragment();
                            break;
                        case R.id.nav_offer:
                            selectedFragment = new OfferServiceFragment();
                            selectedFragment = isUserLoggedIn(selectedFragment);
                            break;
                        case R.id.nav_chat:
                            selectedFragment = new ChatFragment();
                            selectedFragment = isUserLoggedIn(selectedFragment);
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            selectedFragment = isUserLoggedIn(selectedFragment);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    public void openRegister(View v){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
    public void openLogin(View v){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
    public void signOut(View v){
        auth.signOut();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new UserUnknownFragment()).commit();
    }
    private android.support.v4.app.Fragment isUserLoggedIn(android.support.v4.app.Fragment returnFragment){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null){
            returnFragment = new UserUnknownFragment();
        }
        return returnFragment;
    }
}
