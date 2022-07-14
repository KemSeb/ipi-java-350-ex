package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import javax.persistence.EntityExistsException;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {
    @InjectMocks
    EmployeService employeService;

    @Mock
    private EmployeRepository employeRepository;

    @Test
    public void testEmbaucheEmployeLimiteMatricule(){
        //Given
        when(employeRepository.findLastMatricule()).thenReturn("99999");
        //When
        Throwable t = Assertions.catchThrowable(() -> {
            employeService.embaucheEmploye("Doe", "John", Poste.COMMERCIAL, NiveauEtude.MASTER, 1.0);
        });
        //Then
        Assertions.assertThat(t).isInstanceOf(EmployeException.class)
                .hasMessage("Limite des 100000 matricules atteinte !");
    }

    @Test
    public void testEmbaucheEmployeEmployeExist(){
        //Given
        when(employeRepository.findLastMatricule()).thenReturn(null);
        when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe());
        //When
        Throwable t = Assertions.catchThrowable(() -> {
            employeService.embaucheEmploye("Doe", "John", Poste.COMMERCIAL, NiveauEtude.MASTER, 1.0);
        });
        //Then
        Assertions.assertThat(t).isInstanceOf(EntityExistsException.class)
                .hasMessage("L'employé de matricule C00001 existe déjà en BDD");
    }

    @Test
    public void testEmbaucheEmploye() throws EmployeException {

        //given
        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(null);
        Mockito.when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        //when
        employeService.embaucheEmploye("Taratata","Balou",Poste.COMMERCIAL,NiveauEtude.LICENCE,1.0);
        //then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());


        Assertions.assertThat(employeArgumentCaptor.getValue().getMatricule()).isEqualTo("C00001");
        Assertions.assertThat(employeArgumentCaptor.getValue().getNom()).isEqualTo("Taratata");
        Assertions.assertThat(employeArgumentCaptor.getValue().getPrenom()).isEqualTo("Balou");
        Assertions.assertThat(employeArgumentCaptor.getValue().getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employeArgumentCaptor.getValue().getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employeArgumentCaptor.getValue().getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employeArgumentCaptor.getValue().getTempsPartiel()).isEqualTo(1.0);
    }


    @Test
    public void testCalculPerformanceCommercial() throws EmployeException {
        //given
        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe());
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(1.5);
        Mockito.when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        employeService.calculPerformanceCommercial("C00001", 9876543210L, 9125482314L);

        //then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeCaptor.capture());

        Assertions.assertThat(employeCaptor.getValue().getPerformance()).isEqualTo(3);
    }

    @Test
    public void testavgPerformanceWhereMatriculeStartsWith() throws EmployeException {
        //Given
        //When
        employeService.embaucheEmploye("Doe","john", Poste.COMMERCIAL, NiveauEtude.MASTER,1.0);
        //then
        Assertions.assertThat(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).isEqualTo(0);

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

//@Test
//public void testEmbaucheEmployeWithExistingEmployes() throws EmployeException {
//    //Given
//    Mockito.when(employeRepository.findLastMatricule()).thenReturn("12345");
//    Mockito.when(employeRepository.findByMatricule("C12346")).thenReturn(null);
//    Mockito.when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer((AdditionalAnswers.returnsFirstArg()));


//    //When
//    employeService.embaucheEmploye("Doe", "John", Poste.COMMERCIAL, NiveauEtude.MASTER, 0.5);
//    //Then
//    //Employe employe = employeRepository.findByMatricule("C00001");
//    ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
//    Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
//    Employe employe = employeArgumentCaptor.getValue();
//    Assertions.assertThat(employe.getNom()).isEqualTo("Doe");
//    Assertions.assertThat(employe.getPrenom()).isEqualTo("John");
//    Assertions.assertThat(employe.getMatricule()).isEqualTo("C12346");
//    Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
//    Assertions.assertThat(employe.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
//    Assertions.assertThat(employe.getSalaire()).isEqualTo(1064.85);
//    Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(0.5);
//}

