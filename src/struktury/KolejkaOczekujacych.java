package struktury;

import sportowcy.Sportowiec;

/**
 * Kolejka sportowców oczekujących na wjazd wyciągiem.
 * Zaimplementowana jako bufor cykliczny powiększany przez podwojenie.
 * Śledzi maksymalną osiągniętą długość na potrzeby statystyk.
 */
public class KolejkaOczekujacych {

    private Sportowiec[] elementy;
    private int początek;
    private int koniec;
    private int długość;
    private int maksymalnaDługość;

    public KolejkaOczekujacych() {
        this.elementy = new Sportowiec[4];
        this.początek = 0;
        this.koniec = 0;
        this.długość = 0;
        this.maksymalnaDługość = 0;
    }

    public void dodaj(Sportowiec sportowiec) {
        assert sportowiec != null;
        if (długość == elementy.length) {
            powiększTablicę();
        }
        elementy[koniec] = sportowiec;
        koniec = (koniec + 1) % elementy.length;
        długość++;

        if (długość > maksymalnaDługość) {
            maksymalnaDługość = długość;
        }
    }

    public Sportowiec pobierzPierwszego() {
        assert !czyPusta();
        Sportowiec wynik = elementy[początek];
        elementy[początek] = null;
        początek = (początek + 1) % elementy.length;
        długość--;
        return wynik;
    }

    public boolean czyPusta() {
        return długość == 0;
    }

    public int długość() {
        return długość;
    }

    public int maksymalnaDługość() {
        return maksymalnaDługość;
    }

    private void powiększTablicę() {
        Sportowiec[] nowaTablica = new Sportowiec[elementy.length * 2];
        for (int i = 0; i < długość; i++) {
            nowaTablica[i] = elementy[(początek + i) % elementy.length];
        }
        elementy = nowaTablica;
        początek = 0;
        koniec = długość;
    }
}