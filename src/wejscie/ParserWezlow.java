package wejscie;

import java.util.Scanner;

import osrodek.Wezel;

/**
 * Parser sekcji węzłów.
 * Tworzy węzły o numerach wynikających z kolejności występowania na wejściu.
 */
public class ParserWezlow extends ParserSekcji {
    private Wezel[] węzły;

    public ParserWezlow(Scanner scanner) {
        super(scanner);
        this.węzły = null;
    }

    @Override
    protected void zarezerwuj(int liczba) {
        węzły = new Wezel[liczba];
    }

    @Override
    protected void parsujElement(int numer) {
        assert węzły != null;
        assert numer >= 0 && numer < węzły.length;

        Scanner linia = czytajLinię();

        int wysokość = linia.nextInt();
        int x = linia.nextInt();
        int y = linia.nextInt();

        boolean skomunikowany = false;

        if (linia.hasNext()) {
            String znacznik = linia.next();
            assert znacznik.equals("s");
            skomunikowany = true;
        }

        assert !linia.hasNext();

        węzły[numer] = new Wezel(numer, wysokość, x, y, skomunikowany);
    }

    public Wezel[] węzły() {
        assert węzły != null;
        return węzły;
    }
}