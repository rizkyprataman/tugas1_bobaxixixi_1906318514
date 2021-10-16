package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.StoreModel;
import java.time.LocalTime;
import java.util.List;

public interface StoreService {
    List<StoreModel> getListStore();
    void addStore(StoreModel storeModel);
    String getStoreCode(StoreModel store);
    StoreModel getStoreById(Long id);
    StoreModel updateStore(StoreModel store);
    void deleteStore(StoreModel store);
}
