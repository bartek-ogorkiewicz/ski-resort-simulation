package kryteria;

import osrodek.Trasa;
import sportowcy.Sportowiec;

/**
 * Kryterium oceny atrakcyjności trasy z punktu widzenia konkretnego sportowca.
 * W zależności od typu kryterium, może użyć różnych aspektów sportowca:
 * jego poziomu, znudzenia daną trasą, itp.
 */
public interface KryteriumAtrakcyjnosci {
    double oceń(Trasa trasa, Sportowiec sportowiec);
}