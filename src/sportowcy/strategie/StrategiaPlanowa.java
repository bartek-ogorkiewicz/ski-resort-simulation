package sportowcy.strategie;

import java.util.List;

import graf.Plan;
import graf.PrzeszukiwanieGrafu;
import osrodek.Krawedz;
import osrodek.Osrodek;
import osrodek.Trasa;
import osrodek.Wezel;
import sportowcy.Sportowiec;

/**
 * Wspólna baza strategii układających plan przejazdu do wybranej trasy
 * w całym ośrodku (Zachłanny, Kolekcjoner). Konkretny typ wybiera trasę
 * docelową, a baza wyznacza najkrótszą ścieżkę do niej algorytmem BFS
 * i dokleja na końcu samą trasę docelową.
 */
public abstract class StrategiaPlanowa extends StrategiaPlanowania {
    private final PrzeszukiwanieGrafu przeszukiwanie = new PrzeszukiwanieGrafu();

    @Override
    protected Plan ułóżWłaściwyPlan(Sportowiec sportowiec, Osrodek ośrodek) {
        assert sportowiec != null;
        assert ośrodek != null;

        Trasa cel = wybierzCel(sportowiec, ośrodek);
        assert cel != null;

        Wezel start = sportowiec.obecnyWęzeł();

        // Ścieżka do węzła początkowego trasy docelowej.
        Plan dojazd = przeszukiwanie.znajdźPlan(start, cel.początek());

        // Plan końcowy: dojazd do trasy docelowej, a potem zjazd nią.
        List<Krawedz> krawędzie = dojazd.krawędzie();
        krawędzie.add(cel);
        return new Plan(krawędzie);
    }

    /**
     * Odległość (liczba krawędzi) od węzła startowego do początku trasy.
     */
    protected int odległośćDo(Wezel start, Trasa trasa) {
        assert start != null;
        assert trasa != null;
        return przeszukiwanie.znajdźPlan(start, trasa.początek()).długość();
    }

    /**
     * Wybiera trasę docelową spośród wszystkich tras ośrodka.
     * Sposób wyboru zależy od konkretnego typu sportowca.
     */
    protected abstract Trasa wybierzCel(Sportowiec sportowiec, Osrodek ośrodek);
}