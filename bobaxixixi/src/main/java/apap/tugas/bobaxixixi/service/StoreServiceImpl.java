package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.StoreModel;
import apap.tugas.bobaxixixi.repository.StoreDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StoreServiceImpl implements StoreService{
    @Autowired
    StoreDb storeDb;

    @Override
    public List<StoreModel> getListStore() { 
        return storeDb.findAll();
    }

    @Override
    public void addStore(StoreModel store){
        store.setStoreCode(getStoreCode(store));
        storeDb.save(store);
    }

    @Override
    public String getStoreCode(StoreModel store){
        String storeCode ="SC";
        
        String storeName = store.getName().toUpperCase();
        LocalTime openTime = store.getOpenHour();
        LocalTime closeTime = store.getCloseHour();
        String threeName = storeName.substring(0, 3);
        String reversedName = "";
        for(int i=2;i>=0;i--){
            reversedName+=threeName.charAt(i);
        }
        String opHour = openTime.toString().substring(0,2);
        int clHour = closeTime.getHour();
        double clHour2 = (clHour/10);
        int clHour3 =(int)Math.floor(clHour2);
        String randString = randomString(2);
        storeCode += (reversedName + opHour + clHour3 + randString);

        return storeCode;
    }
    
    @Override
    public StoreModel getStoreById(Long id){
        Optional<StoreModel> store = storeDb.findById(id);
        if (store.isPresent()) return store.get();
        else return null;
    }

    @Override
    public StoreModel updateStore(StoreModel store){
        store.setStoreCode(getStoreCode(store));
        storeDb.save(store);
        return store;
    }
    
    @Override
    public void deleteStore(StoreModel store){
        storeDb.delete(store);
    }

    public String randomString(int n){
        String Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String str = "";
        for(int i=0;i<n; i++) {
            int index = (int)(Math.random() * Alphabet.length());
            str += Alphabet.charAt(index);
        }
        return str;
    }
}