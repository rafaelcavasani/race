package br.com.race.controller;

import br.com.race.service.LapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
public class RaceController {

    @Autowired
    private LapService lapService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/race-result")
    public String result(@RequestParam MultipartFile file) throws Exception {
        Map<String, String> results = lapService.getRaceResults(file);
        return "index";
    }

}