package com.site.stockservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Game {
    private String firstPlayer;
    private String secondPlayer;
    private String[] gameArray;
    private String winner;

}
