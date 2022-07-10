package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class EmployeServiceTest {
    @Autowired
    EmployeService employeService;

    @Autowired
    private EmployeRepository employeRepository;

    @BeforeEach
    @AfterEach
    public void purge(){
        employeRepository.deleteAll();
    }

    @Test
    public void testEmbaucheEmploye() throws EmployeException {
        //Given


        //When

         employeService.embaucheEmploye("Doe", "John", Poste.COMMERCIAL, NiveauEtude.MASTER, 1.0);

        //Then
        Employe employe = employeRepository.findByMatricule("C00001");
        Assertions.assertThat(employe.getNom()).isEqualTo("Doe");
        Assertions.assertThat(employe.getPrenom()).isEqualTo("John");
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(2129.71);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1);

    }

    @Test
    public void testCalculPerformanceCommerciale() throws EmployeException {
        //Given
        //When
        employeService.embaucheEmploye("Doe","John", Poste.COMMERCIAL, NiveauEtude.MASTER,1.0);
        employeService.calculPerformanceCommercial("C00001",50000L,25000L);
        //then
        Employe employe = employeRepository.findByMatricule("C00001");
        Assertions.assertThat(employe.getPerformance()).isEqualTo(6);

    }

    @Test
    public void testavgPerformanceWhereMatriculeStartsWith() throws EmployeException {
        //Given
        //When
        employeService.embaucheEmploye("Doe","jhon", Poste.COMMERCIAL, NiveauEtude.MASTER,1.0);
        //then
        Assertions.assertThat(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).isEqualTo(1);

    }
    @Test
    void testCalculPerformanceCommercialeAvecUnObjectifCaNull(){
        String matricule = "C00001";
        Long caTraite = 10L;
        Long objectifCa = null;

        //given
        //when
        Throwable thrown = Assertions.catchThrowable(() -> employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa));
        //then
        Assertions.assertThat(thrown).isInstanceOf(EmployeException.class)
                .hasMessageContaining("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
    }

    @Test
    void testCalculPerformanceCommercialeAvecUnObjectifCaNegatif(){
        String matricule = "C00001";
        Long caTraite = 5L;
        Long objectifCa = -1L;

        //given
        //when
        Throwable thrown = Assertions.catchThrowable(() -> employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa));
        //then
        Assertions.assertThat(thrown).isInstanceOf(EmployeException.class)
                .hasMessageContaining("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
    }
    @Test
    void testCalculPerformanceCommercialeAvecUnMatriculeDifferentDeCommercial(){
        String matricule = "M00001";
        Long caTraite = 1200L;
        Long objectifCa = 1000L;

        //given
        //when
        Throwable thrown = Assertions.catchThrowable(() -> employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa));
        //then
        Assertions.assertThat(thrown).isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le matricule ne peut être null et doit commencer par un C !");
    }


    
}








