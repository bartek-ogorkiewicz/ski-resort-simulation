package wejscie;

import java.util.Random;
import java.util.Scanner;

import kryteria.DopasowanieTrudnosci;
import kryteria.KryteriumAtrakcyjnosci;
import kryteria.WyrownanieNawierzchni;
import kryteria.Znudzenie;
import osrodek.Osrodek;
import osrodek.Trasa;
import osrodek.Wezel;
import osrodek.Wyciag;
import sportowcy.Sportowiec;

/**
 * Główny parser wejścia.
 * Wczytuje kolejne sekcje danych i buduje obiekt DaneSymulacji.
 */
public class ParserWejscia {
    private final Scanner scanner;
    private final Random generator;

    public ParserWejscia(Scanner scanner, Random generator) {
        assert scanner != null;
        assert generator != null;
        this.scanner = scanner;
        this.generator = generator;
    }

    public DaneSymulacji wczytaj() {
        ParserWezlow parserWezlow = new ParserWezlow(scanner);
        parserWezlow.parsuj();
        Wezel[] węzły = parserWezlow.węzły();

        ParserWyciagow parserWyciagow = new ParserWyciagow(scanner, węzły);
        parserWyciagow.parsuj();
        Wyciag[] wyciągi = parserWyciagow.wyciągi();

        ParserTras parserTras = new ParserTras(scanner, węzły);
        parserTras.parsuj();
        Trasa[] trasy = parserTras.trasy();

        Osrodek ośrodek = new Osrodek(węzły, trasy, wyciągi);

        // Kryteria oceny tras - wspólne dla wszystkich sportowców
        KryteriumAtrakcyjnosci[] kryteria = new KryteriumAtrakcyjnosci[3];
        kryteria[0] = new DopasowanieTrudnosci();
        kryteria[1] = new WyrownanieNawierzchni();
        kryteria[2] = new Znudzenie();

        ParserSportowcow parserSportowcow = new ParserSportowcow(
                scanner, węzły, generator, kryteria, ośrodek);
        parserSportowcow.parsuj();
        Sportowiec[] sportowcy = parserSportowcow.sportowcy();

        return new DaneSymulacji(ośrodek, sportowcy);
    }
}