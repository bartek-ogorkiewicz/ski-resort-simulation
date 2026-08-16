package struktury;

import symulacja.zdarzenia.Zdarzenie;

/**
 * Interfejs kolejki zdarzeń używanej przez symulację.
 * Kolejka wydaje zdarzenia w kolejności niemalejącego czasu,
 * a przy remisach zgodnie z kolejnością wstawienia.
 */
public interface KolejkaZdarzen {
    void dodaj(Zdarzenie zdarzenie);

    Zdarzenie pobierzPierwsze();

    boolean czyPusta();
}
