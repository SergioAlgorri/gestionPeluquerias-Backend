package gestionPeluqueria.controllers;

import gestionPeluqueria.dto.HairdresserDTO;
import gestionPeluqueria.dto.HairdresserDTOAssembler;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.services.IHairdresserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/peluquerias")
public class HairdresserController {

    private final IHairdresserService hairdresserService;

    @Autowired
    public HairdresserController(IHairdresserService hairdresserService) {
        this.hairdresserService = hairdresserService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HairdresserDTO>> getAllHairdressers() {
        try {
            List<Hairdresser> hairdressers = hairdresserService.findAll();

            if (hairdressers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<HairdresserDTO> hairdresserDTOS = new ArrayList<>();
            for (Hairdresser h: hairdressers) {
                hairdresserDTOS.add(HairdresserDTOAssembler.generateDTO(h));
            }

            return new ResponseEntity<>(hairdresserDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HairdresserDTO> getHairdresserById(@PathVariable("id") long id) {
        try {
            Hairdresser hairdresser = hairdresserService.findById(id);

            if (hairdresser == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(HairdresserDTOAssembler.generateDTO(hairdresser), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HairdresserDTO> createHairdresser(@RequestBody Hairdresser hairdresser) {
        try {
            Hairdresser hairdresserCreated = hairdresserService.createHairdresser(hairdresser);

            if (hairdresserCreated == null) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(HairdresserDTOAssembler.generateDTO(hairdresser));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HairdresserDTO> updateHairdresser(@PathVariable("id") long idHairdresser,
                                                         @RequestBody Hairdresser hairdresser) {
        try {
            Hairdresser hairdresserUpdated = hairdresserService.updateHairdresser(idHairdresser, hairdresser);

            if (hairdresserUpdated == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(HairdresserDTOAssembler.generateDTO(hairdresserUpdated), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteHairdresser(@PathVariable("id") long idHairdresser) {
        try {
            if (hairdresserService.findById(idHairdresser) == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            hairdresserService.deleteHairdresser(idHairdresser);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
