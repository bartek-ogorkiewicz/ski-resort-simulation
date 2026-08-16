package kryteria;

import osrodek.Trasa;
import sportowcy.Sportowiec;

/**
 * Kryterium oceny trasy uwzględniające znudzenie sportowca daną trasą.
 * Wzór: 1 - z, gdzie z to obecne znudzenie sportowca tą trasą.
 * Im bardziej sportowiec znudzony trasą, tym niższa jej atrakcyjność.
 */
public class Znudzenie implements KryteriumAtrakcyjnosci {
    @Override
    public double oceń(Trasa trasa, Sportowiec sportowiec) {
        assert trasa != null;
        assert sportowiec != null;

        double z = sportowiec.znudzenia().znudzenie(trasa);
        return 1.0 - z;
    }
}
