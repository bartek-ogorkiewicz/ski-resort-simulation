package symulacja;

import osrodek.Trasa;
import osrodek.Wyciag;
import sportowcy.Sportowiec;

/**
 * Interfejs udostępniany obiektom, które mogą planować przyszłe zdarzenia
 * w symulacji i potrzebują znać aktualny czas.
 */
public interface Planer {
    Czas teraz();

    void zaplanujKoniecZjazdu(Sportowiec sportowiec, Trasa trasa);

    void zaplanujKoniecWjazdu(Sportowiec[] pasażerowie, int liczbaPasażerów, Wyciag wyciąg);

    void zaplanujCyklWyciągu(Wyciag wyciąg);
}
