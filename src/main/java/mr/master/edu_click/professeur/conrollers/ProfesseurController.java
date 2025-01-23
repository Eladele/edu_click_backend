package mr.master.edu_click.professeur.conrollers;



import mr.master.edu_click.dao.entities.ProfesseurEntity;
import mr.master.edu_click.professeur.services.ProfesseurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professeurs")
public class ProfesseurController {

    private final ProfesseurService professeurService;

    public ProfesseurController(ProfesseurService professeurService) {
        this.professeurService = professeurService;
    }

    @GetMapping
    public List<ProfesseurEntity> getAllProfesseurs() {
        return professeurService.findAll();
    }

    @PostMapping
    public ResponseEntity<ProfesseurEntity> createProfesseur(@RequestBody ProfesseurEntity professeur) {
        ProfesseurEntity savedProfesseur = professeurService.save(professeur);
        return ResponseEntity.ok(savedProfesseur);
    }

    @GetMapping("/{email}")
    public ResponseEntity<ProfesseurEntity> getProfesseurByEmail(@PathVariable String email) {
        ProfesseurEntity professeur = professeurService.findByEmail(email);
        return ResponseEntity.ok(professeur);
    }
}
