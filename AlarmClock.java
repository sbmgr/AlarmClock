import java.awt.Toolkit;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime setTime = null;

        while (setTime == null) {
            try {
                System.out.print("Enter time for alarm (HH:mm:ss): ");
                String timeInput = in.nextLine();

                setTime = LocalTime.parse(timeInput, dtFormat);
                System.out.println("Alarm is set to go off at " + setTime);
            } catch (DateTimeParseException e) {
                System.out.println("Format error. Try again using HH:mm:ss");
            }
        }

        AlarmClock alarm = new AlarmClock(setTime);
        Thread t = new Thread(alarm);
        t.start();

        in.close();
    }
}

class AlarmClock implements Runnable {
    private final LocalTime targetTime;
    private final LocalDateTime targetDateTime;

    AlarmClock(LocalTime targetTime) {
        this.targetTime = targetTime;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayTarget = now.with(targetTime);
        
        if (now.isAfter(todayTarget)) {
            this.targetDateTime = todayTarget.plusDays(1);
            
        } 
        
        else {
            this.targetDateTime = todayTarget;
        }
    }

    @Override
    public void run() {
        System.out.println("Waiting for the alarm time...");

        while (LocalDateTime.now().isBefore(targetDateTime)) {
            try {
                Thread.sleep(1000);
                LocalTime current = LocalTime.now();
                System.out.printf("\rCurrent time: %02d:%02d:%02d", current.getHour(), current.getMinute(), current.getSecond());
                System.out.flush();
            } catch (InterruptedException e) {
                System.out.println("\nAlarm thread interrupted");
                return;
            }
        }

        System.out.println("\nTime's up!");
        Toolkit.getDefaultToolkit().beep();
    }
}