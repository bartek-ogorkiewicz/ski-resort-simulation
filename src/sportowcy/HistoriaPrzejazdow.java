package sportowcy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import osrodek.Krawedz;

/**
 * Historia przejazdów śledzonego sportowca w kolejności czasowej.
 * Każdy przejazd (trasą lub wyciągiem) dostaje kolejny numer (od 1).
 * Numeracja jest wspólna dla tras i wyciągów. Pozwala odczytać, ile
 * razy i z jakimi numerami sportowiec pokonał daną krawędź.
 */
public class HistoriaPrzejazdow {
    private final List<Krawedz> krawędzie;

    public HistoriaPrzejazdow() {
        this.krawędzie = new ArrayList<>();
    }

    /**
     * Rejestruje kolejny przejazd daną krawędzią. Numer przejazdu
     * wynika z kolejności wywołań (pierwszy przejazd ma numer 1).
     */
    public void zarejestruj(Krawedz krawędź) {
        assert krawędź != null;
        krawędzie.add(krawędź);
    }

    /**
     * Grupuje numery przejazdów według krawędzi. Dla każdej krawędzi,
     * którą sportowiec pokonał, zwraca listę numerów jej przejazdów
     * (w kolejności czasowej, od 1). Jedno przejście po historii.
     */
    public Map<Krawedz, List<Integer>> numeryWedługKrawędzi() {
        Map<Krawedz, List<Integer>> wynik = new HashMap<>();
        for (int i = 0; i < krawędzie.size(); i++) {
            Krawedz krawędź = krawędzie.get(i);
            if (!wynik.containsKey(krawędź)) {
                wynik.put(krawędź, new ArrayList<>());
            }
            wynik.get(krawędź).add(i + 1);
        }
        return wynik;
    }
}