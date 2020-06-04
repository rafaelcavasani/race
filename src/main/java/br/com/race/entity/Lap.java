package br.com.race.entity;

import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;

public class Lap implements Comparable<Lap> {

    private LocalTime time;
    private Pilot pilot;
    private int lapNumber;
    private LocalTime lapTime;
    private double meanSpeed;

    public Lap() {}

    public Lap(LocalTime time, Pilot pilot, int lapNumber, LocalTime lapTime, double meanSpeed) {
        this.time = time;
        this.pilot = pilot;
        this.lapNumber = lapNumber;
        this.lapTime = lapTime;
        this.meanSpeed = meanSpeed;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    public int getLapNumber() {
        return lapNumber;
    }

    public void setLapNumber(int lapNumber) {
        this.lapNumber = lapNumber;
    }

    public LocalTime getLapTime() {
        return lapTime;
    }

    public void setLapTime(LocalTime lapTime) {
        this.lapTime = lapTime;
    }

    public double getMeanSpeed() {
        return meanSpeed;
    }

    public void setMeanSpeed(double meanSpeed) {
        this.meanSpeed = meanSpeed;
    }

    @Override
    public int compareTo(@NotNull Lap lap) {
        return getTime().compareTo(lap.getTime());
    }
}
