package wejscie;

import java.util.Scanner;

import osrodek.Trasa;
import osrodek.Wezel;
import struktury.Asercje;

/**
 * Parser sekcji tras. Każda trasa to jedna linia z parametrami.
 */
public class ParserTras extends ParserSekcji {
    private final Wezel[] węzły;
    private Trasa[] trasy;

    public ParserTras(Scanner scanner, Wezel[] węzły) {
        super(scanner);

        assert Asercje.wszystkieNiepuste(węzły);

        this.węzły = węzły;
        this.trasy = null;
    }

    @Override
    protected void zarezerwuj(int liczba) {
        trasy = new Trasa[liczba];
    }

    @Override
    protected void parsujElement(int numer) {
        assert trasy != null;
        assert numer >= 0 && numer < trasy.length;

        Scanner linia = czytajLinię();

        int nrPoczątku = linia.nextInt();
        int nrKońca = linia.nextInt();
        int trudność = linia.nextInt();
        int czasPrzejazdu = linia.nextInt();
        double bazowaAtrakcyjność = linia.nextDouble();
        double odporność = linia.nextDouble();

        assert !linia.hasNext();

        assert nrPoczątku >= 0 && nrPoczątku < węzły.length;
        assert nrKońca >= 0 && nrKońca < węzły.length;

        Wezel początek = węzły[nrPoczątku];
        Wezel koniec = węzły[nrKońca];

        trasy[numer] = new Trasa(
                numer,
                początek,
                koniec,
                trudność,
                czasPrzejazdu,
                bazowaAtrakcyjność,
                odporność
        );
    }

    public Trasa[] trasy() {
        assert trasy != null;
        return trasy;
    }
}