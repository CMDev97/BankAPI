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

    @Test
    void validRange_ShouldReturnTrue_WhenFromIsBeforeTo() {
        LocalDate from = LocalDate.of(2022, 1, 1);
        LocalDate to = LocalDate.of(2022, 1, 2);

        assertTrue(DateUtility.validRange(from, to));
    }

    @Test
    void validRange_ShouldReturnFalse_WhenFromIsAfterTo() {
        LocalDate from = LocalDate.of(2022, 1, 3);
        LocalDate to = LocalDate.of(2022, 1, 2);

        assertFalse(DateUtility.validRange(from, to));
    }

    @Test
    void validRange_ShouldReturnFalse_WhenFromAndToAreEqual() {
        LocalDate fromAndTo = LocalDate.of(2022, 1, 1);

        assertFalse(DateUtility.validRange(fromAndTo, fromAndTo));
    }

    @Test
    void validRange_ShouldReturnFalse_WhenEitherDateIsNull() {
        LocalDate from = LocalDate.of(2022, 1, 1);

        assertFalse(DateUtility.validRange(null, from));
        assertFalse(DateUtility.validRange(from, null));
        assertFalse(DateUtility.validRange(null, null));
    }

}
