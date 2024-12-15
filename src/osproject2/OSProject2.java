package osproject2;
//https://www.w3schools.com/java/java_packages.asp

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class OSProject2 {

    /**
     * @param args the command line arguments
     */
    final static int numOfGuests = 25;
    final static int numOfFrontdesk = 2;
    final static int numOfBellhop = 2;

    static Guest[] guests = new Guest[numOfGuests];
    static Thread[] guestThreads = new Thread[numOfGuests];

    static FrontDesk[] frontdesk = new FrontDesk[numOfFrontdesk];
    static Thread[] frontdeskThreads = new Thread[numOfFrontdesk];

    static Bellhop[] bellhops = new Bellhop[numOfBellhop];
    static Thread[] bellhopThreads = new Thread[numOfBellhop];

    public static Semaphore guest_ready = new Semaphore(0, true);
    public static Semaphore frontdesk_ready = new Semaphore(numOfFrontdesk, true);
    public static Semaphore bellhop_ready = new Semaphore(numOfBellhop, true);
    public static Semaphore bags_ready = new Semaphore(0, true);

    public static Semaphore[] frontdesk_coord = new Semaphore[numOfGuests];
    public static Semaphore[] bellhop_coord = new Semaphore[numOfGuests];
    public static Semaphore[] guest_in_room = new Semaphore[numOfGuests];
    public static Semaphore[] bellhop_tip = new Semaphore[numOfGuests];
    public static Semaphore[] bellhop_has_bags = new Semaphore[numOfGuests];

    public static Queue<Guest> frontdesk_line = new LinkedList<>();
    public static Queue<Guest> bellhop_line = new LinkedList<>();

    public static Semaphore frontdesk_line_mutex = new Semaphore(1, true);
    public static Semaphore bellhop_line_mutex = new Semaphore(1, true);
    public static Semaphore roomNum_mutex = new Semaphore(1, true);

    public static int room_number = 0;

    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Simulation Starts");
        
        //  initialize semaphore arrays
        for (int i = 0; i < numOfGuests; i++) {
            frontdesk_coord[i] = new Semaphore(0);
            bellhop_coord[i] = new Semaphore(0);
            guest_in_room[i] = new Semaphore(0);
            bellhop_tip[i] = new Semaphore(0);
            bellhop_has_bags[i] = new Semaphore(0);
        }
        //Create guest Threads
        for (int i = 0; i < numOfGuests; i++) {
            guests[i] = new Guest(i);
            guestThreads[i] = new Thread(guests[i]);
            guestThreads[i].start();
        }
        //Create frontdesk Threads
        for (int i = 0; i < numOfFrontdesk; i++) {
            frontdesk[i] = new FrontDesk(i);
            frontdeskThreads[i] = new Thread(frontdesk[i]);
            frontdeskThreads[i].setDaemon(true);
            frontdeskThreads[i].start();
        }
        //Create bellhop Threads
        for (int i = 0; i < numOfBellhop; i++) {
            bellhops[i] = new Bellhop(i);
            bellhopThreads[i] = new Thread(bellhops[i]);
            bellhopThreads[i].setDaemon(true);
            bellhopThreads[i].start();
        }

        for (int i = 0; i < numOfGuests; i++) {
            try {
                int temp = guests[i].num;
                guestThreads[i].join();
                System.out.println("Guest " + temp + " joined!");
            } catch (InterruptedException ex) {
                System.out.println("Thread join failed! Thread: " + i);
            }
        }
        System.out.println("Simulation ends");

    }

}
