package struktury;

/**
 * Pomocnicze metody do asercji. Dzięki użyciu w argumencie 'assert',
 * cała pętla wykonuje się tylko gdy włączona jest flaga -ea.
 */
public final class Asercje {
    private Asercje() {
    }

    /**
     * Sprawdza, że żaden element tablicy nie jest nullem.
     */
    public static boolean wszystkieNiepuste(Object[] tablica) {
        if (tablica == null) {
            return false;
        }
        for (int i = 0; i < tablica.length; i++) {
            if (tablica[i] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sprawdza, że pierwszych 'liczba' elementów tablicy nie jest nullami.
     * Pozostałe pozycje mogą być nullami.
     */
    public static boolean wszystkiePierwszeNiepuste(Object[] tablica, int liczba) {
        if (tablica == null) {
            return false;
        }
        if (liczba < 0 || liczba > tablica.length) {
            return false;
        }
        for (int i = 0; i < liczba; i++) {
            if (tablica[i] == null) {
                return false;
            }
        }
        return true;
    }
}