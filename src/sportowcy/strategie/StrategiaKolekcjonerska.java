package sportowcy.strategie;

import osrodek.Osrodek;
import osrodek.Trasa;
import osrodek.Wezel;
import sportowcy.Sportowiec;

/**
 * Strategia sportowca kolekcjonera.
 * Wybiera trasę zjeżdżaną przez siebie najmniejszą liczbę razy.
 * Remisy rozstrzyga najpierw mniejszą odległością (liczbą krawędzi
 * dojazdu), a następnie większą atrakcyjnością.
 */
public class StrategiaKolekcjonerska extends StrategiaPlanowa {
    @Override
    protected Trasa wybierzCel(Sportowiec sportowiec, Osrodek ośrodek) {
        assert sportowiec != null;
        assert ośrodek != null;
        assert ośrodek.liczbaTras() > 0;

        Wezel start = sportowiec.obecnyWęzeł();

        int minPrzejazdy = Integer.MAX_VALUE;
        for (int i = 0; i < ośrodek.liczbaTras(); i++) {
            int przejazdy = sportowiec.liczbaWłasnychPrzejazdów(ośrodek.trasa(i));

            if (przejazdy < minPrzejazdy) {
                minPrzejazdy = przejazdy;
            }
        }

        // Drugie przejście: wśród tras o minimalnej liczbie przejazdów
        // wybierz najbliższą, a przy remisie najatrakcyjniejszą.
        Trasa najlepsza = null;
        int najlepszaOdległość = Integer.MAX_VALUE;
        double najlepszaAtrakcyjność = -1.0;

        for (int i = 0; i < ośrodek.liczbaTras(); i++) {
            Trasa trasa = ośrodek.trasa(i);

            if (sportowiec.liczbaWłasnychPrzejazdów(trasa) != minPrzejazdy) {
                continue;
            }

            int odległość = odległośćDo(start, trasa);
            double atrakcyjność = sportowiec.preferencje().oceńTrasę(trasa, sportowiec);

            boolean lepsza = odległość < najlepszaOdległość
                    || (odległość == najlepszaOdległość
                    && atrakcyjność > najlepszaAtrakcyjność);

            if (lepsza) {
                najlepsza = trasa;
                najlepszaOdległość = odległość;
                najlepszaAtrakcyjność = atrakcyjność;
            }
        }
        assert najlepsza != null;
        return najlepsza;
    }
}