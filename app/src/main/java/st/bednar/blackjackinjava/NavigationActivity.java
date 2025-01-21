package st.bednar.blackjackinjava;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    Toolbar toolbar;

    protected void setBottomNav(int selectedItemId) {
        bottomNav = findViewById(R.id.bottomMenu);

        bottomNav.setSelectedItemId(selectedItemId);

        Intent toBlackjack = new Intent(this, BlackjackMenuActivity.class);
        Intent toBank = new Intent(this, BankActivity.class);
        Intent toAbout = new Intent(this, AboutActivity.class);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.blackjackMenu && !(NavigationActivity.this instanceof BlackjackMenuActivity)) {
                    startActivity(toBlackjack);
                } else if (itemId == R.id.bankMenu && !(NavigationActivity.this instanceof BankActivity)) {
                    startActivity(toBank);
                } else if (itemId == R.id.aboutMenu && !(NavigationActivity.this instanceof AboutActivity)) {
                    startActivity(toAbout);
                } else {
                    return false;
                }
                return true;
            }
        });
    }
    protected void setToolbar(String title, boolean showUp) {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        try {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(showUp);
        } catch (NullPointerException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_def, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == R.id.toolbarMenuDefSayHello) {
            Toast.makeText(this, getString(R.string.toolbarMenuDefActivitySayHelloText), Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemID == R.id.toolbarMenuDefVersion) {
            Toast.makeText(this, getString(R.string.app_name) + getString(R.string.toolbarMenuDefActivityVersionText) + getString(R.string.app_version), Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
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