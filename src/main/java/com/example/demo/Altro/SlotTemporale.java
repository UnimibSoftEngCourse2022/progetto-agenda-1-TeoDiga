package com.example.demo.Altro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlotTemporale implements Comparable<SlotTemporale>{
    public Timestamp inizio;
    public Timestamp fine;
    public Integer durataSec;
    @Override
    public int compareTo(SlotTemporale s){
        return this.getDurataSec().compareTo(s.getDurataSec());
    }
}
