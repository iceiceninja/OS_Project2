/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproject2;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author iceiceninja
 */
public class Bellhop implements Runnable {

    public int num;

    public Bellhop(int num) {
        this.num = num;
        System.out.println("Bellhop " + num + " created");
    }

    @Override
    public void run() {
        while (true) {
            try {
                OSProject2.bags_ready.acquire();

                OSProject2.bellhop_line_mutex.acquire();
                Guest guest = OSProject2.bellhop_line.poll();
                OSProject2.bellhop_line_mutex.release();

                guest.bellhop_num = num;

                System.out.println("Bellhop " + num + " recieves bags from guest " + guest.num);
                OSProject2.bellhop_has_bags[guest.num].release();// release telling guest to go to their room
                OSProject2.guest_in_room[guest.num].acquire();  //wait for guest to get to room
                System.out.println("Bellhop " + num + " delivers bags to guest " + guest.num);  //go to room and give bags to guest
                OSProject2.bellhop_coord[guest.num].release();
                OSProject2.bellhop_tip[guest.num].acquire();
                OSProject2.bellhop_ready.release();

                //wait for tip before proceeding
            } catch (InterruptedException ex) {
                Logger.getLogger(Bellhop.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
