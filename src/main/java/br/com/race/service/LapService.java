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

    private final Long maxLaps = 4L;
    private final Long lineArgumentsCount = 6L;

    // this is a main method
    public List<Pilot> getRaceResults(List<String> lines) throws IOException, Exception {
        Map<String, List> fileResults = this.readLines(lines);
        List<Pilot> pilotList = this.getPilotsInfo(fileResults);
        List<Pilot> pilotListResult = this.getResultsRace(pilotList);
        return pilotListResult;
    }

    // This method is responsible for calculating all the race results
    private List<Pilot> getResultsRace(List<Pilot> pilotList) {
        if (!pilotList.isEmpty()) {
            Collections.sort(pilotList);
            Long position = 1L;
            LocalTime firstPositionTime = LocalTime.now();
            for (Pilot pilot : pilotList) {
                if (pilot.getLapCount() == this.maxLaps) {
                    pilot.setPosition(position);
                    if (position == 1) {
                        firstPositionTime = pilot.getRaceTime();
                        pilot.setDiffToFirstPosition(LocalTime.parse("00:00:00.000"));
                        pilot.setRaceFinished(true);
                    } else {
                        Long diffToFirstPosition = pilot.getRaceTime().toNanoOfDay() - firstPositionTime.toNanoOfDay();
                        pilot.setDiffToFirstPosition(LocalTime.ofNanoOfDay(diffToFirstPosition));
                        pilot.setRaceFinished(true);
                    }
                    position++;
                } else {
                    pilot.setDiffToFirstPosition(LocalTime.parse("00:00:00.000"));
                    pilot.setRaceFinished(false);
                }
            }
            LocalTime bestRaceLapTime = pilotList.stream().map(pilot -> pilot.getBestLap()).min(LocalTime::compareTo).get();
            pilotList.stream().filter(pilot -> pilot.getBestLap() == bestRaceLapTime)
                    .forEach(pilot -> pilot.setBestRaceLap(bestRaceLapTime));
        }

        return pilotList;
    }

    // This method is responsible for calculating all the pilot results
    private List<Pilot> getPilotsInfo(@NotNull Map<String, List> fileResults) {
        List<Lap> lapList = fileResults.get("lap");
        List<Pilot> pilotList = fileResults.get("pilot");
        if (!pilotList.isEmpty()) {
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
                LocalTime raceTime = LocalTime.ofNanoOfDay(raceTimeLong);

                LocalTime lastLapTime = lapList.stream().filter(lap -> lap.getPilot().getNumber() == pilot.getNumber())
                        .map(lap -> lap.getTime()).max(LocalTime::compareTo).get();

                pilot.setBestLap(bestLap);
                pilot.setWorstLap(worstLap);
                pilot.setLapCount(lapCount);
                pilot.setMeanSpeed(meanSpeed);
                pilot.setRaceTime(raceTime);
                pilot.setLastLapTime(lastLapTime);
            }
        }
        return pilotList;
    }

    // This method is responsible for reading the txt file and returning a Map with the laps and pilots information
    private Map<String, List> readLines(@NotNull List<String> lines) {
        List<Lap> lapList = new ArrayList<>();
        List<Pilot> pilotList = new ArrayList<>();
        for (String line: lines) {
            String[] lineSeparator = line.split("\\s(\\–\\s)?");
            Pilot pilot = this.addNotRepeatedPilotToList(pilotList, lineSeparator);
            this.addLapToList(lapList, lineSeparator, pilot);
        }
        Map<String, List> result = new HashMap<>();
        result.put("pilot", pilotList);
        result.put("lap", lapList);
        return result;
    }

    // This method is responsible for adding pilots to the list, if it is not there
    private void addLapToList(@NotNull List<Lap> lapList, @NotNull String[] lineSeparator, @NotNull Pilot pilot) {
        int lapNumber = Integer.parseInt(lineSeparator[3]);
        if (lapNumber <= this.maxLaps) {
            LocalTime time = LocalTime.parse(lineSeparator[0]);
            String strLapTime = lineSeparator[4].length() == 8 ? "0" + lineSeparator[4] : lineSeparator[4];
            strLapTime = "00:" + strLapTime;
            LocalTime lapTime = LocalTime.parse(strLapTime);
            Double meanSpeed = Double.parseDouble(lineSeparator[5].replace(",", "."));
            Lap lap = new Lap(time, pilot, lapNumber, lapTime, meanSpeed);
            lapList.add(lap);
        }
    }

    // This method is responsible for adding pilots to the list, if it is not there
    private Pilot addNotRepeatedPilotToList(@NotNull List<Pilot> pilotList, @NotNull String[] lineSeparator) {
        Long pilotNumber = Long.parseLong(lineSeparator[1]);
        String pilotName = lineSeparator[2];
        Pilot pilot = new Pilot(pilotNumber, pilotName);
        Long count = pilotList.stream().filter(item -> item.getNumber() == pilot.getNumber()).count();
        if (count == 0) {
            pilotList.add(pilot);
        }
        return pilot;
    }

    // This method is responsible for check if file is in expected pattern
    public List<String> checkFilePattern(MultipartFile txtFile) {
        List<String> lines = new ArrayList<>();
        try {
            InputStream inputStream = txtFile.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            String line;
            String pattern = "(\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\s\\d{3}\\s\\–\\s([\\w\\.]+)\\s\\d\\s\\d?\\d:\\d{2}\\.\\d{3}\\s\\d+?\\,\\d+)";
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                if (line.matches(pattern))
                    lines.add(line);
                else
                    return new ArrayList<>();

                String[] lineSeparator = line.split("\\s(\\–\\s)?");
                if (lineSeparator.length != this.lineArgumentsCount)
                    return new ArrayList<>();
            }
        } catch (IOException e) {
            return new ArrayList<>();
        }
        return lines;
    }

}
