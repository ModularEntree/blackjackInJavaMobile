package cz.bednar.st.blackjack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class gameActivity extends AppCompatActivity implements InfoIntentExtras{
    protected Info info;
    protected TextView bankStatus;
    protected TextView casinoCardsView;
    protected TextView playerCardsView;
    protected Button buttonHit;
    protected Button buttonStand;
    protected Button buttonDouble;
    protected Button buttonVzdat;
    protected Intent toMenu;
    protected CountDownLatch wait;
    protected Blackjack game;

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

        game = new Blackjack();

        bankStatus = findViewById(R.id.bankStatus);

        casinoCardsView = findViewById(R.id.casinoCards);
        playerCardsView = findViewById(R.id.playerCards);

        buttonHit = findViewById(R.id.hitButton);
        buttonStand = findViewById(R.id.standButton);
        buttonDouble = findViewById(R.id.doubleButton);
        buttonVzdat = findViewById(R.id.vzdatButton);

        View.OnClickListener hitAct = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setAction(Blackjack.hitAction);
                wait.countDown();
            }
        };
        View.OnClickListener standAct = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setAction(Blackjack.standAction);
                wait.countDown();
            }
        };
        View.OnClickListener doubleAct = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setAction(Blackjack.doubleAction);
                wait.countDown();
            }
        };
        View.OnClickListener vzdatAct = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setAction(Blackjack.vzdatAction);
                wait.countDown();
            }
        };

        toMenu = new Intent(this, menuActivity.class);

        buttonHit.setOnClickListener(hitAct);
        buttonStand.setOnClickListener(standAct);
        buttonDouble.setOnClickListener(doubleAct);
        buttonVzdat.setOnClickListener(vzdatAct);

        new Thread(() -> game.playGame());
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
        // Herní konstanty
        private static final int maxVelikostRuky = 7;
        private static final int vyherniSum = 21;
        public static final int maxVelikostBalicku = 12;

        // Herní input

        private int action;

        // Herní konstanty inputů
        public static final int hitAction = 1;
        public static final int standAction = 2;
        public static final int doubleAction = 3;
        public static final int vzdatAction = 4;

        // Možné konce hry
        private static final int blackjack = 1;
        private static final int win = 2;
        private static final int lose = 3;
        private static final int winDouble = 4;
        private static final int loseDouble = 5;

        // Násobiče sázky
        private static final double blackjackRate = 0.5;
        private static final int doubleRate = 2;

        // Hra
        public void playGame() {
            Karta [] balicek = Karta.getBalicek();

            Karta [] casinoCards = new Karta[maxVelikostRuky];
            Karta [] playerCards = new Karta[maxVelikostRuky];

            Random rand = new Random();

            int randomNumber = 0;
            boolean firstRound = false;
            int cardsGiven = 0;

            CountDownLatch wait = new CountDownLatch(1);

            do {
                for (int i = 1; i <= 2 ; i++) {
                    // Náhodně volí karty, které se ještě vyskytují v balíčku
                    do {
                        randomNumber = rand.nextInt(maxVelikostBalicku - 1);
                    } while (balicek[randomNumber].pouzito);

                    balicek[randomNumber].pouzito = true;

                    if (i == 1)
                        casinoCards[cardsGiven] = new Karta(balicek[randomNumber]);
                    else
                        playerCards[cardsGiven] = new Karta(balicek[randomNumber]);
                }

                cardsGiven++;

                if (cardsGiven == 2) {
                    firstRound = true;
                }

                if (firstRound) {
                    if (cardsGiven == 2 && sumHand(playerCards) == vyherniSum) {
                        calcNewBank(blackjack);
                        break;
                    }

                    // čeká na input, ach bože

                    try {
                        wait.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    switch (action) {
                        case hitAction: {

                            break;
                        }
                        case standAction: {

                            break;
                        }
                        case doubleAction: {

                            break;
                        }
                        case vzdatAction: {

                            break;
                        }
                        default: {
                            break;
                        }
                    }

                }
            }while(true);

            toMenu.putExtra("info", info);
            startActivity(toMenu);
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

        public void setAction(int action) {
            this.action = action;
        }
    }
}

