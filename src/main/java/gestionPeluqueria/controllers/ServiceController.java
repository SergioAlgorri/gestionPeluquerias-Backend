package gestionPeluqueria.controllers;

import gestionPeluqueria.dto.RequestServiceDTO;
import gestionPeluqueria.entities.composite.ServiceComponent;
import gestionPeluqueria.services.impl.HairServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/peluquerias/servicios")
public class ServiceController {

    private final HairServiceImpl hairService;

    public ServiceController(HairServiceImpl hairService) {
        this.hairService = hairService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServiceComponent>> getAllServices() {
        try {
            List<ServiceComponent> services = hairService.findAll();

            if (services.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(services, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceComponent> getService(@PathVariable("id") long id) {
        try {
            ServiceComponent service = hairService.findById(id);

            if (service == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(service, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceComponent> createService(@RequestBody RequestServiceDTO service) {
        try {
            ServiceComponent serviceCreated = hairService.createService(service);

            if (serviceCreated == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(serviceCreated);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceComponent> updateService(@PathVariable("id") long id,
                                                          @RequestBody ServiceComponent service) {
        try {
            ServiceComponent serviceUpdated = hairService.updateService(id, service);

            if (serviceUpdated == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(serviceUpdated, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteService(@PathVariable("id") long id) {
        try {
            if (hairService.findById(id) == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            hairService.deleteService(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
