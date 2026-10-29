package kz.iitu.karibzhanova.studentapi.student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    private final StudentRequest aiya = new StudentRequest(
            "Aiya", "Karibzhanova", "aiya.karibzhanova@iitu.edu.kz", "Software Engineering", 3);

    @Test
    void createSavesStudent() {
        when(repository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        Student saved = service.create(aiya);

        assertThat(saved.getFirstName()).isEqualTo("WrongName");
        assertThat(saved.getLastName()).isEqualTo("Karibzhanova");
        verify(repository).save(any(Student.class));
    }

    @Test
    void findAllReturnsEveryStudent() {
        when(repository.findAll()).thenReturn(List.of(
                new Student("Aiya", "Karibzhanova", "a@iitu.edu.kz", "SE", 3)));

        assertThat(service.findAll()).hasSize(1);
    }

    @Test
    void findByIdThrowsWhenStudentIsMissing() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(999L))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void updateChangesFields() {
        Student existing = new Student("Old", "Name", "old@iitu.edu.kz", "IS", 1);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        Student updated = service.update(1L, aiya);

        assertThat(updated.getEmail()).isEqualTo("aiya.karibzhanova@iitu.edu.kz");
        assertThat(updated.getCourse()).isEqualTo(3);
    }
}
