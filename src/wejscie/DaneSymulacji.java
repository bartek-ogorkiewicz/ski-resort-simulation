package wejscie;

import java.util.Arrays;

import osrodek.Osrodek;
import sportowcy.Sportowiec;
import struktury.Asercje;

/**
 * Wynik wczytania danych wejściowych potrzebnych do uruchomienia symulacji:
 * ośrodek narciarski oraz sportowcy biorący udział w symulacji.
 */
public class DaneSymulacji {
    private final Osrodek ośrodek;
    private final Sportowiec[] sportowcy;

    public DaneSymulacji(Osrodek ośrodek, Sportowiec[] sportowcy) {
        assert ośrodek != null;
        assert Asercje.wszystkieNiepuste(sportowcy);

        this.ośrodek = ośrodek;
        this.sportowcy = Arrays.copyOf(sportowcy, sportowcy.length);
    }

    public Osrodek ośrodek() {
        return ośrodek;
    }

    public Sportowiec[] sportowcy() {
        return Arrays.copyOf(sportowcy, sportowcy.length);
    }
}