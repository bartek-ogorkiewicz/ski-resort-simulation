package kryteria;

import osrodek.Trasa;
import sportowcy.Sportowiec;

/**
 * Kryterium oceniające trasę pod kątem stopnia wyrównania nawierzchni.
 */
public class WyrownanieNawierzchni implements KryteriumAtrakcyjnosci {
    @Override
    public double oceń(Trasa trasa, Sportowiec sportowiec) {
        assert trasa != null;
        assert sportowiec != null;

        // To kryterium nie zależy od sportowca - patrzy tylko na cechy trasy.
        return trasa.wyrównanieNawierzchni();
    }
}
