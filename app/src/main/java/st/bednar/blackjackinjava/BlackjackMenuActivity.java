package st.bednar.blackjackinjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BlackjackMenuActivity extends NavigationActivity implements InfoIntentExtras {
    protected String warnNotPositiveValueTextmenuActivity;
    protected String warnMoreThanAccaptableTextmenuActivity;
    protected Info info;
    protected TextView bankStatus;
    protected EditText sazkaCisloView;
    protected Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setToolbar("Hlavní menu", false);

        sazkaCisloView = findViewById(R.id.sazkaCislo);

        playButton = findViewById(R.id.playButton);

        bankStatus = findViewById(R.id.bankStatus);

        warnMoreThanAccaptableTextmenuActivity = getString(R.string.warnMoreThanAccaptableTextmenuActivity);

        warnNotPositiveValueTextmenuActivity = getString(R.string.warnNotPositiveValueTextmenuActivity);

        infoIfExists();
        setBank();

        Intent toGame = new Intent(this, BlackjackGameActivity.class);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double sazkaCislo;

                if (sazkaCisloView.getText().toString().isEmpty())
                    sazkaCislo = 100;
                else
                    sazkaCislo = Double.parseDouble(sazkaCisloView.getText().toString());

                if (sazkaCislo < 0) {
                    Toast.makeText(BlackjackMenuActivity.this, warnNotPositiveValueTextmenuActivity, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sazkaCislo > info.getBank()) {
                    Toast.makeText(BlackjackMenuActivity.this, warnMoreThanAccaptableTextmenuActivity, Toast.LENGTH_SHORT).show();
                    return;
                }

                info.setSazka(sazkaCislo);

                toGame.putExtra("info", info);
                startActivity(toGame);
            }
        };

        playButton.setOnClickListener(listener);

    }
    @Override
    protected void onResume() {
        super.onResume();
        setBottomNav(R.id.blackjackMenu);
    }
    @Override
    public void setBank() {
        bankStatus.setText(Info.bankChange(info.getBank(), getString(R.string.bankTextViewText), getString(R.string.moneyTypeTextViewText)));
    }

    @Override
    public void infoIfExists() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            info = extras.getParcelable("info");
        }
        else {
            info = new Info();
        }
    }
}

