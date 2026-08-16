package wejscie;

import java.util.Scanner;

import osrodek.Wezel;
import osrodek.Wyciag;
import struktury.Asercje;

/**
 * Parser sekcji wyciągów.
 * Tworzy wyciągi na podstawie numerów węzłów i parametrów z wejścia.
 */
public class ParserWyciagow extends ParserSekcji {
    private final Wezel[] węzły;
    private Wyciag[] wyciągi;

    public ParserWyciagow(Scanner scanner, Wezel[] węzły) {
        super(scanner);

        assert Asercje.wszystkieNiepuste(węzły);

        this.węzły = węzły;
        this.wyciągi = null;
    }

    @Override
    protected void zarezerwuj(int liczba) {
        wyciągi = new Wyciag[liczba];
    }

    @Override
    protected void parsujElement(int numer) {
        assert wyciągi != null;
        assert numer >= 0 && numer < wyciągi.length;

        Scanner linia = czytajLinię();

        int nrPoczątku = linia.nextInt();
        int nrKońca = linia.nextInt();
        int odstęp = linia.nextInt();
        int maksGrupa = linia.nextInt();
        int czasPrzejazdu = linia.nextInt();

        assert !linia.hasNext();

        assert nrPoczątku >= 0 && nrPoczątku < węzły.length;
        assert nrKońca >= 0 && nrKońca < węzły.length;

        Wezel początek = węzły[nrPoczątku];
        Wezel koniec = węzły[nrKońca];

        Wyciag wyciąg = new Wyciag(numer, początek, koniec, odstęp, maksGrupa, czasPrzejazdu);
        wyciągi[numer] = wyciąg;
    }

    public Wyciag[] wyciągi() {
        assert wyciągi != null;
        return wyciągi;
    }
}