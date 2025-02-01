package mr.master.edu_click.professeur.conrollers;

import mr.master.edu_click.professeur.dtos.ProfesseurDto;
import mr.master.edu_click.professeur.services.ProfesseurService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/professeurs")
public class ProfesseurController {

    private final ProfesseurService professeurService;

    public ProfesseurController(ProfesseurService professeurService) {
        this.professeurService = professeurService;
    }

    @GetMapping
    public List<ProfesseurDto> getAll() {
        return professeurService.getAll();
    }

    @GetMapping("/{id}")
    public ProfesseurDto getById(@PathVariable("id") Long id) {
        return professeurService.getById(id);
    }

    @PostMapping
    @Transactional
    public Long add(@RequestBody ProfesseurDto professeurDto) {
        return professeurService.add(professeurDto);
    }

    @PutMapping("/{id}")
    @Transactional
    public Long update(@RequestBody ProfesseurDto professeurDto, @PathVariable("id") Long id) {
        return professeurService.update(professeurDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        professeurService.deleteById(id);
    }
}
