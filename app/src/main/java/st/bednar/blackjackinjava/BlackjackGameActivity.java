package st.bednar.blackjackinjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class BlackjackGameActivity extends NavigationActivity implements GamblerStatsIntentExtras {
    protected GamblerStats gamblerStats;
    protected TextView bankStatus;
    protected TextView sazkaStatus;
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

        setToolbar(getString(R.string.BlackjackGameActivityName), true);

        game = new Blackjack();

        bankStatus = findViewById(R.id.bankStatus);
        sazkaStatus = findViewById(R.id.sazkaStatus);

        casinoCardsView = findViewById(R.id.casinoCards);
        playerCardsView = findViewById(R.id.playerCards);

        buttonHit = findViewById(R.id.hitButton);
        buttonStand = findViewById(R.id.standButton);
        buttonDouble = findViewById(R.id.doubleButton);
        buttonVzdat = findViewById(R.id.vzdatButton);

        infoIfExists();
        setBank();
        setSazka();
        toMenu = new Intent(this, BlackjackMenuActivity.class);

        wait = new CountDownLatch(1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                game.playGame();
            }
        }).start();

        View.OnClickListener hitAct = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setAction(Blackjack.hitAction);
                wait.countDown();
                Log.d("GameActivity", "Hit action initiated. CountDown called.");
            }
        };
        View.OnClickListener standAct = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setAction(Blackjack.standAction);
                wait.countDown();
                Log.d("GameActivity", "Hit action initiated. CountDown called.");

            }
        };
        View.OnClickListener doubleAct = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setAction(Blackjack.doubleAction);
                wait.countDown();
                Log.d("GameActivity", "Hit action initiated. CountDown called.");

            }
        };
        View.OnClickListener vzdatAct = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setAction(Blackjack.vzdatAction);
                wait.countDown();
                Log.d("GameActivity", "Hit action initiated. CountDown called.");

            }
        };

        buttonHit.setOnClickListener(hitAct);
        buttonStand.setOnClickListener(standAct);
        buttonDouble.setOnClickListener(doubleAct);
        buttonVzdat.setOnClickListener(vzdatAct);
    }

    public void setSazka() {
        sazkaStatus.setText(GamblerStats.sazkaChange(gamblerStats.getSazka(), getString(R.string.sazkaTextInfo), getString(R.string.moneyTypeTextViewText)));
    }

    @Override
    public void setBank() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bankStatus.setText(GamblerStats.bankChange(gamblerStats.getBank(), getString(R.string.ballanceTextInfo), getString(R.string.moneyTypeTextViewText)));
            }
        });
    }

    @Override
    public void infoIfExists() {
        Bundle extras = getIntent().getExtras();
        Log.d("Info problém", "Prošel infem");
        if (extras != null && extras.containsKey("info")) {
            this.gamblerStats = extras.getParcelable("info");
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

    @Override
    protected void onResume() {
        super.onResume();
        setToolbar(getString(R.string.BlackjackGameActivityName), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_blackjack, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == R.id.toolbarMenuBlackjackGiveUp) {
            Toast.makeText(this, getString(R.string.toolbarMenuBlackjackGameActivityGiveUpText), Toast.LENGTH_SHORT).show();
            game.endOfGameOutsideOfGameloop(Blackjack.lose);
            return true;
        } else if (itemID == R.id.toolbarMenuBlackjackItemOutcomes) {
            Toast.makeText(this, getString(R.string.toolbarMenuBlackjackGameActivityOutcomesText) + ((game.isDoubleWinSet()) ? game.calcNewBank(Blackjack.winDouble) : game.calcNewBank(Blackjack.win)) + "/" + ((game.isDoubleWinSet()) ? game.calcNewBank(Blackjack.loseDouble) : game.calcNewBank(Blackjack.lose)), Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemID == android.R.id.home) {
            Toast.makeText(this, getString(R.string.toolbarMenuBlackjackGameActivityLeaveText), Toast.LENGTH_SHORT).show();
            game.endOfGameOutsideOfGameloop(Blackjack.lose);
            return true;
        }

        return true;
    }

    protected class Blackjack {
        // Toast messages
        protected final String warnCannotDoubleTextBlackjack = getString(R.string.warnCannotDoubleTextBlackjack);
        private final String warnCannotHitOver21TextBlackJack = getString(R.string.warnCannotHitOver21TextBlackJack);

        // Herní konstanty
        private static final int maxVelikostRuky = 7;
        private static final int vyherniSum = 21;
        public static final int maxVelikostBalicku = 12;

        // Herní input

        private boolean doubleWinSet;
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

            int validCardRes;
            boolean firstRoundPassed = false;
            int cardsGiven = 0;
            doubleWinSet = false;
            boolean vzdalTo = false;
            boolean lastRound = false;

            // HRA!
            do {
                if (vzdalTo) break;
                if (!lastRound) {
                    for (int i = 0; i < ((firstRoundPassed) ? 1 : 2 ); i++) {
                        validCardRes = validCard(balicek);
                        casinoCards[cardsGiven] = new Karta(balicek[validCardRes]);

                        validCardRes = validCard(balicek);
                        playerCards[cardsGiven] = new Karta(balicek[validCardRes]);
                    }
                }
                else if (sumHand(playerCards) > sumHand(casinoCards) && sumHand(playerCards) <= vyherniSum){
                    validCardRes = validCard(balicek);

                    casinoCards[cardsGiven] = new Karta(balicek[validCardRes]);
                }

                cardsGiven++;

                if (cardsGiven == 2) {
                    firstRoundPassed = true;
                }

                if (firstRoundPassed) {
                    if (cardsGiven == 2 && sumHand(playerCards) == vyherniSum) {
                        calcNewBankAndSet(blackjack);
                        break;
                    }

                    if (lastRound) {
                        if (sumHand(playerCards) < sumHand(casinoCards)) {
                            if ((doubleWinSet))
                                calcNewBankAndSet(winDouble);
                            else
                                calcNewBankAndSet(win);
                        } else {
                            if ((doubleWinSet))
                                calcNewBankAndSet(loseDouble);
                            else
                                calcNewBankAndSet(lose);
                        }
                        break;
                    }

                    // čeká na input, ach bože

                    setCardsDisplay(casinoCards, playerCards);

                    try {
                        wait.await();
                        wait = new CountDownLatch(1);
                        Log.d("GameActivity", "Wait released, action: " + action);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    switch (action) {
                        case doubleAction: {
                            if (cardsGiven <= 2)
                                doubleWinSet = true;
                            else
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(BlackjackGameActivity.this, warnCannotDoubleTextBlackjack, Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }
                        case hitAction: {
                            if (sumHand(playerCards) <= 21)
                                continue;
                            else
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(BlackjackGameActivity.this, warnCannotHitOver21TextBlackJack, Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }
                        case standAction: {
                            lastRound = true;
                            break;
                        }
                        case vzdatAction: {
                            vzdalTo = true;
                            calcNewBankAndSet(lose);
                            break;
                        }
                        default: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BlackjackGameActivity.this, "Akce nevybrana?", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        }
                    }

                }
            }while(true);

            setCardsDisplay(casinoCards, playerCards);

            toMenu.putExtra("info", gamblerStats);
            startActivity(toMenu);
        }

        public void endOfGameOutsideOfGameloop(int status) {
            calcNewBankAndSet(status);
            toMenu.putExtra("info", gamblerStats);
            startActivity(toMenu);
        }

        protected void setCardsDisplay(Karta [] casinoCards, Karta [] playerCards) {
            StringBuilder finalStringCasino = new StringBuilder();
            StringBuilder finalStringPlayer = new StringBuilder();

            for (int i = 0; i <= casinoCards.length - 1 ; i++) {
                if (casinoCards[i] != null) {
                    finalStringCasino.append(casinoCards[i].oznaceni);
                    if (i != casinoCards.length - 1) finalStringCasino.append(" ");
                }
            }

            for (int i = 0; i <= playerCards.length - 1 ; i++) {
                if (playerCards[i] != null) {
                    finalStringPlayer.append(playerCards[i].oznaceni);
                    if (i != playerCards.length - 1 ) finalStringPlayer.append(" ");
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    casinoCardsView.setText(finalStringCasino.toString());
                    playerCardsView.setText(finalStringPlayer.toString());
                }
            });
        }

        protected int dealtCards(Karta [] hand) {
            int sum = 0;

            for (int i = 0; i <= hand.length - 1; i++) {
                if (hand[i] != null) {
                    sum++;
                }
            }

            return sum;
        }

        protected int sumHand(Karta [] hand) {
            int sum = 0;

            for (int i = 0; i <= hand.length - 1; i++) {
                if (hand[i] != null) {
                    sum += hand[i].hodnota;
                }
            }

            return sum;
        }

        protected int validCard(Karta [] balicek) {
            Random rand = new Random();
            int randomNumber;
            // Náhodně volí karty, které se ještě vyskytují v balíčku
            do {
                randomNumber = rand.nextInt(maxVelikostBalicku);                                    // resolved thanks to issue on github by Martufa, thanks a lot :3
            } while (balicek[randomNumber].pouzito);

            balicek[randomNumber].pouzito = true;

            return randomNumber;
        }

        protected void calcNewBankAndSet(int status) {
            switch (status) {
                case blackjack : {
                    gamblerStats.setBank(gamblerStats.getBank() + gamblerStats.getSazka()*blackjackRate);
                    break;
                }
                case win : {
                    gamblerStats.setBank(gamblerStats.getBank() + gamblerStats.getSazka());
                    break;
                }
                case lose : {
                    gamblerStats.setBank(gamblerStats.getBank() - gamblerStats.getSazka());
                    break;
                }
                case winDouble : {
                    gamblerStats.setBank(gamblerStats.getBank() + gamblerStats.getSazka()*doubleRate);
                    break;
                }
                case loseDouble : {
                    gamblerStats.setBank(gamblerStats.getBank() - gamblerStats.getSazka()*doubleRate);
                    break;
                }
                default: {
                    break;
                }
            }
        }

        protected double calcNewBank(int status) {
            switch (status) {
                case blackjack : {
                    return gamblerStats.getBank() + gamblerStats.getSazka()*blackjackRate;
                }
                case win : {
                    return gamblerStats.getBank() + gamblerStats.getSazka();
                }
                case lose : {
                    return gamblerStats.getBank() - gamblerStats.getSazka();
                }
                case winDouble : {
                    return gamblerStats.getBank() + gamblerStats.getSazka()*doubleRate;
                }
                case loseDouble : {
                    return gamblerStats.getBank() - gamblerStats.getSazka()*doubleRate;
                }
                default: {
                    throw new IllegalArgumentException("Byl zadán nesmyslný výpočet");
                }
            }
        }

        public void setAction(int action) {
            this.action = action;
        }

        public boolean isDoubleWinSet() {
            return doubleWinSet;
        }
    }
}

