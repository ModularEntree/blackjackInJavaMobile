package cz.bednar.st.blackjack;

import android.os.Bundle;
import android.widget.TextView;

import java.text.BreakIterator;

public interface InfoIntentExtras {
    Info info = null;
    void setBank();
    void infoIfExists();
}
