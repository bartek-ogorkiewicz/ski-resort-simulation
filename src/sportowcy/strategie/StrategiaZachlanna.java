package sportowcy.strategie;

import osrodek.Osrodek;
import osrodek.Trasa;
import sportowcy.Sportowiec;

/**
 * Strategia sportowca zachłannego.
 * Wybiera najatrakcyjniejszą trasę spośród wszystkich tras ośrodka,
 * a następnie układa plan przejazdu do niej.
 */
public class StrategiaZachlanna extends StrategiaPlanowa {
    @Override
    protected Trasa wybierzCel(Sportowiec sportowiec, Osrodek ośrodek) {
        assert sportowiec != null;
        assert ośrodek != null;
        assert ośrodek.liczbaTras() > 0;

        Trasa najlepsza = null;
        // Atrakcyjność jest z przedziału [0, 1], więc -1 oznacza brak kandydata.
        double najlepszaAtrakcyjność = -1.0;

        for (int i = 0; i < ośrodek.liczbaTras(); i++) {
            Trasa trasa = ośrodek.trasa(i);
            double atrakcyjność = sportowiec.preferencje().oceńTrasę(trasa, sportowiec);

            if (atrakcyjność > najlepszaAtrakcyjność) {
                najlepszaAtrakcyjność = atrakcyjność;
                najlepsza = trasa;
            }
        }

        assert najlepsza != null;
        return najlepsza;
    }
}