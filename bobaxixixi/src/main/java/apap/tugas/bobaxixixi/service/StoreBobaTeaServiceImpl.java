package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.BobaTeaModel;
import apap.tugas.bobaxixixi.model.ManagerModel;
import apap.tugas.bobaxixixi.model.StoreModel;
import apap.tugas.bobaxixixi.model.StoreBobaTeaModel;
import apap.tugas.bobaxixixi.repository.StoreBobaTeaDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@Transactional
public class StoreBobaTeaServiceImpl implements StoreBobaTeaService{
    @Autowired
    StoreBobaTeaDb storeBobaTeaDb;

    @Override
    public void addStoreBobaTea (StoreBobaTeaModel storeBobaTea){
        storeBobaTea.setProductionCode(getProductionCode(storeBobaTea));
        storeBobaTeaDb.save(storeBobaTea);
    }

    @Override
    public void deleteStoreBobaTea(StoreBobaTeaModel storeBobaTea){
        storeBobaTeaDb.delete(storeBobaTea);
    }

    @Override
    public String getProductionCode(StoreBobaTeaModel storeBobaTea){
        String productionCode ="PC";
        
        String idStore = String.valueOf(storeBobaTea.getStore().getId());
        BobaTeaModel boba = storeBobaTea.getBobaTea();
        String bobaId = String.valueOf(boba.getId());
        if(idStore.length()<2){
            productionCode += ("00"+idStore);
        }

        else if(idStore.length()<3){
            productionCode += ("0"+idStore);
        }

        else{
            productionCode += idStore;
        }

        if(boba.getTopping() != null){
            productionCode += "1";
        }
        else if(boba.getTopping() == null){
            productionCode += "0";
        } 
        if(bobaId.length()<2){
            productionCode += "00"+bobaId;
        }

        else if(bobaId.length()<3){
            productionCode += "0"+bobaId;
        }

        else{
            productionCode += bobaId;
        }
        
        return productionCode;
    }

    @Override
    public List<StoreBobaTeaModel> getByBobaTopping(String bobaName, String toppingName){
        List<StoreBobaTeaModel> listSBT = storeBobaTeaDb.findAll();
        List<StoreBobaTeaModel> listFinal = new ArrayList<>();
        bobaName = bobaName.toUpperCase();
        toppingName = toppingName.toUpperCase();
        LocalTime sekarang = LocalTime.now();
        for(StoreBobaTeaModel sbt : listSBT){
            BobaTeaModel bobaTea = sbt.getBobaTea();
            StoreModel store = sbt.getStore();
            String bname = bobaTea.getName().toUpperCase();
            String tname = "null";
            if(bobaTea.getTopping() != null){
                tname = bobaTea.getTopping().getName().toUpperCase();
            }
             
            LocalTime waktuTutup = store.getCloseHour();
            LocalTime waktuBuka = store.getOpenHour();
            if(bname.equals(bobaName) && tname.equals(toppingName)){
                if(sekarang.compareTo(waktuBuka)>0 || sekarang.compareTo(waktuTutup)<0){
                    listFinal.add(sbt);
                }
            }
        }
        return listFinal;
    }
    @Override
    public List<ManagerModel> getManagerByBoba(String bobaName){
        List<ManagerModel> listManager = new ArrayList<>();
        List<StoreBobaTeaModel> listStoreBobaTea = storeBobaTeaDb.findAll();
        for(StoreBobaTeaModel sbt: listStoreBobaTea){
            String bobaTeaName = sbt.getBobaTea().getName().toUpperCase();
            if(bobaTeaName.equals(bobaName.toUpperCase())){
                listManager.add(sbt.getStore().getManager());
            }
        }

        return listManager;
    }

}