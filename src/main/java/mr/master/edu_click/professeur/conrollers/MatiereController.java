package mr.master.edu_click.professeur.conrollers;




import mr.master.edu_click.dao.entities.MatiereEntity;
import mr.master.edu_click.professeur.services.MatiereService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matieres")
public class MatiereController {

    private final MatiereService matiereService;

    public MatiereController(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    @GetMapping
    public List<MatiereEntity> getAllMatieres() {
        return matiereService.findAll();
    }

    @PostMapping
    public ResponseEntity<MatiereEntity> createMatiere(@RequestBody MatiereEntity matiere) {
        MatiereEntity savedMatiere = matiereService.save(matiere);
        return ResponseEntity.ok(savedMatiere);
    }

    @GetMapping("/{nom}")
    public ResponseEntity<MatiereEntity> getMatiereByNom(@PathVariable String nom) {
        MatiereEntity matiere = matiereService.findByNom(nom);
        return ResponseEntity.ok(matiere);
    }
}
