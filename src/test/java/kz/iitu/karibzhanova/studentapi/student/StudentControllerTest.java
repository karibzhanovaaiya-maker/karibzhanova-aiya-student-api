package kz.iitu.karibzhanova.studentapi.student;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    private static final String AIYA_JSON = """
            {"firstName":"Aiya","lastName":"Karibzhanova",
             "email":"aiya.karibzhanova@iitu.edu.kz","major":"Software Engineering","course":3}
            """;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudentRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void homeShowsStudentName() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Karibzhanova Aiya"));
    }

    @Test
    void createAndReadStudent() throws Exception {
        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(AIYA_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lastName").value("Karibzhanova"));

        mvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateStudent() throws Exception {
        Student saved = repository.save(new Student("Aiya", "Karibzhanova", "old@iitu.edu.kz", "SE", 2));

        mvc.perform(put("/api/students/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(AIYA_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course").value(3));
    }

    @Test
    void deleteStudent() throws Exception {
        Student saved = repository.save(new Student("Aiya", "Karibzhanova", "a@iitu.edu.kz", "SE", 3));

        mvc.perform(delete("/api/students/" + saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void missingStudentReturns404() throws Exception {
        mvc.perform(get("/api/students/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student with id 999 not found"));
    }

    @Test
    void duplicateEmailReturns409() throws Exception {
        repository.save(new Student("Aiya", "Karibzhanova", "aiya.karibzhanova@iitu.edu.kz", "SE", 3));

        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(AIYA_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void invalidStudentReturns400() throws Exception {
        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"\",\"lastName\":\"K\",\"email\":\"not-an-email\"}"))
                .andExpect(status().isBadRequest());
    }
}
