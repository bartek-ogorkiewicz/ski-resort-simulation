package mapki;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import kadra.mapki.styl.GruboscKonturu;
import kadra.mapki.styl.StylKrawedzi;
import kadra.mapki.styl.StylLinii;
import kadra.mapki.styl.StylWezla;
import osrodek.Krawedz;
import osrodek.Osrodek;
import osrodek.Trasa;
import osrodek.Wezel;
import osrodek.Wyciag;
import sportowcy.Sportowiec;

/**
 * Generuje mapki ośrodka w formacie .tex przy użyciu biblioteki GeneratorMapek.
 * Tworzy trzy rodzaje mapek: parametry tras i wyciągów, statystyki oraz
 * historię przejazdów każdego śledzonego sportowca. Wszystkie mapki powstają
 * jednym generatorem (zerowanym między kolejnymi mapkami).
 */
public class RysownikMapek {
    private final GeneratorMapek generator;
    private final Osrodek ośrodek;

    public RysownikMapek(GeneratorMapek generator, Osrodek ośrodek) {
        assert generator != null;
        assert ośrodek != null;
        this.generator = generator;
        this.ośrodek = ośrodek;
    }

    /**
     * Generuje wszystkie mapki: parametry, statystyki oraz historię każdego
     * śledzonego sportowca.
     */
    public void narysujWszystkie(Sportowiec[] sportowcy) throws WyjatekSystemuPlikow {
        assert sportowcy != null;

        narysujParametry();
        narysujStatystyki();

        for (int i = 0; i < sportowcy.length; i++) {
            if (sportowcy[i].czyŚledzony()) {
                narysujHistorię(sportowcy[i]);
            }
        }
    }

    private void narysujParametry() throws WyjatekSystemuPlikow {
        rysujGraf(this::tekstParametrówTrasy, this::tekstParametrówWyciągu);
        generator.tworzMapke("parametry.tex");
        generator.zeruj();
    }

    private void narysujStatystyki() throws WyjatekSystemuPlikow {
        rysujGraf(this::tekstStatystykTrasy, this::tekstStatystykWyciągu);
        generator.tworzMapke("statystyki.tex");
        generator.zeruj();
    }

    private void narysujHistorię(Sportowiec sportowiec) throws WyjatekSystemuPlikow {
        Map<Krawedz, List<Integer>> historia = sportowiec.historia().numeryWedługKrawędzi();

        dodajWęzły();

        for (int i = 0; i < ośrodek.liczbaTras(); i++) {
            Trasa trasa = ośrodek.trasa(i);
            dodajKrawędźHistorii(trasa, StylLinii.CIAGLA, "t", historia);
        }
        for (int i = 0; i < ośrodek.liczbaWyciągów(); i++) {
            Wyciag wyciąg = ośrodek.wyciąg(i);
            dodajKrawędźHistorii(wyciąg, StylLinii.PRZERYWANA, "w", historia);
        }

        generator.tworzMapke("historia-" + sportowiec.numer() + ".tex");
        generator.zeruj();
    }

    /**
     * Wspólny szkielet rysowania grafu: dodaje wszystkie węzły, a potem
     * wszystkie trasy (linia ciągła) i wyciągi (linia przerywana) z tekstem
     * dostarczonym przez podane funkcje.
     */
    private void rysujGraf(
            Function<Trasa, List<String>> opisTrasy,
            Function<Wyciag, List<String>> opisWyciągu
    ) {
        dodajWęzły();

        for (int i = 0; i < ośrodek.liczbaTras(); i++) {
            Trasa trasa = ośrodek.trasa(i);
            generator.dodajKrawedz(
                    trasa.początek().numer(),
                    trasa.koniec().numer(),
                    new StylKrawedzi(StylLinii.CIAGLA),
                    opisTrasy.apply(trasa)
            );
        }

        for (int i = 0; i < ośrodek.liczbaWyciągów(); i++) {
            Wyciag wyciąg = ośrodek.wyciąg(i);
            generator.dodajKrawedz(
                    wyciąg.początek().numer(),
                    wyciąg.koniec().numer(),
                    new StylKrawedzi(StylLinii.PRZERYWANA),
                    opisWyciągu.apply(wyciąg)
            );
        }
    }

    private void dodajWęzły() {
        for (int i = 0; i < ośrodek.liczbaWęzłów(); i++) {
            Wezel węzeł = ośrodek.węzeł(i);
            GruboscKonturu grubość = węzeł.czySkomunikowany()
                    ? GruboscKonturu.POGRUBIONY
                    : GruboscKonturu.ZWYKLY;
            generator.dodajWezel(
                    węzeł.numer(),
                    węzeł.x(),
                    węzeł.y(),
                    new StylWezla(grubość)
            );
        }
    }

    // Teksty krawędzi: parametry
    private List<String> tekstParametrówTrasy(Trasa trasa) {
        List<String> linie = new ArrayList<>();
        linie.add(String.format(Locale.US,
                "t%d: poziom: %d, czas: %ds",
                trasa.numer(), trasa.trudność(), trasa.czasPrzejazdu()));
        linie.add(String.format(Locale.US,
                "odporność: %.2f, %.5f",
                trasa.bazowaAtrakcyjność(), trasa.odporność()));
        return linie;
    }

    private List<String> tekstParametrówWyciągu(Wyciag wyciąg) {
        List<String> linie = new ArrayList<>();
        linie.add(String.format(Locale.US,
                "w%d: %d os. co %ds",
                wyciąg.numer(), wyciąg.maksymalnaWielkośćGrupy(),
                wyciąg.odstępMiędzyGrupami()));
        linie.add(String.format(Locale.US,
                "czas: %ds", wyciąg.czasPrzejazdu()));
        return linie;
    }

    // Teksty krawędzi: statystyki
    private List<String> tekstStatystykTrasy(Trasa trasa) {
        List<String> linie = new ArrayList<>();
        linie.add(String.format(Locale.US,
                "t%d: śnieg: %.2f",
                trasa.numer(), trasa.wyrównanieNawierzchni()));
        linie.add(String.format(Locale.US,
                "zjazdy: %d", trasa.liczbaPrzejazdów()));
        return linie;
    }

    private List<String> tekstStatystykWyciągu(Wyciag wyciąg) {
        int wjazdy = wyciąg.liczbaPrzejazdów();
        int maks = wyciąg.maksymalnaLiczbaWjazdów();
        int procent = (int) Math.round(100.0 * wjazdy / maks);

        List<String> linie = new ArrayList<>();
        linie.add(String.format(Locale.US,
                "w%d: kol: %d(śr), %d(maks)",
                wyciąg.numer(),
                Math.round(wyciąg.średniaDługośćKolejki()),
                wyciąg.maksymalnaDługośćKolejki()));
        linie.add(String.format(Locale.US,
                "wjazdy: %d / %d (%d%%)", wjazdy, maks, procent));
        return linie;
    }

    // Tekst krawędzi: historia
    private void dodajKrawędźHistorii(
            Krawedz krawędź,
            StylLinii styl,
            String prefiks,
            Map<Krawedz, List<Integer>> historia
    ) {
        List<Integer> numery = historia.get(krawędź);

        String tekst;
        if (numery == null || numery.isEmpty()) {
            tekst = prefiks + krawędź.numer() + "(0):";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(prefiks).append(krawędź.numer())
                    .append("(").append(numery.size()).append("): ");
            for (int i = 0; i < numery.size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(numery.get(i));
            }
            tekst = sb.toString();
        }

        generator.dodajKrawedz(
                krawędź.początek().numer(),
                krawędź.koniec().numer(),
                new StylKrawedzi(styl),
                tekst
        );
    }
}