package wejscie;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import kryteria.KryteriumAtrakcyjnosci;
import osrodek.Osrodek;
import osrodek.Wezel;
import raportowanie.Reporter;
import raportowanie.ReporterKonsolowy;
import raportowanie.ReporterNieaktywny;
import sportowcy.PreferencjeSportowca;
import sportowcy.RodzajSportowca;
import sportowcy.Sportowiec;
import sportowcy.strategie.FabrykaStrategii;
import sportowcy.strategie.StrategiaPlanowania;
import struktury.Asercje;
import symulacja.Czas;

/**
 * Parser sekcji sportowców (format części 2).
 * Z opisów grup tworzy pojedynczych sportowców z odpowiednimi
 * preferencjami, czasem przybycia i reporterem.
 *
 * Format grupy (3 linie):
 *  1. liczba poziom spontaniczność β rodzaj [s]
 *  2. αd αw αz
 *  3. węzeł godzina [odstęp]
 *
 * Liczba sportowców na wejściu nie jest znana z góry (znamy tylko
 * liczbę grup), więc tablica jest powiększana dynamicznie.
 */
public class ParserSportowcow extends ParserSekcji {
    private final Wezel[] węzły;
    private final Random generator;
    private final KryteriumAtrakcyjnosci[] kryteria;
    private final Osrodek ośrodek;

    private Sportowiec[] sportowcy;
    private int liczbaSportowców;

    public ParserSportowcow(
            Scanner scanner,
            Wezel[] węzły,
            Random generator,
            KryteriumAtrakcyjnosci[] kryteria,
            Osrodek ośrodek
    ) {
        super(scanner);

        assert generator != null;
        assert Asercje.wszystkieNiepuste(węzły);
        assert Asercje.wszystkieNiepuste(kryteria);
        assert kryteria.length == 3;
        assert ośrodek != null;

        this.węzły = węzły;
        this.generator = generator;
        this.kryteria = kryteria;
        this.ośrodek = ośrodek;
        this.sportowcy = null;
        this.liczbaSportowców = 0;
    }

    @Override
    protected void zarezerwuj(int liczbaGrup) {
        sportowcy = new Sportowiec[Math.max(16, liczbaGrup)];
        liczbaSportowców = 0;
    }

    @Override
    protected void parsujElement(int numerGrupy) {
        // Linia 1: liczba, poziom, spontaniczność, β, rodzaj (L/Z/K), opcjonalnie "s"
        Scanner linia1 = czytajLinię();

        int liczbaWGrupie = linia1.nextInt();
        int poziom = linia1.nextInt();
        double spontaniczność = linia1.nextDouble();
        double beta = linia1.nextDouble();
        String literaRodzaju = linia1.next();
        RodzajSportowca rodzaj = RodzajSportowca.zLitery(literaRodzaju);

        boolean śledzeni = false;

        if (linia1.hasNext()) {
            String znacznik = linia1.next();
            assert znacznik.equals("s");
            śledzeni = true;
        }

        assert !linia1.hasNext();

        // Linia 2: 3 wagi - dopasowanie, wyrównanie, znudzenie
        Scanner linia2 = czytajLinię();

        double wagaDopasowania = linia2.nextDouble();
        double wagaWyrównania = linia2.nextDouble();
        double wagaZnudzenia = linia2.nextDouble();

        assert !linia2.hasNext();

        // Linia 3: węzeł startowy, pierwsza godzina, opcjonalnie odstęp
        Scanner linia3 = czytajLinię();

        int nrWęzłaStart = linia3.nextInt();
        String godzinaStr = linia3.next();
        Czas pierwszaGodzina = Czas.zNapisu(godzinaStr);

        int odstęp = 0;

        // Dla grup wieloosobowych odstęp jest wymagany.
        if (liczbaWGrupie > 1) {
            assert linia3.hasNextInt();
            odstęp = linia3.nextInt();
        } else if (linia3.hasNextInt()) {
            odstęp = linia3.nextInt();
        }

        assert !linia3.hasNext();

        assert liczbaWGrupie > 0;
        assert poziom >= 0 && poziom <= 10;
        assert spontaniczność >= 0.0 && spontaniczność <= 1.0;
        assert beta >= 0.0 && beta <= 1.0;
        assert odstęp >= 0;
        assert nrWęzłaStart >= 0 && nrWęzłaStart < węzły.length;

        Wezel węzełStart = węzły[nrWęzłaStart];

        assert węzełStart != null;
        assert węzełStart.czySkomunikowany();

        double[] wagi = { wagaDopasowania, wagaWyrównania, wagaZnudzenia };

        // Wszyscy sportowcy z grupy dzielą wspólny obiekt preferencji
        PreferencjeSportowca preferencje = new PreferencjeSportowca(
                poziom,
                spontaniczność,
                beta,
                rodzaj,
                kryteria,
                wagi
        );

        StrategiaPlanowania strategia = FabrykaStrategii.dla(rodzaj);

        for (int i = 0; i < liczbaWGrupie; i++) {
            Czas czasPrzybycia = pierwszaGodzina.plus(i * odstęp);
            Reporter reporter = śledzeni
                    ? new ReporterKonsolowy()
                    : ReporterNieaktywny.INSTANCJA;

            zapewnijMiejsce();

            sportowcy[liczbaSportowców] = new Sportowiec(
                    liczbaSportowców,
                    preferencje,
                    węzełStart,
                    czasPrzybycia,
                    reporter,
                    generator,
                    ośrodek,
                    strategia
            );

            liczbaSportowców++;
        }
    }

    private void zapewnijMiejsce() {
        if (liczbaSportowców == sportowcy.length) {
            sportowcy = Arrays.copyOf(sportowcy, 2 * sportowcy.length);
        }
    }

    public Sportowiec[] sportowcy() {
        assert sportowcy != null;
        return Arrays.copyOf(sportowcy, liczbaSportowców);
    }
}