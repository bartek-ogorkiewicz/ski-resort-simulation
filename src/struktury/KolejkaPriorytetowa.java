package struktury;

import java.util.PriorityQueue;

import symulacja.zdarzenia.Zdarzenie;

/**
 * Kolejka zdarzeń oparta na bibliotecznej kolejce priorytetowej.
 * Zdarzenia są wydawane w kolejności rosnącego czasu, a przy równym czasie
 * w kolejności wstawienia. Porządek ten zapewnia metoda compareTo klasy
 * Zdarzenie, której kolejka priorytetowa używa do porównań.
 */
public class KolejkaPriorytetowa implements KolejkaZdarzen {
    private final PriorityQueue<Zdarzenie> kolejka;

    public KolejkaPriorytetowa() {
        this.kolejka = new PriorityQueue<>();
    }

    @Override
    public void dodaj(Zdarzenie zdarzenie) {
        assert zdarzenie != null;
        kolejka.add(zdarzenie);
    }

    @Override
    public Zdarzenie pobierzPierwsze() {
        assert !czyPusta();
        return kolejka.poll();
    }

    @Override
    public boolean czyPusta() {
        return kolejka.isEmpty();
    }
}
