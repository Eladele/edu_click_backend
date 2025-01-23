package mr.master.edu_click.utilisateurs.controllers;


import mr.master.edu_click.dao.entities.UtilisateurEntity;
import mr.master.edu_click.utilisateurs.services.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public List<UtilisateurEntity> getAllUtilisateurs() {
        return utilisateurService.findAll();
    }

    @PostMapping
    public ResponseEntity<UtilisateurEntity> createUtilisateur(@RequestBody UtilisateurEntity utilisateur) {
        UtilisateurEntity savedUtilisateur = utilisateurService.save(utilisateur);
        return ResponseEntity.ok(savedUtilisateur);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UtilisateurEntity> getUtilisateurByEmail(@PathVariable String email) {
        UtilisateurEntity utilisateur = utilisateurService.findByEmail(email);
        return ResponseEntity.ok(utilisateur);
    }
}
