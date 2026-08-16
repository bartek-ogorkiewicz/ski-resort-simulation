package sportowcy.strategie;

import sportowcy.RodzajSportowca;

/**
 * Dostarcza strategię planowania odpowiednią dla danego rodzaju sportowca.
 * Strategie są bezstanowe, więc współdzielone jako pojedyncze instancje.
 */
public final class FabrykaStrategii {
    private static final StrategiaPlanowania LOKALNA = new StrategiaLokalna();
    private static final StrategiaPlanowania ZACHŁANNA = new StrategiaZachlanna();
    private static final StrategiaPlanowania KOLEKCJONERSKA = new StrategiaKolekcjonerska();

    private FabrykaStrategii() {
    }

    public static StrategiaPlanowania dla(RodzajSportowca rodzaj) {
        assert rodzaj != null;

        return switch (rodzaj) {
            case LOKALNY -> LOKALNA;
            case ZACHŁANNY -> ZACHŁANNA;
            case KOLEKCJONER -> KOLEKCJONERSKA;
        };
    }
}