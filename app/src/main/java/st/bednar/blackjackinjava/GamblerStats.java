package st.bednar.blackjackinjava;

import android.os.Parcel;
import android.os.Parcelable;

public class GamblerStats implements Parcelable {
    
    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(bank);
        out.writeDouble(sazka);
    }
    public static final Parcelable.Creator<GamblerStats> CREATOR = new Parcelable.Creator<GamblerStats>() {
        public GamblerStats createFromParcel(Parcel in) {
            return new GamblerStats(in);
        }

        public GamblerStats[] newArray(int size) {
            return new GamblerStats[size];
        }
    };
    private GamblerStats(Parcel in) {
        bank = in.readDouble();
        sazka = in.readDouble();
    }

    /* Konec Implementu */
    enum settings {
        SAVEPLAYON, SAVEPLAYOFF
    }
    static public final String gamblerStatsString = "GamblerStats";
    static protected final double ballanceDef = 1000;
    static protected final double maxSaveBetDef = 1000;
    private double maxSaveBet;
    private double bank;
    private double sazka;
    public GamblerStats() {
        this.bank = GamblerStats.ballanceDef;
        this.sazka = 0;
        this.maxSaveBet = maxSaveBetDef;
    }
    public GamblerStats(double bank) {
        this.bank = bank;
        this.sazka = 0;
    }

    static public String bankChange(double bank, String ballanceTextInfo, String moneyTypeTextViewText) {
        return ballanceTextInfo + bank + moneyTypeTextViewText;
    }
    static public String sazkaChange(double bank, String sazkaTextInfo, String moneyTypeTextViewText) {
        return sazkaTextInfo + bank + moneyTypeTextViewText;
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

    public double getMaxSaveBet() {
        return maxSaveBet;
    }
    public void setMaxSaveBet(double maxSaveBet) {
        this.maxSaveBet = maxSaveBet;
    }
}
