package sportowcy;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;

import graf.Plan;
import osrodek.Krawedz;
import osrodek.Osrodek;
import osrodek.Trasa;
import osrodek.Wezel;
import osrodek.Wyciag;
import raportowanie.Reporter;
import sportowcy.strategie.StrategiaPlanowania;
import symulacja.Planer;
import symulacja.Czas;

/**
 * Reprezentuje pojedynczego sportowca biorącego udział w symulacji.
 * Przechowuje jego stan, położenie, preferencje oraz realizuje plany
 * przejazdu układane przez przypisaną strategię.
 */
public class Sportowiec {
    private final int numer;
    private final PreferencjeSportowca preferencje;
    private final Wezel węzełStartowy;
    private final Czas czasPrzybycia;
    private final Reporter reporter;
    private final Random generator;
    private final ZnudzeniaTras znudzenia;
    private final Map<Trasa, Integer> własnePrzejazdy;
    private final HistoriaPrzejazdow historia;
    private final Osrodek ośrodek;
    private final StrategiaPlanowania strategia;

    private Wezel obecnyWęzeł;
    private StanSportowca stan;
    private Plan obecnyPlan;
    private int krokPlanu;

    public Sportowiec(
            int numer,
            PreferencjeSportowca preferencje,
            Wezel węzełStartowy,
            Czas czasPrzybycia,
            Reporter reporter,
            Random generator,
            Osrodek ośrodek,
            StrategiaPlanowania strategia
    ) {
        assert numer >= 0;
        assert preferencje != null;
        assert węzełStartowy != null;
        assert węzełStartowy.czySkomunikowany();
        assert czasPrzybycia != null;
        assert reporter != null;
        assert generator != null;
        assert ośrodek != null;
        assert strategia != null;

        this.numer = numer;
        this.preferencje = preferencje;
        this.węzełStartowy = węzełStartowy;
        this.czasPrzybycia = czasPrzybycia;
        this.reporter = reporter;
        this.generator = generator;
        this.znudzenia = new ZnudzeniaTras(preferencje.beta());
        this.własnePrzejazdy = new HashMap<>();
        this.historia = new HistoriaPrzejazdow();
        this.ośrodek = ośrodek;
        this.strategia = strategia;

        this.obecnyWęzeł = null;
        this.stan = StanSportowca.PRZED_PRZYJAZDEM;
        this.obecnyPlan = null;
        this.krokPlanu = 0;
    }

    public int numer() {
        return numer;
    }

    public PreferencjeSportowca preferencje() {
        return preferencje;
    }

    public ZnudzeniaTras znudzenia() {
        return znudzenia;
    }

    public int poziomZaawansowania() {
        return preferencje.poziomZaawansowania();
    }

    public StanSportowca stan() {
        return stan;
    }

    public Wezel obecnyWęzeł() {
        return obecnyWęzeł;
    }

    public Czas czasPrzybycia() {
        return czasPrzybycia;
    }

    public HistoriaPrzejazdow historia() {
        return historia;
    }

    public boolean czyŚledzony() {
        return reporter.czyAktywny();
    }

    public boolean czyZdecydujeSpontanicznie() {
        return preferencje.czyZdecydujeSpontanicznie(generator);
    }

    public int losujIndeks(int zakres) {
        assert zakres > 0;
        return generator.nextInt(zakres);
    }

    public void przyjedźNaStok(Planer planer) {
        assert planer != null;
        assert stan == StanSportowca.PRZED_PRZYJAZDEM;

        obecnyWęzeł = węzełStartowy;
        stan = StanSportowca.W_WEZLE;

        podejmijDecyzję(planer);
    }

    public void zakończZjazd(Trasa trasa, Planer planer) {
        assert trasa != null;
        assert planer != null;
        assert stan == StanSportowca.NA_TRASIE;

        reporter.raportujZakończenieZjazdu(numer, trasa.numer(), planer.teraz());
        obecnyWęzeł = trasa.koniec();
        stan = StanSportowca.W_WEZLE;

        podejmijDecyzję(planer);
    }

    public void rozpocznijWjazd(Wyciag wyciąg, Planer planer) {
        assert wyciąg != null;
        assert planer != null;
        assert stan == StanSportowca.W_KOLEJCE;

        reporter.raportujRozpoczęcieWjazdu(numer, wyciąg.numer(), planer.teraz());
        if (reporter.czyAktywny()) {
            historia.zarejestruj(wyciąg);
        }
        stan = StanSportowca.NA_WYCIAGU;
    }

    public void zakończWjazd(Wyciag wyciąg, Planer planer) {
        assert wyciąg != null;
        assert planer != null;
        assert stan == StanSportowca.NA_WYCIAGU;

        reporter.raportujZakończenieWjazdu(numer, wyciąg.numer(), planer.teraz());
        obecnyWęzeł = wyciąg.koniec();
        stan = StanSportowca.W_WEZLE;

        podejmijDecyzję(planer);
    }

    /**
     * Decyduje o kolejnej akcji sportowca. Po 15:00 sportowiec kończy
     * dzień (stan PO_DNIU). W przeciwnym razie realizuje kolejny krok
     * planu, a gdy plan się wyczerpał - prosi strategię o nowy.
     */
    private void podejmijDecyzję(Planer planer) {
        assert planer != null;
        assert stan == StanSportowca.W_WEZLE;

        if (planer.teraz().nieWcześniejNiż(Czas.KONIEC_PLANOWANIA)) {
            stan = StanSportowca.PO_DNIU;
            return;
        }

        if (planWyczerpany()) {
            obecnyPlan = strategia.ułóżPlan(this, ośrodek);
            krokPlanu = 0;
        }

        Krawedz krawędź = obecnyPlan.krawędź(krokPlanu);
        krokPlanu++;

        if (krawędź instanceof Trasa trasa) {
            rozpocznijZjazd(trasa, planer);
        } else if (krawędź instanceof Wyciag wyciąg) {
            ustawSięDoWyciągu(wyciąg, planer);
        } else {
            assert false : "Nieznany typ krawędzi w planie";
        }
    }

    private boolean planWyczerpany() {
        return obecnyPlan == null || krokPlanu >= obecnyPlan.długość();
    }

    private void rozpocznijZjazd(Trasa trasa, Planer planer) {
        assert trasa != null;
        assert planer != null;
        assert stan == StanSportowca.W_WEZLE;
        assert trasa.początek() == obecnyWęzeł;

        reporter.raportujRozpoczęcieZjazdu(numer, trasa.numer(), planer.teraz());
        trasa.zarejestrujPrzejazd();
        znudzenia.zarejestrujZjazd(trasa);
        zarejestrujWłasnyPrzejazd(trasa);
        if (reporter.czyAktywny()) {
            historia.zarejestruj(trasa);
        }
        stan = StanSportowca.NA_TRASIE;
        planer.zaplanujKoniecZjazdu(this, trasa);
    }

    private void ustawSięDoWyciągu(Wyciag wyciąg, Planer planer) {
        assert wyciąg != null;
        assert planer != null;
        assert stan == StanSportowca.W_WEZLE;
        assert wyciąg.początek() == obecnyWęzeł;

        reporter.raportujDołączenieDoKolejki(numer, wyciąg.numer(), planer.teraz());
        wyciąg.dodajDoKolejki(this, planer.teraz());
        stan = StanSportowca.W_KOLEJCE;
    }

    private void zarejestrujWłasnyPrzejazd(Trasa trasa) {
        assert trasa != null;
        int obecne = własnePrzejazdy.getOrDefault(trasa, 0);
        własnePrzejazdy.put(trasa, obecne + 1);
    }

    public int liczbaWłasnychPrzejazdów(Trasa trasa) {
        assert trasa != null;
        return własnePrzejazdy.getOrDefault(trasa, 0);
    }

    @Override
    public String toString() {
        return "Sportowiec " + numer;
    }
}
