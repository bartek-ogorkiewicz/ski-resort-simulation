package sportowcy;

/**
 * Rodzaj sportowca - określa strategię wyboru tras.
 * - LOKALNY: wybiera trasę spośród sąsiednich (jak w części 1)
 * - ZACHŁANNY: wybiera globalnie najatrakcyjniejszą trasę w ośrodku
 * - KOLEKCJONER: stara się odwiedzić jak najwięcej różnych tras
 */
public enum RodzajSportowca {
    LOKALNY,
    ZACHŁANNY,
    KOLEKCJONER;

    public static RodzajSportowca zLitery(String litera) {
        assert litera != null;
        return switch (litera) {
            case "L" -> LOKALNY;
            case "Z" -> ZACHŁANNY;
            case "K" -> KOLEKCJONER;
            default -> throw new IllegalArgumentException(
                    "Nieznany rodzaj sportowca: " + litera);
        };
    }
}