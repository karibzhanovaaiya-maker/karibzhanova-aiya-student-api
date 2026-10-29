package kz.iitu.karibzhanova.studentapi.common;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final String studentName;
    private final String version;

    public HomeController(@Value("${app.student.name}") String studentName,
                          @Value("${app.version}") String version) {
        this.studentName = studentName;
        this.version = version;
    }

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("application", "Student API");
        info.put("author", studentName);
        info.put("version", version);
        info.put("students", "/api/students");
        return info;
    }
}
