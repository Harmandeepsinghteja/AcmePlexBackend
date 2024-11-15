package __project.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import __project.server.repositories.seatRepository;
import __project.server.model.seat;
import java.util.Optional;

@Service
public class seatService {

    @Autowired
    private seatRepository seatRepository;



    public ArrayList<ArrayList<Boolean>> getSeats(int id)  {
        
        int row = 2;
        int col =10;
        ArrayList<ArrayList<Boolean>>  seat_map = new ArrayList<ArrayList<Boolean>>();
        for(int i=0; i<row; i++){
            ArrayList<Boolean> row_list = new ArrayList<Boolean>();
            for(int j=1; j<=col; j++){
            System.out.println("Seat Number " + i*col + j);
            int isAvaliable = 0;
            try{
                isAvaliable = seatRepository.findById(id, i*col + j);
            }
            catch(Exception e){
                System.out.println("Error " + e);
            }
            if(isAvaliable == 1){
                row_list.add(true);
            }
            else{
                row_list.add(false);
            }
           
            }
            seat_map.add(row_list);
        }
        System.out.println("Seat Map " + seat_map);

        // seat seats =  seatRepository.findById(id,);
        // System.out.println("Seats " + seats);

        return seat_map;
    }

}
