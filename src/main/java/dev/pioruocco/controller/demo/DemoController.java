package dev.pioruocco.controller.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping(path = "/demo")
    public ResponseEntity<String> demo(){
        return ResponseEntity.ok("Se vedi questo messaggio il programma funziona!!!");
    }

}
