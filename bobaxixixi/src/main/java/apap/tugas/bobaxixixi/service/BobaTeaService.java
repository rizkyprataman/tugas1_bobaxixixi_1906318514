package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.BobaTeaModel;

import java.util.List;

public interface BobaTeaService {
    List<BobaTeaModel> getListBobaTea();
    void addBobaTea(BobaTeaModel bobaTea);
    BobaTeaModel getBobaTeaById(Long id);
    BobaTeaModel updateBobaTea(BobaTeaModel bobaTea);
    void deleteBobaTea(BobaTeaModel bobaTea);

}
