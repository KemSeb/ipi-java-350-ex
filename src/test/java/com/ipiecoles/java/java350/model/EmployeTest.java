package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

public class EmployeTest {

    @Test
    public void testGetNbAnneesAncienneteDateEmbaucheNow() {
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now());
        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();


        //Then nbAnnesAnciennete = 0
        Assertions.assertThat(nbAnneesAnciennete).isZero();
    }

    @Test
    public void testGetNbAnneesAncienneteDateEmbauchePassee(){
        //Given
        //Date d'embauche 10 ans dans le passé
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().minusYears(10));
        //employe.setDateEmbauche(LocalDate.of(2012, 4, 26)); //Pas bon...
        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        // => 10
        Assertions.assertThat(nbAnneesAnciennete).isEqualTo(10);
    }

    @Test
    public void testGetNbAnneesAncienneteDateEmbaucheFutur(){
        //Given
        //Date d'embauche 2 ans dans le futur
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().plusYears(2));
        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        // => 0
        Assertions.assertThat(nbAnneesAnciennete).isZero();
    }


    @Test
    public void testGetNbAnneesAncienneteDateEmbaucheNull(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);
        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        // => 0
        Assertions.assertThat(nbAnneesAnciennete).isZero();
    }


    @ParameterizedTest
    @CsvSource({
            "'T12345',0,2,1.0,2300.0",
            "'M12345',0,1,0.5,850.0",
            "'M123456',0,1,1.0,1700.0",
            "'T123458',0,1,1.0,1000.0",
            ",0,1,1.0,1000.0",
            "'M12345',2,1,1.0,1900.0",
            "'T12346',5,1,1.0,1500.0",
            "'T123459',0,,1.0,1000.0"
    })
    public void testGetPrimeAnnuelleManagerPerformanceBasePleinTemps(
            String matricule,
            Integer nbAnneesAnciennete,
            Integer performance,
            Double tauxActivite,
            Double prime
    ){
        //Given
        Employe employe = new Employe("Doe", "John", matricule,
                LocalDate.now().minusYears(nbAnneesAnciennete), 2500d, performance, tauxActivite);
        //When
        Double primeObtenue = employe.getPrimeAnnuelle();
        //Then
        Assertions.assertThat(primeObtenue).isEqualTo(prime);
    }

    // test Augmentation du salaire avec un pourcentage positif
    @Test
    public void testAugmenterSalaireqQuiMonte(){
        //Given
        Employe employe = new Employe();
        employe.setSalaire(1721.22);

        //When
        Double Salaire = employe.augmenterSalaire(10.0);

        //Then        // => 0
        Assertions.assertThat(Salaire).isEqualTo(1893.342);

    }
    // test Augmentation du salaire avec un pourcentage négatif

    @Test
    public void testAugmenterSalaireQuiBaisse(){
        //Given
        Employe employe = new Employe();
        employe.setSalaire(1800.00);

        //When
        Double Salaire = employe.augmenterSalaire(-5.0);

        //Then        // => 0
        Assertions.assertThat(Salaire).isEqualTo(1800.00);

    }
    // test Augmentation du salaire avec un pourcentage égale à 0.

    @Test
    public void testAugmenterSalaireEgal(){
        //Given
        Employe employe = new Employe();
        employe.setSalaire(1800.00);

        //When
        Double Salaire = employe.augmenterSalaire(0.0);

        //Then        // => 0
        Assertions.assertThat(Salaire).isEqualTo(1800.00);
    }

    @ParameterizedTest
    @CsvSource({
            "2020,10",
            "2021,10",
            "2022,10",
            "2023,9",
            "2024,9",
            "2040,11"
    })

    public void testGetNbRtt(
            int annee,
            Integer rtt
    ){
        //Given
        Employe employe = new Employe("Doe", "John", "C00001",
                LocalDate.now().minusYears(10), 2200d,1, 1.0);
        //When
        Integer rttAcquis = employe.getNbRtt(LocalDate.of(annee, 1, 1));
        //Then
        Assertions.assertThat(rttAcquis).isEqualTo(rtt);
    }


    }
