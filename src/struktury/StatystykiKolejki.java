package struktury;

import symulacja.Czas;

/**
 * Liczy średnią długość kolejki w czasie.
 * Sumuje iloczyny (długość kolejki * czas, przez jaki kolejka miała tę
 * długość). Średnia to ta suma podzielona przez długość rozważanego
 * przedziału czasu.
 */
public class StatystykiKolejki {
    private final Czas początek;
    private int ostatniaDługość;
    private Czas ostatniaZmiana;
    private long suma;

    public StatystykiKolejki(Czas początek) {
        assert początek != null;
        this.początek = początek;
        this.ostatniaDługość = 0;
        this.ostatniaZmiana = początek;
        this.suma = 0;
    }

    /**
     * Rejestruje, że w danej chwili długość kolejki zmieniła się na nową.
     * Domyka poprzedni przedział (dolicza jego wkład do sumy) i zapamiętuje
     * nową długość obowiązującą od tej chwili.
     */
    public void zarejestrujZmianę(int nowaDługość, Czas teraz) {
        assert nowaDługość >= 0;
        assert teraz != null;
        assert teraz.nieWcześniejNiż(ostatniaZmiana);

        suma += (long) ostatniaDługość * (teraz.sekundy() - ostatniaZmiana.sekundy());
        ostatniaDługość = nowaDługość;
        ostatniaZmiana = teraz;
    }

    /**
     * Średnia długość kolejki w przedziale od początku do podanego końca.
     * Domyka ostatni przedział (od ostatniej zmiany do końca) w locie,
     * nie zmieniając stanu obiektu.
     */
    public double średniaDługość(Czas koniec) {
        assert koniec != null;
        assert koniec.nieWcześniejNiż(ostatniaZmiana);

        long pełnaSuma = suma
                + (long) ostatniaDługość * (koniec.sekundy() - ostatniaZmiana.sekundy());
        int długośćPrzedziału = koniec.sekundy() - początek.sekundy();

        assert długośćPrzedziału > 0;
        return (double) pełnaSuma / długośćPrzedziału;
    }
}