package sportowcy;

import java.util.Arrays;
import java.util.Random;

import osrodek.Trasa;
import kryteria.KryteriumAtrakcyjnosci;
import struktury.Asercje;

/**
 * Przechowuje preferencje sportowca wykorzystywane przy wyborze trasy:
 * poziom zaawansowania, spontaniczność, β (współczynnik znudzenia),
 * rodzaj sportowca oraz wagi kryteriów atrakcyjności.
 */
public class PreferencjeSportowca {
    private final int poziomZaawansowania;
    private final double spontaniczność;
    private final double beta;
    private final RodzajSportowca rodzaj;
    private final KryteriumAtrakcyjnosci[] kryteria;
    private final double[] wagi;

    public PreferencjeSportowca(
            int poziomZaawansowania,
            double spontaniczność,
            double beta,
            RodzajSportowca rodzaj,
            KryteriumAtrakcyjnosci[] kryteria,
            double[] wagi
    ) {
        assert poziomZaawansowania >= 0 && poziomZaawansowania <= 10;
        assert spontaniczność >= 0.0 && spontaniczność <= 1.0;
        assert beta >= 0.0 && beta <= 1.0;
        assert rodzaj != null;
        assert kryteria != null && wagi != null;
        assert kryteria.length == wagi.length;
        assert kryteria.length > 0;
        assert Asercje.wszystkieNiepuste(kryteria);
        assert wszystkieWagiWZakresie(wagi);

        double suma = 0;
        for (int i = 0; i < wagi.length; i++) {
            suma += wagi[i];
        }
        assert Math.abs(suma - 1.0) < 1e-9;

        this.poziomZaawansowania = poziomZaawansowania;
        this.spontaniczność = spontaniczność;
        this.beta = beta;
        this.rodzaj = rodzaj;
        this.kryteria = Arrays.copyOf(kryteria, kryteria.length);
        this.wagi = Arrays.copyOf(wagi, wagi.length);
    }

    private static boolean wszystkieWagiWZakresie(double[] wagi) {
        for (int i = 0; i < wagi.length; i++) {
            if (wagi[i] < 0.0 || wagi[i] > 1.0) return false;
        }
        return true;
    }

    public int poziomZaawansowania() {
        return poziomZaawansowania;
    }

    public double beta() {
        return beta;
    }

    public RodzajSportowca rodzaj() {
        return rodzaj;
    }

    public boolean czyZdecydujeSpontanicznie(Random generator) {
        assert generator != null;
        return generator.nextDouble() < spontaniczność;
    }

    public double oceńTrasę(Trasa trasa, Sportowiec sportowiec) {
        assert trasa != null;
        assert sportowiec != null;
        double suma = 0;
        for (int i = 0; i < kryteria.length; i++) {
            suma += wagi[i] * kryteria[i].oceń(trasa, sportowiec);
        }
        return suma;
    }
}

