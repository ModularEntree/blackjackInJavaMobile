package st.bednar.blackjackinjava;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BankActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bank);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setBottomNav(R.id.bankMenu);
        setToolbar(getString(R.string.BankActivityName), false);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setBottomNav(R.id.bankMenu);
        setToolbar(getString(R.string.BankActivityName), false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_bank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == R.id.toolbarMenuBankReset) {
            // Tu se bude resetovat info bank
            return true;
        } else if (itemID == R.id.toolbarMenuBankSafeProgress) {
            // Až se bude ukládat pogress
            return true;
        }

        return true;
    }
}