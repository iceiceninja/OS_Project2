/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproject2;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author iceiceninja
 */
public class Guest implements Runnable {

    public final int num;
//    private static Semaphore customer_frontdesk_sem;
//    private static Semaphore customer_bellhop_sem;

    int hotelroom_num;
    public int bags;
    public int bellhop_num;
    public int frontdesk_num;

    public Guest(int num) {
        this.num = num;
//        Guest.customer_frontdesk_sem = OSProject2.frontdesk_guest_sem[num];
//        Guest.customer_bellhop_sem = OSProject2.bellhop_guest_sem[num];

        bags = (int) (Math.random() * 6);
        System.out.println("Guest " + num + " created");
    }

    @Override
    public void run() {
        try {
            System.out.println("Guest " + num + " entered the hotel with " + bags + " bags!");
            OSProject2.frontdesk_line_mutex.acquire();
            OSProject2.frontdesk_line.offer(this);  //use mutex to add guest to line
            OSProject2.frontdesk_line_mutex.release();

            OSProject2.guest_ready.release();
            OSProject2.frontdesk_ready.acquire();
            //getHotelRoom
            OSProject2.frontdesk_coord[num].acquire();
            if (bags > 2) {
                System.out.println("Guest " + num + " requests help with bags");
                OSProject2.bellhop_line_mutex.acquire();
                OSProject2.bellhop_line.offer(this);
                OSProject2.bellhop_line_mutex.release();

                OSProject2.bags_ready.release();
                OSProject2.bellhop_ready.acquire();
                //wait for Bellhop to take bags
                OSProject2.bellhop_has_bags[num].acquire();
                goToRoom();
                OSProject2.guest_in_room[num].release();    //this guest is now in their room
                OSProject2.bellhop_coord[num].acquire();    //this guest is now waiting on bellhop
                recieveBagsFromBellhop(bellhop_num);    //recieve bags from bellhop
                OSProject2.bellhop_tip[num].release();//signal tip has been given and release bellhop

            } else {
                goToRoom();
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Guest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Guest " + num + " retires for the evening");

    }

    public void printCheckedIn(int frontdeskEmployee) {
        System.out.println("Guest " + num + " recieves room key for room " + hotelroom_num + " from frontdesk employee " + frontdeskEmployee);
    }

    public void recieveBagsFromBellhop(int bellhopEmployee) {
        System.out.println("Guest " + num + " recieves bags from bellhop " + bellhopEmployee + " and gives tip");
    }

    public void goToRoom() {
        System.out.println("Guest " + num + " enters room " + hotelroom_num);

    }
}
