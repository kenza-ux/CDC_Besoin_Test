package Batiment.Utils;
import java.time.DayOfWeek;
import java.time.LocalDate;
public class FakeDay {
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    private LocalDate date;

    public FakeDay(LocalDate date) {
        this.date = date;
    }


}
