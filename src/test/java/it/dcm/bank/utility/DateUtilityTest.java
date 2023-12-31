package it.dcm.bank.utility;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DateUtilityTest {


    @Test
    void getDateString_ShouldFormatDateCorrectly() {
        LocalDate exampleDate = LocalDate.of(2022, 3, 15);
        String expectedFormat = "2022-03-15";

        String formattedDate = DateUtility.getDateString(exampleDate);

        assertEquals(expectedFormat, formattedDate);
    }

}
