package cz.bednar.st.blackjack;

public class Karta {
    public static final int maxVelikostBalicku = 12;                                                // počet karet v balíčku
    public static final int maxVelikostRuky = 7;                                                    // suma řady 7 je vyšší než 21, nemá smysl pokračovat
    public static final int vyherniSum = 21;
    public char oznaceni;
    public int hodnota;
    public boolean pouzito;
    private Karta(char oznaceni, int hodnota, boolean pouzito) {
        this.oznaceni = oznaceni;
        this.hodnota = hodnota;
        this.pouzito = pouzito;
    }
    private Karta(char oznaceni, int hodnota) {
        this.oznaceni = oznaceni;
        this.hodnota = hodnota;
        this.pouzito = false;
    }

    static public Karta [] getBalicek(){
        Karta [] balicek = new Karta[maxVelikostBalicku];

        balicek[0] = new Karta('A', 1);
        for (int i = 1; i <= 10 ; i++) {
            balicek[i] = new Karta((char) i, i);
        }
        balicek[11] = new Karta('S', 11);

        return balicek;
    }
}
