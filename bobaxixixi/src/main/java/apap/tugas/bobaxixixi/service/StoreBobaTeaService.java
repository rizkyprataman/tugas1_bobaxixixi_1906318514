package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.StoreBobaTeaModel;
import apap.tugas.bobaxixixi.model.BobaTeaModel;
import apap.tugas.bobaxixixi.model.ManagerModel;
import java.time.LocalTime;
import java.util.List;

public interface StoreBobaTeaService {
    void addStoreBobaTea (StoreBobaTeaModel storeBobaTea);
    void deleteStoreBobaTea (StoreBobaTeaModel storeBobaTea);
    String getProductionCode(StoreBobaTeaModel storeBobaTea);
    List<StoreBobaTeaModel> getByBobaTopping(String bobaName, String toppingName);
    List<ManagerModel> getManagerByBoba(String bobaName);
}
