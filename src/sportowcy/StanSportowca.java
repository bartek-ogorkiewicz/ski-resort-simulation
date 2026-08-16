package sportowcy;

/**
 * Opisuje aktualny etap dnia sportowca w symulacji.
 */
public enum StanSportowca {
    PRZED_PRZYJAZDEM,   // czeka na swój czas startu
    W_WEZLE,            // jest w węźle i może podjąć decyzję
    NA_TRASIE,          // zjeżdża
    W_KOLEJCE,          // czeka u dolnej stacji wyciągu
    NA_WYCIAGU,         // jedzie w górę
    PO_DNIU             // nie podejmuje już nowych aktywności
}