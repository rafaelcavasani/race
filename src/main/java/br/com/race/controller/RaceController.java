package br.com.race.controller;

import br.com.race.entity.Pilot;
import br.com.race.service.LapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class RaceController {

    @Autowired
    private LapService lapService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/race-result")
    public ModelAndView result(@RequestParam MultipartFile file) throws Exception {
        if (file.isEmpty())
            throw new Exception("Arquivo não enviado.");

        List<String> lines = lapService.checkFilePattern(file);
        if (lines.isEmpty())
            throw new Exception("Arquivo fora do padrão esperado.");

        List<Pilot> pilotList = lapService.getRaceResults(lines);
        if (pilotList != null) {
            ModelAndView model = new ModelAndView("result");
            model.addObject("pilotList", pilotList);
            return model;
        } else {
            return null;
        }
    }

}