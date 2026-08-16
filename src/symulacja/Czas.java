package symulacja;

/**
 * Niemutowalna reprezentacja czasu wirtualnego symulacji.
 * Wewnętrznie przechowuje liczbę sekund od początku doby.
 */
public final class Czas implements Comparable<Czas> {
    public static final Czas POCZATEK_DNIA = new Czas(9 * 3600);
    public static final Czas KONIEC_PLANOWANIA = new Czas(15 * 3600);

    private final int sekundyOd0;

    public Czas(int sekundyOd0) {
        assert sekundyOd0 >= 0;
        this.sekundyOd0 = sekundyOd0;
    }

    public static Czas zNapisu(String hms) {
        assert hms != null;

        String[] części = hms.split(":");
        assert części.length == 3;

        int godziny = Integer.parseInt(części[0]);
        int minuty = Integer.parseInt(części[1]);
        int sekundy = Integer.parseInt(części[2]);

        assert godziny >= 0 && godziny < 24;
        assert minuty >= 0 && minuty < 60;
        assert sekundy >= 0 && sekundy < 60;

        return new Czas(godziny * 3600 + minuty * 60 + sekundy);
    }

    public int sekundy() {
        return sekundyOd0;
    }

    public Czas plus(int sekund) {
        assert sekund >= 0;
        return new Czas(sekundyOd0 + sekund);
    }

    public boolean przed(Czas inny) {
        assert inny != null;
        return this.sekundyOd0 < inny.sekundyOd0;
    }

    public boolean nieWcześniejNiż(Czas inny) {
        assert inny != null;
        return this.sekundyOd0 >= inny.sekundyOd0;
    }

    @Override
    public int compareTo(Czas inny) {
        assert inny != null;
        return Integer.compare(this.sekundyOd0, inny.sekundyOd0);
    }

    @Override
    public String toString() {
        int godziny = sekundyOd0 / 3600;
        int minuty = (sekundyOd0 / 60) % 60;
        int sekundy = sekundyOd0 % 60;
        return String.format("%02d:%02d:%02d", godziny, minuty, sekundy);
    }
}