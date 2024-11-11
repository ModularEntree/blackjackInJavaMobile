package cz.bednar.st.blackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class menuActivity extends AppCompatActivity implements InfoIntentExtras{
    protected Info info;
    protected TextView bankStatus = findViewById(R.id.bankStatus);
    protected EditText sazkaCislo;
    protected Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        infoIfExists();
        setBank();

        sazkaCislo = findViewById(R.id.sazkaCislo);

        playButton = findViewById(R.id.playButton);

        Intent toGame = new Intent(this, gameActivity.class);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setSazka(Double.parseDouble(sazkaCislo.getText().toString()));

                if (info.getSazka() < 0) {
                    Toast.makeText(menuActivity.this, "Nemůžete vsadit nekladnou hodnotu.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (info.getSazka() > info.getBank()) {
                    Toast.makeText(menuActivity.this, "Vsázíte víc, než je váš stávající bank.", Toast.LENGTH_SHORT).show();
                    return;
                }

                toGame.putExtra("info", info);
                startActivity(toGame);
            }
        };

        playButton.setOnClickListener(listener);

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

