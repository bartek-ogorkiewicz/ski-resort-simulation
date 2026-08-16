package graf;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import osrodek.Krawedz;
import osrodek.Wezel;

/**
 * Wyznacza najkrótsze ścieżki w grafie ośrodka algorytmem BFS.
 * Odległość mierzona jest liczbą krawędzi (tras i wyciągów łącznie).
 * Trasy i wyciągi traktowane są jednakowo jako skierowane krawędzie.
 */
public class PrzeszukiwanieGrafu {
    /**
     * Znajduje najkrótszą ścieżkę od węzła startowego do docelowego.
     * Jeśli start i cel są tym samym węzłem, zwraca pusty plan (odległość 0).
     * Zakładamy, że graf jest silnie spójny, więc ścieżka zawsze istnieje.
     */
    public Plan znajdźPlan(Wezel start, Wezel cel) {
        assert start != null;
        assert cel != null;

        if (start == cel) {
            return new Plan(new ArrayList<>());
        }

        // Dla każdego odwiedzonego węzła pamiętamy krawędź, którą go znaleziono.
        Map<Wezel, Krawedz> skądKrawędź = new HashMap<>();
        Queue<Wezel> kolejka = new ArrayDeque<>();

        kolejka.add(start);
        skądKrawędź.put(start, null);  // start nie ma krawędzi wejściowej

        while (!kolejka.isEmpty()) {
            Wezel obecny = kolejka.poll();
            for (Krawedz krawędź : krawędzieWychodzące(obecny)) {
                Wezel sąsiad = krawędź.koniec();

                if (!skądKrawędź.containsKey(sąsiad)) {
                    skądKrawędź.put(sąsiad, krawędź);

                    if (sąsiad == cel) {
                        return odtwórzPlan(skądKrawędź, start, cel);
                    }

                    kolejka.add(sąsiad);
                }
            }
        }
        // Graf silnie spójny - nie powinniśmy tu dotrzeć.
        assert false : "Brak ścieżki w silnie spójnym grafie";
        return new Plan(new ArrayList<>());
    }

    /**
     * Zwraca listę wszystkich krawędzi wychodzących z węzła
     * (najpierw trasy, potem wyciągi).
     */
    private List<Krawedz> krawędzieWychodzące(Wezel węzeł) {
        assert węzeł != null;

        List<Krawedz> wynik = new ArrayList<>();

        for (int i = 0; i < węzeł.liczbaWychodzącychTras(); i++) {
            wynik.add(węzeł.wychodzącaTrasa(i));
        }
        for (int i = 0; i < węzeł.liczbaWychodzącychWyciągów(); i++) {
            wynik.add(węzeł.wychodzącyWyciąg(i));
        }

        return wynik;
    }

    /**
     * Odtwarza ścieżkę od celu do startu, cofając się po zapamiętanych
     * krawędziach, a następnie odwraca ją do kolejności start -> cel.
     */
    private Plan odtwórzPlan(Map<Wezel, Krawedz> skądKrawędź, Wezel start, Wezel cel) {
        assert skądKrawędź != null;
        assert start != null;
        assert cel != null;

        List<Krawedz> ścieżka = new ArrayList<>();
        Wezel obecny = cel;

        while (obecny != start) {
            Krawedz krawędź = skądKrawędź.get(obecny);
            assert krawędź != null;
            ścieżka.add(krawędź);
            obecny = krawędź.początek();
        }

        // ścieżka: od celu do startu - odwracamy na start -> cel
        Collections.reverse(ścieżka);
        return new Plan(ścieżka);
    }
}