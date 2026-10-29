package kz.iitu.karibzhanova.studentapi.student;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student create(StudentRequest request) {
        Student student = new Student(request.firstName(), request.lastName(), request.email(),
                request.major(), request.course());
        return repository.save(student);
    }

    public Student update(Long id, StudentRequest request) {
        Student student = findById(id);
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());
        student.setMajor(request.major());
        student.setCourse(request.course());
        return repository.save(student);
    }

    public void delete(Long id) {
        Student student = findById(id);
        repository.delete(student);
    }
}
