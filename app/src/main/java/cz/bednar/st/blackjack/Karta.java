package cz.bednar.st.blackjack;

public class Karta {
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
        Karta [] balicek = new Karta[13];

        balicek[0] = new Karta('A', 1);
        for (int i = 1; i <= 10 ; i++) {
            balicek[i] = new Karta((char) i, i);
        }

        return balicek;
    }
}
