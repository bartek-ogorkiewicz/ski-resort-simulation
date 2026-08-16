package osrodek;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kryteria.DopasowanieTrudnosci;
import kryteria.KryteriumAtrakcyjnosci;
import kryteria.WyrownanieNawierzchni;
import kryteria.Znudzenie;
import raportowanie.ReporterNieaktywny;
import sportowcy.PreferencjeSportowca;
import sportowcy.RodzajSportowca;
import sportowcy.Sportowiec;
import sportowcy.strategie.FabrykaStrategii;
import symulacja.Czas;

/**
 * Testy pracy wyciągu: zabieranie grup pasażerów zgodnie z pojemnością
 * oraz śledzenie maksymalnej długości kolejki.
 */
class WyciagTest {
    private Wezel dół;
    private Wezel góra;
    private Osrodek ośrodek;
    private int licznikSportowców;

    @BeforeEach
    void przygotuj() {
        // Dwa węzły: dolna i górna stacja wyciągu (wyciąg jedzie w górę).
        dół = new Wezel(0, 100, 0, 0, true);
        góra = new Wezel(1, 200, 0, 0, false);

        // Minimalny ośrodek (potrzebny do konstruktora sportowca).
        Wyciag wyciągPomocniczy = new Wyciag(0, dół, góra, 10, 3, 60);
        ośrodek = new Osrodek(
                new Wezel[]{dół, góra},
                new Trasa[]{},
                new Wyciag[]{wyciągPomocniczy}
        );

        licznikSportowców = 0;
    }

    /**
     * Tworzy sportowca o kolejnym numerze. Do testów wyciągu istotne jest
     * tylko, że jest niepusty i można dodać go do kolejki.
     */
    private Sportowiec nowySportowiec() {
        KryteriumAtrakcyjnosci[] kryteria = {
                new DopasowanieTrudnosci(),
                new WyrownanieNawierzchni(),
                new Znudzenie()
        };
        double[] wagi = {0.5, 0.3, 0.2};

        PreferencjeSportowca preferencje = new PreferencjeSportowca(
                5, 0.0, 0.5, RodzajSportowca.LOKALNY, kryteria, wagi
        );

        Sportowiec sportowiec = new Sportowiec(
                licznikSportowców,
                preferencje,
                dół,
                Czas.zNapisu("09:00:00"),
                ReporterNieaktywny.INSTANCJA,
                new Random(),
                ośrodek,
                FabrykaStrategii.dla(RodzajSportowca.LOKALNY)
        );
        licznikSportowców++;
        return sportowiec;
    }

    @Test
    void wyciągZabieraTylkoTyluIluSięMieści() {
        // Pojemność 3, w kolejce 4 - jeden się nie mieści.
        Wyciag wyciąg = new Wyciag(0, dół, góra, 10, 3, 60);
        for (int i = 0; i < 4; i++) {
            wyciąg.dodajDoKolejki(nowySportowiec(), Czas.POCZATEK_DNIA);
        }

        wyciąg.zabierzGrupę(Czas.POCZATEK_DNIA);

        assertEquals(3, wyciąg.liczbaPrzejazdów());
    }

    @Test
    void wyciągZabieraWszystkichGdyMieściSięCałaKolejka() {
        // Pojemność 3, w kolejce 2 - obaj odjeżdżają.
        Wyciag wyciąg = new Wyciag(0, dół, góra, 10, 3, 60);
        for (int i = 0; i < 2; i++) {
            wyciąg.dodajDoKolejki(nowySportowiec(), Czas.POCZATEK_DNIA);
        }

        wyciąg.zabierzGrupę(Czas.POCZATEK_DNIA);

        assertEquals(2, wyciąg.liczbaPrzejazdów());
    }

    @Test
    void maksymalnaDługośćKolejkiJestPoprawna() {
        // 4 w kolejce, odjazd grupy (3), potem dodanie 1.
        // Maksimum osiągnięte przed odjazdem wynosi 4.
        Wyciag wyciąg = new Wyciag(0, dół, góra, 10, 3, 60);
        for (int i = 0; i < 4; i++) {
            wyciąg.dodajDoKolejki(nowySportowiec(), Czas.POCZATEK_DNIA);
        }

        wyciąg.zabierzGrupę(Czas.POCZATEK_DNIA);
        wyciąg.dodajDoKolejki(nowySportowiec(), Czas.POCZATEK_DNIA);

        assertEquals(4, wyciąg.maksymalnaDługośćKolejki());
    }
}