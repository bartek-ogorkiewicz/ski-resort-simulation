package sportowcy.strategie;

import java.util.ArrayList;
import java.util.List;

import graf.Plan;
import osrodek.Krawedz;
import osrodek.Osrodek;
import osrodek.Trasa;
import osrodek.Wezel;
import osrodek.Wyciag;
import sportowcy.Sportowiec;

/**
 * Strategia sportowca lokalnego (zachowanie z części 1).
 * Rozważa trasy wychodzące z obecnego węzła oraz trasy wychodzące
 * z górnych stacji dostępnych wyciągów. Wybiera najatrakcyjniejszą
 * i układa plan długości 1: albo zjazd tą trasą (jeśli wychodzi z
 * obecnego węzła), albo wjazd prowadzącym do niej wyciągiem.
 * Po zrealizowaniu kroku sportowiec planuje od nowa.
 */
public class StrategiaLokalna extends StrategiaPlanowania {
    @Override
    protected Plan ułóżWłaściwyPlan(Sportowiec sportowiec, Osrodek ośrodek) {
        assert sportowiec != null;
        assert ośrodek != null;

        Wezel węzeł = sportowiec.obecnyWęzeł();

        Trasa najlepszaTrasa = null;
        Wyciag najlepszyWyciąg = null;

        // Atrakcyjność jest z przedziału [0, 1], więc -1 oznacza brak kandydata.
        double najlepszaAtrakcyjność = -1.0;

        // Trasy wychodzące z obecnego węzła.
        for (int i = 0; i < węzeł.liczbaWychodzącychTras(); i++) {
            Trasa trasa = węzeł.wychodzącaTrasa(i);
            double atrakcyjność = sportowiec.preferencje().oceńTrasę(trasa, sportowiec);

            if (atrakcyjność > najlepszaAtrakcyjność) {
                najlepszaAtrakcyjność = atrakcyjność;
                najlepszaTrasa = trasa;
                najlepszyWyciąg = null;
            }
        }

        // Trasy wychodzące z górnych stacji wyciągów z obecnego węzła.
        for (int i = 0; i < węzeł.liczbaWychodzącychWyciągów(); i++) {
            Wyciag wyciąg = węzeł.wychodzącyWyciąg(i);
            Wezel górnaStacja = wyciąg.koniec();

            for (int j = 0; j < górnaStacja.liczbaWychodzącychTras(); j++) {
                Trasa trasa = górnaStacja.wychodzącaTrasa(j);
                double atrakcyjność = sportowiec.preferencje().oceńTrasę(trasa, sportowiec);
                if (atrakcyjność > najlepszaAtrakcyjność) {
                    najlepszaAtrakcyjność = atrakcyjność;
                    najlepszaTrasa = trasa;
                    najlepszyWyciąg = wyciąg;
                }
            }
        }

        Krawedz krok;
        if (najlepszaTrasa != null) {
            // Jeśli najlepsza trasa jest za wyciągiem - najpierw wjazd wyciągiem.
            // W przeciwnym razie zjazd tą trasą.
            krok = (najlepszyWyciąg != null) ? najlepszyWyciąg : najlepszaTrasa;
        } else {
            // Brak dostępnych tras - decyzja zapasowa: dowolny wyciąg.
            assert węzeł.liczbaWychodzącychWyciągów() > 0;
            krok = węzeł.wychodzącyWyciąg(0);
        }

        List<Krawedz> krawędzie = new ArrayList<>();
        krawędzie.add(krok);
        return new Plan(krawędzie);
    }
}