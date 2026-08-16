package symulacja;

import java.util.Arrays;

import osrodek.Osrodek;
import osrodek.Trasa;
import osrodek.Wyciag;
import sportowcy.Sportowiec;
import struktury.Asercje;
import struktury.KolejkaZdarzen;
import struktury.KolejkaPriorytetowa;
import symulacja.zdarzenia.CyklWyciagu;
import symulacja.zdarzenia.KoniecWjazdu;
import symulacja.zdarzenia.KoniecZjazdu;
import symulacja.zdarzenia.PrzybycieSportowca;
import symulacja.zdarzenia.Zdarzenie;

/**
 * Główny silnik symulacji zdarzeniowej.
 * Przechowuje kolejkę zdarzeń, aktualny czas symulacji
 * oraz udostępnia obiektom możliwość planowania kolejnych zdarzeń.
 */
public class Symulacja implements Planer {
    private final KolejkaZdarzen kolejka;
    private final Osrodek ośrodek;
    private final Sportowiec[] sportowcy;

    private Czas zegar;
    private long licznikWstawień; // do rozstrzygania remisów czasowych
    private boolean uruchomiona;

    public Symulacja(Osrodek ośrodek, Sportowiec[] sportowcy) {
        assert ośrodek != null;
        assert Asercje.wszystkieNiepuste(sportowcy);

        this.ośrodek = ośrodek;
        this.sportowcy = Arrays.copyOf(sportowcy, sportowcy.length);
        this.kolejka = new KolejkaPriorytetowa();
        this.zegar = Czas.POCZATEK_DNIA;
        this.licznikWstawień = 0;
        this.uruchomiona = false;
    }

    /**
     * Uruchamia symulację. Pobiera kolejne zdarzenia z kolejki w kolejności
     * niemalejącego czasu i prosi je o reakcję, aż kolejka się opróżni.
     */
    public void uruchom() {
        assert !uruchomiona;
        uruchomiona = true;

        zainicjuj();

        while (!kolejka.czyPusta()) {
            Zdarzenie zdarzenie = kolejka.pobierzPierwsze();
            zegar = zdarzenie.czas();
            zdarzenie.obsłuż(this);
        }
    }

    public void wypiszStatystyki() {
        ośrodek.wypiszStatystyki();
    }

    @Override
    public Czas teraz() {
        return zegar;
    }

    @Override
    public void zaplanujKoniecZjazdu(Sportowiec sportowiec, Trasa trasa) {
        assert sportowiec != null;
        assert trasa != null;

        Czas kiedy = zegar.plus(trasa.czasPrzejazdu());
        zaplanuj(new KoniecZjazdu(kiedy, sportowiec, trasa));
    }

    @Override
    public void zaplanujKoniecWjazdu(Sportowiec[] pasażerowie, int liczbaPasażerów, Wyciag wyciąg) {
        assert pasażerowie != null;
        assert wyciąg != null;
        assert liczbaPasażerów > 0;
        assert liczbaPasażerów <= pasażerowie.length;

        assert Asercje.wszystkiePierwszeNiepuste(pasażerowie, liczbaPasażerów);

        Czas kiedy = zegar.plus(wyciąg.czasPrzejazdu());
        zaplanuj(new KoniecWjazdu(kiedy, pasażerowie, liczbaPasażerów, wyciąg));
    }

    @Override
    public void zaplanujCyklWyciągu(Wyciag wyciąg) {
        assert wyciąg != null;

        Czas kiedy = zegar.plus(wyciąg.odstępMiędzyGrupami());
        if (kiedy.nieWcześniejNiż(Czas.KONIEC_PLANOWANIA)) {
            return;
        }
        zaplanuj(new CyklWyciagu(kiedy, wyciąg));
    }

    /**
     * Wstawia do kolejki początkowe zdarzenia symulacji:
     * przybycia wszystkich sportowców oraz pierwsze cykle wyciągów.
     */
    private void zainicjuj() {
        for (int i = 0; i < sportowcy.length; i++) {
            Sportowiec s = sportowcy[i];
            zaplanuj(new PrzybycieSportowca(s.czasPrzybycia(), s));
        }
        for (int i = 0; i < ośrodek.liczbaWyciągów(); i++) {
            Wyciag w = ośrodek.wyciąg(i);
            zaplanuj(new CyklWyciagu(Czas.POCZATEK_DNIA, w));
        }
    }

    private void zaplanuj(Zdarzenie zdarzenie) {
        assert zdarzenie != null;
        zdarzenie.ustawKolejnośćWstawienia(licznikWstawień);
        licznikWstawień++;
        kolejka.dodaj(zdarzenie);
    }
}
