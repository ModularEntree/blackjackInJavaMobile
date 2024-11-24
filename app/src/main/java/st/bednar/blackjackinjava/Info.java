package st.bednar.blackjackinjava;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;

public class Info implements Parcelable {
    
    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(bank);
        out.writeDouble(sazka);
    }
    public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() {
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        public Info[] newArray(int size) {
            return new Info[size];
        }
    };
    private Info(Parcel in) {
        bank = in.readDouble();
        sazka = in.readDouble();
    }

    /* Konec Implementu */
    static public final double defaultBank = 1000;
    private double bank;
    private double sazka;
    public Info() {
        this.bank = Info.defaultBank;
        this.sazka = 0;
    }
    public Info(double bank) {
        this.bank = bank;
        this.sazka = 0;
    }

    static public String bankChange(double bank, String ballanceTextInfo) {
        return ballanceTextInfo + bank + ",- Kč";
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
}
