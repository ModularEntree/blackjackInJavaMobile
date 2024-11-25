package st.bednar.blackjackinjava;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    protected void setBottomNav(int selectedItemId) {
        bottomNav = findViewById(R.id.bottomMenu);

        Intent toBlackjack = new Intent(this, BlackjackMenuActivity.class);
        Intent toBank = new Intent(this, BankActivity.class);
        Intent toAbout = new Intent(this, AboutActivity.class);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.blackjackMenu) {
                    startActivity(toBlackjack);
                } else if (itemId == R.id.bankMenu) {
                    startActivity(toBank);
                } else if (itemId == R.id.aboutMenu) {
                    startActivity(toAbout);
                } else {
                    return false;
                }
                return true;
            }
        });
        bottomNav.setSelectedItemId(selectedItemId);
    }


    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }*/
}