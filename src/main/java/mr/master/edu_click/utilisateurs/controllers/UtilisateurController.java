package mr.master.edu_click.utilisateurs.controllers;

import jakarta.validation.Valid;
import mr.master.edu_click.dao.entities.UtilisateurEntity;
import mr.master.edu_click.utilisateurs.dtos.UtilisateurDto;
import mr.master.edu_click.utilisateurs.services.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public List<UtilisateurDto> getAll() {
        return utilisateurService.getAll();
    }

    @GetMapping("/{id}")
    public UtilisateurDto getById(@PathVariable("id") Long id) {
        return utilisateurService.getById(id);
    }
//
    @PostMapping
    @Transactional
    public Long add(@RequestBody UtilisateurDto utilisateurDto) {
        return utilisateurService.add(utilisateurDto);
    }
//@PostMapping
//public ResponseEntity<UtilisateurDto> createUser(@Valid @RequestBody UtilisateurDto utilisateurDto) {
//    if (utilisateurDto.getPassword() == null || utilisateurDto.getPassword().trim().isEmpty()) {
//        throw new IllegalArgumentException("Le mot de passe est obligatoire");
//    }
//    UtilisateurEntity utilisateur = utilisateurDto.toEntity();
//    utilisateurService.add(utilisateur.toDto());
//    return ResponseEntity.ok(utilisateur.toDto());
//}


    @PutMapping("/{id}")
    @Transactional
    public Long update(@RequestBody UtilisateurDto utilisateurDto, @PathVariable("id") Long id) {
        return utilisateurService.update(utilisateurDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        utilisateurService.deleteById(id);
    }
}
