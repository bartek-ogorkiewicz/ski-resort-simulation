package wejscie;

import java.util.Locale;
import java.util.Scanner;

/**
 * Bazowa klasa parserów sekcji wejścia.
 * Obsługuje wspólny schemat: liczba elementów, a następnie opisy elementów.
 */
public abstract class ParserSekcji {
    protected final Scanner scanner;

    protected ParserSekcji(Scanner scanner) {
        assert scanner != null;
        this.scanner = scanner;
    }

    public final void parsuj() {
        int liczba = czytajLiczbęCałkowitą();
        assert liczba >= 0;

        zarezerwuj(liczba);

        for (int i = 0; i < liczba; i++) {
            parsujElement(i);
        }
    }

    protected int czytajLiczbęCałkowitą() {
        String linia;
        do {
            linia = scanner.nextLine();
        } while (linia.trim().isEmpty());

        Scanner scannerLinii = new Scanner(linia);
        scannerLinii.useLocale(Locale.ENGLISH);

        assert scannerLinii.hasNextInt();

        int wynik = scannerLinii.nextInt();

        assert !scannerLinii.hasNext();

        return wynik;
    }

    protected Scanner czytajLinię() {
        String linia;

        do {
            linia = scanner.nextLine();
        } while (linia.trim().isEmpty());

        Scanner scannerLinii = new Scanner(linia);
        scannerLinii.useLocale(Locale.ENGLISH);

        return scannerLinii;
    }

    protected abstract void zarezerwuj(int liczba);

    protected abstract void parsujElement(int numer);
}