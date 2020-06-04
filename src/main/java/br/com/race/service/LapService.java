package br.com.race.service;

import br.com.race.entity.Lap;
import br.com.race.entity.Pilot;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.*;

@Service
public class LapService {

    public Map<String, String> getRaceResults(MultipartFile file) {
        Map<String, List> fileResults = this.readFile(file);
        List<Pilot> pilotList = this.getPilotsInfo(fileResults);
        return this.getResultsRace(pilotList);
    }

    private Map<String, String> getResultsRace(List<Pilot> pilotList) {

        return new HashMap<String, String>();
    }

    private List<Pilot> getPilotsInfo(@NotNull Map<String, List> fileResults) {
        List<Lap> lapList = fileResults.get("lap");
        List<Pilot> pilotList = fileResults.get("pilot");
        Collections.sort(lapList);
        for (Pilot pilot : pilotList) {
            Long lapCount = lapList.stream().filter(lap -> lap.getPilot().getNumber() == pilot.getNumber()).count();
            Double meanSpeed = lapList.stream().filter(lap -> lap.getPilot().getNumber() == pilot.getNumber())
                    .mapToDouble(lap -> lap.getMeanSpeed()).average().getAsDouble();

            LocalTime bestLap = lapList.stream().filter(lap -> lap.getPilot().getNumber() == pilot.getNumber())
                    .map(lap -> lap.getLapTime()).min(LocalTime::compareTo).get();

            LocalTime worstLap = lapList.stream().filter(lap -> lap.getPilot().getNumber() == pilot.getNumber())
                    .map(lap -> lap.getLapTime()).max(LocalTime::compareTo).get();

            Long raceTimeLong = lapList.stream().filter(lap -> lap.getPilot().getNumber() == pilot.getNumber())
                    .mapToLong(item -> item.getLapTime().toNanoOfDay())
                    .reduce(0, (subtotal, lap) -> lap + subtotal);
            LocalTime raceTime =  LocalTime.ofNanoOfDay(raceTimeLong);

            LocalTime lastLapTime = lapList.stream().filter(lap -> lap.getPilot().getNumber() == pilot.getNumber())
                    .map(lap -> lap.getTime()).max(LocalTime::compareTo).get();

            pilot.setBestLap(bestLap);
            pilot.setWorstLap(worstLap);
            pilot.setLapCount(lapCount);
            pilot.setMeanSpeed(meanSpeed);
            pilot.setRaceTime(raceTime);
            pilot.setLastLapTime(lastLapTime);
        }
        return pilotList;
    }

    // This method is responsible for reading the txt file and returning a Map with the laps and pilots information
    private Map<String, List> readFile(@NotNull MultipartFile txtFile) {
        List<Lap> lapList = new ArrayList<>();
        List<Pilot> pilotList = new ArrayList<>();
        try {
            InputStream inputStream = txtFile.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            String line;
            String pattern = "(\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\s\\d{3}\\s\\–\\s([\\w\\.]+)\\s\\d\\s\\d?\\d:\\d{2}\\.\\d{3}\\s\\d+?\\,\\d+)";
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                if (line.matches(pattern)) {
                    String[] lineSeparator = line.split("\\s(\\–\\s)?");
                    if (lineSeparator.length == 6) {
                        Pilot pilot = this.addPilotNotRepeated(pilotList, lineSeparator);
                        this.addLapToList(lapList, lineSeparator, pilot);
                    } else {
                        throw new Exception("O arquivo está fora do padrão aceito.");
                    }
                } else {
                    throw new Exception("O arquivo está fora do padrão aceito.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, List> result = new HashMap<>();
        result.put("pilot", pilotList);
        result.put("lap", lapList);
        return result;
    }

    // This method is responsible for adding pilots to the list, if it is not there
    private void addLapToList(@NotNull List<Lap> lapList, @NotNull String[] lineSeparator, @NotNull Pilot pilot) {
        LocalTime time = LocalTime.parse(lineSeparator[0]);
        int lapNumber = Integer.parseInt(lineSeparator[3]);
        String strLapTime = lineSeparator[4].length() == 8 ? "0" + lineSeparator[4] : lineSeparator[4];
        strLapTime = "00:" + strLapTime;
        LocalTime lapTime = LocalTime.parse(strLapTime);
        Double meanSpeed = Double.parseDouble(lineSeparator[5].replace(",", "."));
        Lap lap = new Lap(time, pilot, lapNumber, lapTime, meanSpeed);
        lapList.add(lap);
    }

    // This method is responsible for adding pilots to the list, if it is not there
    private Pilot addPilotNotRepeated(@NotNull List<Pilot> pilotList, @NotNull String[] lineSeparator) {
        Long pilotNumber = Long.parseLong(lineSeparator[1]);
        String pilotName = lineSeparator[2];
        Pilot pilot = new Pilot(pilotNumber, pilotName);
        Long count = pilotList.stream().filter(item -> item.getNumber() == pilot.getNumber()).count();
        if (count == 0) {
            pilotList.add(pilot);
        }
        return pilot;
    }

}
