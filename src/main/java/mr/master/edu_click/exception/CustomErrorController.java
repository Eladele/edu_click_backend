package mr.master.edu_click.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        // Récupérer le code d'erreur
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");

        if (statusCode != null && statusCode == HttpStatus.NOT_FOUND.value()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endpoint non trouvé");
        }

        // Gérer d'autres erreurs (500, etc.)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite");
    }
}