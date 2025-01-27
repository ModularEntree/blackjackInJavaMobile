package st.bednar.blackjackinjava;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BlackjackMenuActivity extends NavigationActivity implements GamblerStatsIntentExtras {
    protected String warnNotPositiveValueTextmenuActivity;
    protected String warnMoreThanAccaptableTextmenuActivity;
    protected String warnMoreThanSaveBet;
    protected GamblerStats gamblerStats;
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

        setBottomNav(R.id.blackjackMenu);
        setToolbar(getString(R.string.BlackjackMenuActivityName), false);

        sazkaCisloView = findViewById(R.id.sazkaCislo);

        playButton = findViewById(R.id.playButton);

        bankStatus = findViewById(R.id.bankStatus);

        warnMoreThanAccaptableTextmenuActivity = getString(R.string.warnMoreThanAccaptableTextmenuActivity);

        warnMoreThanSaveBet = getString(R.string.warnMoreThanSaveBet);

        warnNotPositiveValueTextmenuActivity = getString(R.string.warnNotPositiveValueTextmenuActivity);

        infoIfExists();
        setBank();

        //sazkaCisloView.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);


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
                if (sazkaCislo > gamblerStats.getBank()) {
                    Toast.makeText(BlackjackMenuActivity.this, warnMoreThanAccaptableTextmenuActivity, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sazkaCislo > gamblerStats.getMaxSaveBet()) {
                    Toast.makeText(BlackjackMenuActivity.this, warnMoreThanSaveBet, Toast.LENGTH_SHORT).show();
                    return;
                }

                gamblerStats.setSazka(sazkaCislo);

                toGame.putExtra(GamblerStats.gamblerStatsString, gamblerStats);
                startActivity(toGame);
            }
        };

        playButton.setOnClickListener(listener);

    }
    @Override
    protected void onResume() {
        super.onResume();
        setBottomNav(R.id.blackjackMenu);
        setToolbar(getString(R.string.BlackjackMenuActivityName), false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putParcelable(GamblerStats.gamblerStatsString, gamblerStats);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        gamblerStats = savedInstanceState.getParcelable(GamblerStats.gamblerStatsString);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void setBank() {
        bankStatus.setText(GamblerStats.bankChange(gamblerStats.getBank(), getString(R.string.bankTextViewText), getString(R.string.moneyTypeTextViewText)));
    }

    @Override
    public void infoIfExists() {
        Bundle extras = getIntent().getExtras();
        Log.d("Info problém", "Prošel infem");
        if (extras != null && extras.containsKey(GamblerStats.gamblerStatsString)) {
            this.gamblerStats = extras.getParcelable(GamblerStats.gamblerStatsString);
            Log.d("Info problém", "Pokus o načtení infa");
            if (gamblerStats == null) {
                this.gamblerStats = new GamblerStats();
                Log.d("Info problém", "Pokus selhal");
            }
            else {
                Log.d("Info problém", "Pokus vyšel");
            }
        }
        else {
            this.gamblerStats = new GamblerStats();
            Log.d("Info problém", "Nové info vytvořeno");
        }
    }
}

