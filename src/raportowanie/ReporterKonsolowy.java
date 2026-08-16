package raportowanie;

import symulacja.Czas;

/**
 * Reporter wypisujący na standardowe wyjście zdarzenia
 * dotyczące śledzonego sportowca.
 */
public class ReporterKonsolowy implements Reporter {
    @Override
    public boolean czyAktywny() {
        return true;
    }

    @Override
    public void raportujRozpoczęcieZjazdu(int numerSportowca, int numerTrasy, Czas czas) {
        assert czas != null;
        assert numerSportowca >= 0;
        assert numerTrasy >= 0;

        System.out.println(czas + ": Sportowiec " + numerSportowca
                + " rozpoczął zjazd trasą nr " + numerTrasy + ".");
    }

    @Override
    public void raportujZakończenieZjazdu(int numerSportowca, int numerTrasy, Czas czas) {
        assert czas != null;
        assert numerSportowca >= 0;
        assert numerTrasy >= 0;

        System.out.println(czas + ": Sportowiec " + numerSportowca
                + " zakończył zjazd trasą nr " + numerTrasy + ".");
    }

    @Override
    public void raportujDołączenieDoKolejki(int numerSportowca, int numerWyciągu, Czas czas) {
        assert czas != null;
        assert numerSportowca >= 0;
        assert numerWyciągu >= 0;

        System.out.println(czas + ": Sportowiec " + numerSportowca
                + " ustawił się w kolejce do wyciągu nr " + numerWyciągu + ".");
    }

    @Override
    public void raportujRozpoczęcieWjazdu(int numerSportowca, int numerWyciągu, Czas czas) {
        assert czas != null;
        assert numerSportowca >= 0;
        assert numerWyciągu >= 0;

        System.out.println(czas + ": Sportowiec " + numerSportowca
                + " rozpoczął wjazd wyciągiem nr " + numerWyciągu + ".");
    }

    @Override
    public void raportujZakończenieWjazdu(int numerSportowca, int numerWyciągu, Czas czas) {
        assert czas != null;
        assert numerSportowca >= 0;
        assert numerWyciągu >= 0;

        System.out.println(czas + ": Sportowiec " + numerSportowca
                + " zszedł z wyciągu nr " + numerWyciągu + ".");
    }
}