package cz.bednar.st.blackjack;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class game extends AppCompatActivity implements InfoIntentExtras{
    protected Info info;
    protected TextView bankStatus;
    protected TextView casinoCards;
    protected TextView playerCards;
    protected Button buttonHit;
    protected Button buttonStand;
    protected Button buttonDouble;
    protected Button buttonVzdat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        infoIfExists();
        setBank();

        casinoCards = findViewById(R.id.casinoCards);
        playerCards = findViewById(R.id.playerCards);

        buttonHit = findViewById(R.id.hitButton);
        buttonStand = findViewById(R.id.standButton);
        buttonDouble = findViewById(R.id.doubleButton);
        buttonVzdat = findViewById(R.id.vzdatButton);


    }

    @Override
    public void setBank() {
        bankStatus.setText(Info.bankChange(info.getBank()));
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

