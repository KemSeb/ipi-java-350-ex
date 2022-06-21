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
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {

        @InjectMocks
        private EmployeService employeService;
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
        //Given
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(null);
        Mockito.when(employeRepository.save(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());
        //When
        employeService.embaucheEmploye("Doe", "John", Poste.COMMERCIAL, NiveauEtude.MASTER, 1.0);
        //Then
//        Employe employe = employeRepository.findByMatricule("C00001");
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe.getNom()).isEqualTo("Doe");
        Assertions.assertThat(employe.getPrenom()).isEqualTo("John");
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(2129.71);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1);
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

}
