package sportowcy;

import java.util.HashMap;
import java.util.Map;

import osrodek.Trasa;

/**
 * Pamięta znudzenie sportowca poszczególnymi trasami.
 * Używa wygładzania wykładniczego z współczynnikiem β.
 *
 * Stosuje leniwe wyliczanie: zamiast aktualizować znudzenie wszystkich
 * tras po każdym zjeździe, dla każdej trasy pamięta tylko numer zjazdu
 * przy jej ostatniej aktualizacji oraz wartość znudzenia w tym momencie.
 * Bieżące znudzenie po i kolejnych zjazdach innymi trasami to z * (1-β)^i.
 */
public class ZnudzeniaTras {
    private final double beta;
    private final Map<Trasa, Integer> ostatniZjazd;
    private final Map<Trasa, Double> ostatniaWartość;

    private int licznikZjazdów;

    public ZnudzeniaTras(double beta) {
        assert beta >= 0.0 && beta <= 1.0;

        this.beta = beta;
        this.ostatniZjazd = new HashMap<>();
        this.ostatniaWartość = new HashMap<>();
        this.licznikZjazdów = 0;
    }

    /**
    * Zwraca bieżące znudzenie daną trasą (z przedziału [0, 1]).
    * Trasy, którą sportowiec nigdy nie zjeżdżał, ma znudzenie 0.
    */
    public double znudzenie(Trasa trasa) {
        assert trasa != null;

        if (!ostatniaWartość.containsKey(trasa)) {
            return 0.0;
        }

        int i = licznikZjazdów - ostatniZjazd.get(trasa);
        return ostatniaWartość.get(trasa) * Math.pow(1.0 - beta, i);
    }

    /**
     * Rejestruje, że sportowiec właśnie zjechał daną trasą.
     * Aktualizuje znudzenie tej trasy zgodnie z wzorem i zwiększa
     * licznik zjazdów. Znudzenie pozostałych tras jest liczone leniwie.
     */
    public void zarejestrujZjazd(Trasa trasa) {
        assert trasa != null;

        double stare = znudzenie(trasa);
        double nowe = beta * 1.0 + (1.0 - beta) * stare;

        licznikZjazdów++;
        ostatniaWartość.put(trasa, nowe);
        ostatniZjazd.put(trasa, licznikZjazdów);
    }
}