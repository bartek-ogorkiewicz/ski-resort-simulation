package graf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import osrodek.Trasa;
import osrodek.Wezel;
import osrodek.Wyciag;

/**
 * Testy przeszukiwania grafu (BFS) na grafie ze schematu z treści zadania.
 * Graf zawiera 6 węzłów połączonych trasami (w dół) i wyciągami (w górę).
 * Wysokości węzłów dobrane tak, by trasy szły w dół, a wyciągi w górę.
 */
class PrzeszukiwanieGrafuTest {
    private Wezel w0;
    private Wezel w1;
    private Wezel w2;
    private Wezel w3;
    private Wezel w4;
    private Wezel w5;

    private PrzeszukiwanieGrafu przeszukiwanie;

    @BeforeEach
    void przygotujGraf() {
        // Wysokości dobrane tak, by wszystkie krawędzie były poprawne:
        // wyciągi w górę (początek niżej), trasy w dół (początek wyżej).
        w0 = new Wezel(0, 0, 0, 0, true);
        w1 = new Wezel(1, 20, 0, 0, false);
        w2 = new Wezel(2, 10, 0, 0, false);
        w3 = new Wezel(3, 40, 0, 0, false);
        w4 = new Wezel(4, 30, 0, 0, false);
        w5 = new Wezel(5, 50, 0, 0, false);

        // Wyciągi (w górę): 0->1, 2->3, 2->4, 4->5
        dodajWyciąg(0, w0, w1);
        dodajWyciąg(1, w2, w3);
        dodajWyciąg(2, w2, w4);
        dodajWyciąg(3, w4, w5);

        // Trasy (w dół): 5->3 (dwie równoległe), 3->4, 3->1, 1->2, 2->0, 1->0
        dodajTrasę(0, w5, w3);
        dodajTrasę(1, w5, w3);
        dodajTrasę(2, w3, w4);
        dodajTrasę(3, w3, w1);
        dodajTrasę(4, w1, w2);
        dodajTrasę(5, w2, w0);
        dodajTrasę(6, w1, w0);

        przeszukiwanie = new PrzeszukiwanieGrafu();
    }

    private void dodajWyciąg(int numer, Wezel początek, Wezel koniec) {
        Wyciag wyciąg = new Wyciag(numer, początek, koniec, 10, 4, 60);
        początek.dodajWychodzącyWyciąg(wyciąg);
    }

    private void dodajTrasę(int numer, Wezel początek, Wezel koniec) {
        Trasa trasa = new Trasa(numer, początek, koniec, 5, 60, 0.3, 0.99);
        początek.dodajWychodzącąTrasę(trasa);
    }

    @Test
    void ścieżkaZ0Do4MaDługość3() {
        Plan plan = przeszukiwanie.znajdźPlan(w0, w4);

        assertEquals(3, plan.długość());

        // Ścieżka prowadzi przez 0 -> 1 -> 2 -> 4
        assertEquals(w0, plan.krawędź(0).początek());
        assertEquals(w1, plan.krawędź(0).koniec());
        assertEquals(w1, plan.krawędź(1).początek());
        assertEquals(w2, plan.krawędź(1).koniec());
        assertEquals(w2, plan.krawędź(2).początek());
        assertEquals(w4, plan.krawędź(2).koniec());
    }

    @Test
    void ścieżkaZ3Do1JestBezpośrednia() {
        Plan plan = przeszukiwanie.znajdźPlan(w3, w1);

        assertEquals(1, plan.długość());
        assertEquals(w3, plan.krawędź(0).początek());
        assertEquals(w1, plan.krawędź(0).koniec());
    }

    @Test
    void ścieżkaZ2DoSiebieMaDługość0() {
        Plan plan = przeszukiwanie.znajdźPlan(w2, w2);

        assertEquals(0, plan.długość());
        assertTrue(plan.czyPusty());
    }

    @Test
    void ścieżkaZ4Do3MaDługość2() {
        Plan plan = przeszukiwanie.znajdźPlan(w4, w3);

        assertEquals(2, plan.długość());

        // Zaczyna w 4, kończy w 3, ścieżka jest ciągła
        assertEquals(w4, plan.krawędź(0).początek());
        assertEquals(w3, plan.krawędź(1).koniec());
        assertEquals(plan.krawędź(0).koniec(), plan.krawędź(1).początek());
    }
}