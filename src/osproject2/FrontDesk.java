package osproject2;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author iceiceninja
 */
public class FrontDesk implements Runnable {

    int num;

    public FrontDesk(int num) {
        this.num = num;
        System.out.println("Frontdesk " + num + " created");
    }

    @Override
    public void run() {
        while (true) {
            try {
                OSProject2.guest_ready.acquire();

                OSProject2.frontdesk_line_mutex.acquire();
                Guest guest = OSProject2.frontdesk_line.poll();
                OSProject2.frontdesk_line_mutex.release();

                guest.frontdesk_num = num;

                OSProject2.roomNum_mutex.acquire();
                guest.hotelroom_num = OSProject2.room_number;
                OSProject2.room_number++;
                OSProject2.roomNum_mutex.release();

                System.out.println("Front desk employee " + num + " registers guest " + guest.num + " and assigns room " + guest.hotelroom_num);

                OSProject2.frontdesk_coord[guest.num].release();
                OSProject2.frontdesk_ready.release();
            } catch (InterruptedException ex) {
                Logger.getLogger(FrontDesk.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
