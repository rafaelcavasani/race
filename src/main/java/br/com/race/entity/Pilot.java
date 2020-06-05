package br.com.race.entity;

import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;

public class Pilot implements Comparable<Pilot> {
    private Long number;
    private String name;
    private Long lapCount;
    private Long position;
    private LocalTime bestLap;
    private LocalTime worstLap;
    private Double meanSpeed;
    private LocalTime lastLapTime;
    private LocalTime raceTime;
    private LocalTime diffToFirstPosition;
    private Boolean raceFinished;
    private LocalTime bestRaceLap;

    public Pilot(Long number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLapCount() {
        return lapCount;
    }

    public void setLapCount(Long lapCount) {
        this.lapCount = lapCount;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public LocalTime getBestLap() {
        return bestLap;
    }

    public void setBestLap(LocalTime bestLap) {
        this.bestLap = bestLap;
    }

    public Double getMeanSpeed() {
        return meanSpeed;
    }

    public void setMeanSpeed(Double meanSpeed) {
        this.meanSpeed = meanSpeed;
    }

    public LocalTime getWorstLap() {
        return worstLap;
    }

    public void setWorstLap(LocalTime worstLap) {
        this.worstLap = worstLap;
    }

    public LocalTime getLastLapTime() {
        return lastLapTime;
    }

    public void setLastLapTime(LocalTime lastLapTime) {
        this.lastLapTime = lastLapTime;
    }

    public LocalTime getRaceTime() {
        return raceTime;
    }

    public void setRaceTime(LocalTime raceTime) {
        this.raceTime = raceTime;
    }

    public LocalTime getDiffToFirstPosition() {
        return diffToFirstPosition;
    }

    public void setDiffToFirstPosition(LocalTime diffToFirstPosition) {
        this.diffToFirstPosition = diffToFirstPosition;
    }

    public Boolean getRaceFinished() {
        return raceFinished;
    }

    public void setRaceFinished(Boolean raceFinished) {
        this.raceFinished = raceFinished;
    }

    public LocalTime getBestRaceLap() {
        return bestRaceLap;
    }

    public void setBestRaceLap(LocalTime bestRaceLap) {
        this.bestRaceLap = bestRaceLap;
    }

    @Override
    public int compareTo(@NotNull Pilot pilot) {
        return getLastLapTime().compareTo(pilot.getLastLapTime());
    }
}
