package kryteria;

import osrodek.Trasa;
import sportowcy.Sportowiec;

/**
 * Kryterium oceniające trasę pod kątem dopasowania jej trudności
 * do poziomu zaawansowania sportowca.
 */
public class DopasowanieTrudnosci implements KryteriumAtrakcyjnosci {
    @Override
    public double oceń(Trasa trasa, Sportowiec sportowiec) {
        assert trasa != null;
        assert sportowiec != null;

        int pt = trasa.trudność();
        int pn = sportowiec.preferencje().poziomZaawansowania();

        assert pn >= 0 && pn <= 10;

        if (pt >= pn + 5) {
            return 0.0;
        }

        if (pt >= pn) {
            return 1.0 - (pt - pn) / 5.0;
        }

        return Math.max(0.2, 1.0 - (pn - pt) / 7.0);
    }
}
