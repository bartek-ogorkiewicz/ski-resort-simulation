package uruchomienie;

import java.util.Random;
import java.util.Scanner;

import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import mapki.RysownikMapek;
import symulacja.Symulacja;
import wejscie.DaneSymulacji;
import wejscie.ParserWejscia;

/**
 * Punkt wejścia programu.
 * Wczytuje dane, uruchamia symulację, wypisuje statystyki końcowe
 * i generuje mapki do katalogu podanego jako argument programu.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println(
                    "Brak ścieżki katalogu na mapki. "
                            + "Uruchom program z argumentem: ścieżka do katalogu na mapki.");
            return;
        }

        String ścieżkaMapek = args[0];

        try {
            Scanner scanner = new Scanner(System.in);
            Random generator = new Random();

            ParserWejscia parser = new ParserWejscia(scanner, generator);
            DaneSymulacji dane = parser.wczytaj();

            Symulacja symulacja = new Symulacja(dane.ośrodek(), dane.sportowcy());
            symulacja.uruchom();
            symulacja.wypiszStatystyki();

            GeneratorMapek generatorMapek = new GeneratorMapek(ścieżkaMapek);
            RysownikMapek rysownik = new RysownikMapek(generatorMapek, dane.ośrodek());
            rysownik.narysujWszystkie(dane.sportowcy());
        } catch (WyjatekSystemuPlikow e) {
            System.err.println(
                    "Problem z zapisem mapek. Upewnij się, że podana ścieżka jest "
                            + "poprawna i że masz uprawnienia do zapisu w tej lokalizacji.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println(
                    "Wystąpił nieoczekiwany błąd. Prosimy zgłosić go zespołowi "
                            + "deweloperskiemu.");
            e.printStackTrace();
        }
    }
}