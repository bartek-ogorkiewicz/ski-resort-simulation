package raportowanie;

import symulacja.Czas;

/**
 * Odpowiada za raportowanie zdarzeń dotyczących pojedynczego sportowca.
 * Implementacje mogą wypisywać komunikaty albo celowo nic nie robić.
 */
public interface Reporter {
    boolean czyAktywny();

    void raportujRozpoczęcieZjazdu(int numerSportowca, int numerTrasy, Czas czas);

    void raportujZakończenieZjazdu(int numerSportowca, int numerTrasy, Czas czas);

    void raportujDołączenieDoKolejki(int numerSportowca, int numerWyciągu, Czas czas);

    void raportujRozpoczęcieWjazdu(int numerSportowca, int numerWyciągu, Czas czas);

    void raportujZakończenieWjazdu(int numerSportowca, int numerWyciągu, Czas czas);
}