package cz.bednar.st.blackjack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class menu extends AppCompatActivity {

    private final double defaultBank = 1000;
    private double bank;
    private double sazka;

    public menu() {
        this.bank = this.defaultBank;
    }

    public double getBank() {
        return bank;
    }

    public void setBank(double bank) {
        this.bank = bank;
    }

    public double getSazka() {
        return sazka;
    }

    public void setSazka(double sazka) {
        this.sazka = sazka;
    }

    private String bankChange(double bank) {
        return "Bank: " + bank + ",- Kč";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView bankStatus = findViewById(R.id.bankStatus);
        EditText sazkaCislo = findViewById(R.id.sazkaCislo);
        Button playButton = findViewById(R.id.playButton);

        bankStatus.setText(bankChange(bank));

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sazka = Double.parseDouble(sazkaCislo.getText().toString());

                if (sazka < 0) Toast.makeText(menu.this, "Nemůžete vsadit nekladnou hodnotu.", Toast.LENGTH_SHORT).show();
                if (sazka > bank) Toast.makeText(menu.this, "Vsázíte víc, než je váš stávající bank.", Toast.LENGTH_SHORT).show();


            }
        };

        playButton.setOnClickListener(listener);

    }
}