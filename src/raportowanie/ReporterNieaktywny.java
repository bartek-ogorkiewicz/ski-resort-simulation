package raportowanie;

import symulacja.Czas;

/**
 * Reporter dla nieśledzonych sportowców - implementacja Null Object.
 * Wszystkie metody są celowo puste. Klasa jest bezstanowa, więc
 * współdzielimy jedną instancję między wszystkich nieśledzonych sportowców.
 */
public final class ReporterNieaktywny implements Reporter {
    public static final ReporterNieaktywny INSTANCJA = new ReporterNieaktywny();

    private ReporterNieaktywny() {
    }

    @Override
    public boolean czyAktywny() {
        return false;
    }

    @Override
    public void raportujRozpoczęcieZjazdu(int numerSportowca, int numerTrasy, Czas czas) {
    }

    @Override
    public void raportujZakończenieZjazdu(int numerSportowca, int numerTrasy, Czas czas) {
    }

    @Override
    public void raportujDołączenieDoKolejki(int numerSportowca, int numerWyciągu, Czas czas) {
    }

    @Override
    public void raportujRozpoczęcieWjazdu(int numerSportowca, int numerWyciągu, Czas czas) {
    }

    @Override
    public void raportujZakończenieWjazdu(int numerSportowca, int numerWyciągu, Czas czas) {
    }
}