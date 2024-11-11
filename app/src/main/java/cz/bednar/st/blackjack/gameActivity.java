package cz.bednar.st.blackjack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class gameActivity extends AppCompatActivity implements InfoIntentExtras{
    protected Info info;
    protected TextView bankStatus;
    protected TextView casinoCardsView;
    protected TextView playerCardsView;
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

        bankStatus = findViewById(R.id.bankStatus);

        casinoCardsView = findViewById(R.id.casinoCards);
        playerCardsView = findViewById(R.id.playerCards);

        buttonHit = findViewById(R.id.hitButton);
        buttonStand = findViewById(R.id.standButton);
        buttonDouble = findViewById(R.id.doubleButton);
        buttonVzdat = findViewById(R.id.vzdatButton);

        Intent toMenu = new Intent(this, menuActivity.class);

        Blackjack game = new Blackjack();
        game.playGame();

        toMenu.putExtra("info", info);
        startActivity(toMenu);
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

    protected class Blackjack {
        // Možné konce hry
        private static final int blackjack = 1;
        private static final int win = 2;
        private static final int lose = 3;
        private static final int winDouble = 4;
        private static final int loseDouble = 5;

        // Násobiče sázky
        private static final double blackjackRate = 0.5;
        private static final int doubleRate = 2;
        public void playGame() {
            Karta [] balicek = Karta.getBalicek();

            Karta [] casinoCards = new Karta[Karta.maxVelikostRuky];
            Karta [] playerCards = new Karta[Karta.maxVelikostRuky];

            Random rand = new Random();

            int randomValue = 0;
            boolean firstRound = false;
            int cardsGiven = 0;

            while (true) {


                cardsGiven++;

                if (cardsGiven == 2) {
                    firstRound = true;
                }

                if (firstRound && (sumHand(playerCards) == Karta.vyherniSum)) {
                    calcNewBank(blackjack);
                    break;
                }
            }
        }

        protected int sumHand(Karta [] hand) {
            int sum = 0;

            for (int i = 0; i <= hand.length ; i++) {
                sum+=hand[i].hodnota;
            }

            return sum;
        }

        protected void calcNewBank(int status) {
            switch (status) {
                case blackjack : {
                    info.setBank(info.getBank() + info.getSazka()*blackjackRate);
                    break;
                }
                case win : {
                    info.setBank(info.getBank() + info.getSazka());
                    break;
                }
                case lose : {
                    info.setBank(info.getBank() - info.getSazka());
                    break;
                }
                case winDouble : {
                    info.setBank(info.getBank() + info.getSazka()*doubleRate);
                    break;
                }
                case loseDouble : {
                    info.setBank(info.getBank() - info.getSazka()*doubleRate);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}

