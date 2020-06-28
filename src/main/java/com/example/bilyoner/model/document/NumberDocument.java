package com.example.bilyoner.model.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NumberDocument {
    @Id
    private String id;

    private Long number;

    @JsonProperty(value = "insert_date")
    private LocalDateTime insertDate;
}
