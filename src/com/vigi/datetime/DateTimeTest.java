package com.vigi.datetime;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.Locale;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by vigi on 2/22/2015.
 */
public class DateTimeTest {

    @Test
    public void local_date() {
        LocalDate date = LocalDate.of(2015, 2, 22);

        assertThat(date.getYear(), is(2015));
        assertThat(date.getMonth(), is(Month.FEBRUARY));
        assertThat(date.getDayOfMonth(), is(22));
        assertThat(date.getDayOfWeek(), is(DayOfWeek.SUNDAY));
        assertThat(date.getDayOfYear(), is(53));
        assertThat(date.isLeapYear(), is(false));
    }

    @Test
    public void local_time() {
        LocalTime time = LocalTime.of(13, 33, 44);

        assertThat(time.getHour(), is(13));
        assertThat(time.getMinute(), is(33));
        assertThat(time.getSecond(), is(44));
    }

    @Test
    public void local_date_time() {
        LocalDateTime dt1 = LocalDateTime.of(2015, Month.FEBRUARY, 22, 13, 45, 21);

        assertThat(dt1.getYear(), is(2015));
        assertThat(dt1.getHour(), is(13));
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void instant_should_throw_exception() {
        Instant instant = Instant.now();

        instant.get(ChronoField.DAY_OF_YEAR);
    }

    @Test
    public void duration() {
        LocalDateTime dt1 = LocalDateTime.of(2015, Month.FEBRUARY, 22, 13, 45, 21);
        LocalDateTime dt2 = LocalDateTime.of(2015, Month.FEBRUARY, 21, 13, 45, 21);
        Duration d = Duration.between(dt1, dt2);

        assertThat(d.getSeconds(), is(-86400L));
    }

    @Test
    public void period() {
        LocalDate dt1 = LocalDate.of(2015, 2, 22);
        LocalDate dt2 = LocalDate.of(2015, 7, 19);
        Period p = Period.between(dt1, dt2);

        assertThat(p.getMonths(), is(4));
        assertThat(p.getDays(), is(27));
    }

    @Test
    public void plus_days() {
        LocalDate dt1 = LocalDate.of(2015, 2, 22);
        LocalDate dt2 = dt1.plusDays(3);

        assertThat(dt2.getDayOfMonth(), is(25));
    }

    @Test
    public void temporal_adjusters() {
        LocalDate dt1 = LocalDate.of(2015, 2, 22);
        LocalDate dt2 = dt1.with(nextOrSame(DayOfWeek.SATURDAY));
        LocalDate dt3 = dt1.with(lastDayOfMonth());

        assertThat(dt2.getDayOfMonth(), is(28));
        assertThat(dt3.getDayOfMonth(), is(28));
    }

    @Test
    public void next_working_day() {
        LocalDate dt1 = LocalDate.of(2015, 2, 21);
        LocalDate next = dt1.with(new NextWorkingDay());

        assertThat(next.getDayOfMonth(), is(23));
    }

    @Test
    public void parsing_and_printing_dates() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d. MMMM yyyy", new Locale("ro"));
        LocalDate date1 = LocalDate.of(2015, 2, 21);
        String formattedDate = date1.format(dtf);
        LocalDate date2 = LocalDate.parse(formattedDate, dtf);

        assertThat(formattedDate, is("21. februarie 2015"));
        assertThat(date2.getDayOfMonth(), is(21));
    }

    @Test
    public void time_zones() {
        ZoneId zoneId = ZoneId.of("Europe/Bucharest");
        LocalDate date = LocalDate.of(2015, Month.MARCH, 11);
        ZonedDateTime zdt1 = date.atStartOfDay(zoneId);
        LocalDateTime dateTime = LocalDateTime.of(2015, Month.MARCH, 15, 16, 33);
        ZonedDateTime zdt2 = dateTime.atZone(zoneId);

        assertThat(zdt1.toString(), is("2015-03-11T00:00+02:00[Europe/Bucharest]"));
        assertThat(zdt2.toString(), is("2015-03-15T16:33+02:00[Europe/Bucharest]"));
    }

    private static class NextWorkingDay implements TemporalAdjuster {

        @Override
        public Temporal adjustInto(Temporal temporal) {
            DayOfWeek dayOfWeek = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int daysToAdd = 1;
            if (DayOfWeek.FRIDAY.equals(dayOfWeek)) {
                daysToAdd = 3;
            } else if (DayOfWeek.SATURDAY.equals(dayOfWeek)) {
                daysToAdd = 2;
            }
            return temporal.plus(daysToAdd, ChronoUnit.DAYS);
        }
    }
}
