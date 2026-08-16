package osrodek;

import java.util.Arrays;

/**
 * Reprezentuje cały graf ośrodka narciarskiego:
 * węzły, trasy oraz wyciągi.
 * Odpowiada za podpięcie tras i wyciągów do ich węzłów początkowych.
 */
public class Osrodek {
    private final Wezel[] węzły;
    private final Trasa[] trasy;
    private final Wyciag[] wyciągi;

    public Osrodek(Wezel[] węzły, Trasa[] trasy, Wyciag[] wyciągi) {
        assert węzły != null && węzły.length > 0;
        assert trasy != null;
        assert wyciągi != null;

        this.węzły = Arrays.copyOf(węzły, węzły.length);
        this.trasy = Arrays.copyOf(trasy, trasy.length);
        this.wyciągi = Arrays.copyOf(wyciągi, wyciągi.length);

        assert węzłyMająPoprawneNumery();
        assert trasyMająPoprawneNumery();
        assert wyciągiMająPoprawneNumery();

        podepnijKrawędzieDoWęzłów();
    }

    public int liczbaWęzłów() {
        return węzły.length;
    }

    public Wezel węzeł(int i) {
        assert i >= 0 && i < węzły.length;
        return węzły[i];
    }

    public int liczbaTras() {
        return trasy.length;
    }

    public Trasa trasa(int i) {
        assert i >= 0 && i < trasy.length;
        return trasy[i];
    }

    public int liczbaWyciągów() {
        return wyciągi.length;
    }

    public Wyciag wyciąg(int i) {
        assert i >= 0 && i < wyciągi.length;
        return wyciągi[i];
    }

    public void wypiszStatystyki() {
        for (int i = 0; i < trasy.length; i++) {
            System.out.println(trasy[i].opisStatystyki());
        }

        for (int i = 0; i < wyciągi.length; i++) {
            System.out.println(wyciągi[i].opisStatystyki());
        }
    }

    private boolean węzłyMająPoprawneNumery() {
        for (int i = 0; i < węzły.length; i++) {
            if (węzły[i] == null || węzły[i].numer() != i) {
                return false;
            }
        }
        return true;
    }

    private boolean trasyMająPoprawneNumery() {
        for (int i = 0; i < trasy.length; i++) {
            if (trasy[i] == null || trasy[i].numer() != i) {
                return false;
            }
        }
        return true;
    }

    private boolean wyciągiMająPoprawneNumery() {
        for (int i = 0; i < wyciągi.length; i++) {
            if (wyciągi[i] == null || wyciągi[i].numer() != i) {
                return false;
            }
        }
        return true;
    }

    /**
     * Podpina krawędzie do węzłów, żeby sportowiec mógł szybko sprawdzić,
     * jakie trasy i wyciągi wychodzą z aktualnego węzła.
     */
    private void podepnijKrawędzieDoWęzłów() {
        for (int i = 0; i < trasy.length; i++) {
            Trasa trasa = trasy[i];
            trasa.początek().dodajWychodzącąTrasę(trasa);
        }

        for (int i = 0; i < wyciągi.length; i++) {
            Wyciag wyciąg = wyciągi[i];
            wyciąg.początek().dodajWychodzącyWyciąg(wyciąg);
        }
    }
}
