package osrodek;

import java.util.Locale;

/**
 * Reprezentuje trasę narciarską, czyli krawędź prowadzącą w dół stoku.
 * Oprócz danych wspólnych dla krawędzi przechowuje poziom trudności
 * oraz parametry używane do oceny atrakcyjności trasy.
 */
public class Trasa extends Krawedz {
    private final int trudność;
    private final double bazowaAtrakcyjność;
    private final double odporność;

    public Trasa(
            int numer,
            Wezel początek,
            Wezel koniec,
            int trudność,
            int czasPrzejazdu,
            double bazowaAtrakcyjność,
            double odporność
    ) {
        super(numer, początek, koniec, czasPrzejazdu);

        assert początek.wysokość() > koniec.wysokość();
        assert trudność >= 0 && trudność <= 10;
        assert bazowaAtrakcyjność >= 0.0 && bazowaAtrakcyjność <= 1.0;
        assert odporność > 0.0 && odporność <= 1.0;

        this.trudność = trudność;
        this.bazowaAtrakcyjność = bazowaAtrakcyjność;
        this.odporność = odporność;
    }

    public int trudność() {
        return trudność;
    }

    public double bazowaAtrakcyjność() {
        return bazowaAtrakcyjność;
    }

    public double odporność() {
        return odporność;
    }

    /**
     * Atrakcyjność wynikająca z wyrównania nawierzchni przy obecnej
     * liczbie przejazdów. Maleje wykładniczo wraz z kolejnymi zjazdami.
     */
    public double wyrównanieNawierzchni() {
        return bazowaAtrakcyjność + (1.0 - bazowaAtrakcyjność)
                * Math.pow(odporność, liczbaPrzejazdów());
    }

    @Override
    public String opisStatystyki() {
        return String.format(Locale.US,
                "Trasa %d: przejazdów: %d, wyrównanie nawierzchni: %.2f",
                numer(), liczbaPrzejazdów(), wyrównanieNawierzchni());
    }

    @Override
    public String toString() {
        return "t" + numer();
    }
}
