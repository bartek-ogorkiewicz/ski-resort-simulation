package sportowcy.strategie;

import java.util.ArrayList;
import java.util.List;

import graf.Plan;
import osrodek.Krawedz;
import osrodek.Osrodek;
import osrodek.Wezel;
import sportowcy.Sportowiec;

/**
 * Strategia układania planu przejazdu sportowca.
 * Z prawdopodobieństwem spontaniczności sportowiec pomija właściwą
 * procedurę i wybiera losowy pojedynczy przejazd (jak sportowiec lokalny).
 * W przeciwnym razie plan układany jest zgodnie z konkretnym typem.
 */
public abstract class StrategiaPlanowania {
    /**
     * Układa plan kolejnego etapu jazdy sportowca.
     * Najpierw rozważa spontaniczność, potem deleguje do konkretnej strategii.
     */
    public final Plan ułóżPlan(Sportowiec sportowiec, Osrodek ośrodek) {
        assert sportowiec != null;
        assert ośrodek != null;

        if (sportowiec.czyZdecydujeSpontanicznie()) {
            return planLosowy(sportowiec);
        }
        return ułóżWłaściwyPlan(sportowiec, ośrodek);
    }

    /**
     * Właściwa procedura układania planu, zależna od typu sportowca.
     */
    protected abstract Plan ułóżWłaściwyPlan(Sportowiec sportowiec, Osrodek ośrodek);

    /**
     * Spontaniczny wybór: losowy pojedynczy przejazd (trasa lub wyciąg)
     * spośród krawędzi wychodzących z obecnego węzła. Plan ma długość 1.
     */
    protected Plan planLosowy(Sportowiec sportowiec) {
        assert sportowiec != null;

        Wezel węzeł = sportowiec.obecnyWęzeł();
        int liczbaTras = węzeł.liczbaWychodzącychTras();
        int liczbaWyciągów = węzeł.liczbaWychodzącychWyciągów();
        int opcje = liczbaTras + liczbaWyciągów;
        assert opcje > 0;

        int indeks = sportowiec.losujIndeks(opcje);

        Krawedz wybrana;
        if (indeks < liczbaTras) {
            wybrana = węzeł.wychodzącaTrasa(indeks);
        } else {
            wybrana = węzeł.wychodzącyWyciąg(indeks - liczbaTras);
        }

        List<Krawedz> krawędzie = new ArrayList<>();
        krawędzie.add(wybrana);
        return new Plan(krawędzie);
    }
}