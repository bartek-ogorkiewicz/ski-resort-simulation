package graf;

import java.util.ArrayList;
import java.util.List;

import osrodek.Krawedz;

/**
 * Plan przejazdu - sekwencja krawędzi (tras i wyciągów) prowadząca
 * od węzła startowego do docelowego. Pusta lista oznacza, że cel
 * jest tym samym węzłem co start (odległość 0).
 */
public class Plan {
    private final List<Krawedz> krawędzie;

    public Plan(List<Krawedz> krawędzie) {
        assert krawędzie != null;
        this.krawędzie = new ArrayList<>(krawędzie);
    }

    /**
     * Długość planu - liczba krawędzi do przejechania (odległość w grafie).
     */
    public int długość() {
        return krawędzie.size();
    }

    public boolean czyPusty() {
        return krawędzie.isEmpty();
    }

    /**
     * Zwraca i-tą krawędź planu (0 = pierwsza do przejechania).
     */
    public Krawedz krawędź(int i) {
        assert i >= 0 && i < krawędzie.size();
        return krawędzie.get(i);
    }

    /**
     * Zwraca kopię wszystkich krawędzi planu w kolejności przejazdu.
     */
    public List<Krawedz> krawędzie() {
        return new ArrayList<>(krawędzie);
    }
}