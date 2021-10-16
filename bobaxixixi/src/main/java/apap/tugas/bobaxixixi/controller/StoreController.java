package apap.tugas.bobaxixixi.controller;

import apap.tugas.bobaxixixi.model.*;
import apap.tugas.bobaxixixi.repository.BobaTeaDb;
import apap.tugas.bobaxixixi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.time.LocalTime;

import java.util.List;
import java.util.ArrayList;

@Controller
public class StoreController {
    
    @Qualifier("storeServiceImpl")
    @Autowired
    private StoreService storeService;
    @Qualifier("managerServiceImpl")
    @Autowired
    private ManagerService managerService;
    @Qualifier("bobaTeaServiceImpl")
    @Autowired
    private BobaTeaService bobaTeaService;
    @Qualifier("storeBobaTeaServiceImpl")
    @Autowired
    private StoreBobaTeaService storeBobaTeaService;

    @GetMapping("/store")
    public String listStore(Model model) {
        List<StoreModel> listStore = storeService.getListStore();
        model.addAttribute("listStore", listStore);
        return "viewAll-store";
    }

    @GetMapping("/store/add")
    public String addStore(Model model){
        List<ManagerModel> listManagerExist = managerService.getListManager();
        model.addAttribute("store", new StoreModel());
        model.addAttribute("listManagerExist", listManagerExist);

        return "form-add-store";
    }

    @PostMapping(value = "/store/add", params = "save")
    public String addStoreSubmitPage(
        @ModelAttribute StoreModel store,
        Model model
    ){
        try{
            storeService.addStore(store);
        }catch (Exception e){
            return "add-store-failed";
        }
        model.addAttribute("storeName", store.getName());
        model.addAttribute("storeCode", store.getStoreCode());
        return "add-store";
    }

    @GetMapping(value = "/store/{id}")
    public String viewDetailStorePage(
        @PathVariable Long id,
        Model model
    ){
        StoreModel store = storeService.getStoreById(id);
        if(store != null){
            List<StoreBobaTeaModel> bobastore = store.getStoreBobaTeaModel();
            List<BobaTeaModel> boba = new ArrayList<>();
            for(StoreBobaTeaModel x: bobastore){
                boba.add(x.getBobaTea());
            }
            model.addAttribute("store", store);
            model.addAttribute("bobaTea", boba);
            return "view-store";
        }
        return "error/400";
    }

    @GetMapping("/store/update/{id}")
    public String updateStoreFormPage(
        @PathVariable Long id,
        Model model
    ){
        StoreModel store = storeService.getStoreById(id);
        if(store != null){
            LocalTime waktuTutup = store.getCloseHour();
            LocalTime waktuBuka = store.getOpenHour();
            LocalTime sekarang = LocalTime.now();
            if(sekarang.compareTo(waktuBuka)<0 || sekarang.compareTo(waktuTutup)>0){
                List<ManagerModel> listManagerExist = managerService.getListManager();
                model.addAttribute("listManagerExist", listManagerExist);
                model.addAttribute("store", store);
                return "form-update-store";
            }
            else{
                model.addAttribute("storeCode", store.getStoreCode());
                model.addAttribute("storeName", store.getName());
                return "update-store-failed";
                }
        }
        
        return "400";  
    }

    @PostMapping("/store/update/{id}")
    public String updateStoreSubmitPage(
        @PathVariable String id,
        @ModelAttribute StoreModel store,
        Model model
    ){
        
        StoreModel updatedStore = storeService.updateStore(store);
        model.addAttribute("storeCode", updatedStore.getStoreCode());
        model.addAttribute("storeName", updatedStore.getName());

        return "update-store";
    }

    @PostMapping("/store/delete/{id}")
    public String deleteStoreSubmitPage(
        @PathVariable Long id,
        Model model
    ){
        StoreModel store = storeService.getStoreById(id);
        LocalTime waktuTutup = store.getCloseHour();
        LocalTime waktuBuka = store.getOpenHour();
        LocalTime sekarang = LocalTime.now();
        String storeName = store.getName();
        String storeCode = store.getStoreCode();
        model.addAttribute("storeName", storeName);
        model.addAttribute("storeCode", storeCode);
        int banyakBobaTea = store.getStoreBobaTeaModel().size();
        if(sekarang.compareTo(waktuBuka)<0 || sekarang.compareTo(waktuTutup)>0){
            if(banyakBobaTea == 0){
                storeService.deleteStore(store);
                return "delete-store";
            }
            return "delete-store-gagal-boba";
        }
        model.addAttribute("storeName", storeName);
        model.addAttribute("storeCode", storeCode);
        return "delete-store-failed";
    }

    @GetMapping("/store/{id}/assign-boba")
    public String addBobaPage(
        @PathVariable Long id,
        Model model
    ){
        StoreModel store = storeService.getStoreById(id);
        List <StoreBobaTeaModel> listStoreBobaTea =store.getStoreBobaTeaModel();
        List <BobaTeaModel> listBobaTea = bobaTeaService.getListBobaTea();
        List <BobaTeaModel> listBobaAdded = new ArrayList<>();
        List <BobaTeaModel> listBobaNotAdded = new ArrayList<>();
        for(StoreBobaTeaModel sbt : listStoreBobaTea){
            listBobaAdded.add(sbt.getBobaTea());
        }
        for(BobaTeaModel bt : listBobaTea){
            if(listBobaAdded.contains(bt) != true){
                listBobaNotAdded.add(bt);
            }
        }
        model.addAttribute("listBobaNotAdded", listBobaNotAdded);
        model.addAttribute("listBobaAdded", listBobaAdded);
        model.addAttribute("store", store);
        return "form-add-bobaFromStore";  
    }

    @PostMapping("/store/{id}/assign-boba")
    public String submitAddBobaPage(
        @PathVariable Long id,
        @RequestParam(value = "checklist" , required = false) List<Long> checklist,
        Model model
    ){  
        StoreModel store = storeService.getStoreById(id);
        List <StoreBobaTeaModel> listStoreBobaTea =store.getStoreBobaTeaModel();
        List <BobaTeaModel> listBobaTeaFinal =new ArrayList<>();
        if(checklist != null){
        for(StoreBobaTeaModel sbt : listStoreBobaTea){
             if(checklist.contains(sbt.getBobaTea().getId())){
                 checklist.remove(sbt.getBobaTea().getId());
                 listBobaTeaFinal.add(sbt.getBobaTea());
             }
             else{
                 storeBobaTeaService.deleteStoreBobaTea(sbt);
             }
        }

        for(int x =0;x<checklist.size();x++){
            Long bobaId = checklist.get(x);
            StoreBobaTeaModel storeBobaTea = new StoreBobaTeaModel();
            BobaTeaModel boba = bobaTeaService.getBobaTeaById(bobaId);
            storeBobaTea.setBobaTea(boba);
            storeBobaTea.setStore(store);
            storeBobaTeaService.addStoreBobaTea(storeBobaTea);
            listBobaTeaFinal.add(boba);
        }
        
        model.addAttribute("listBoba", listBobaTeaFinal);
        model.addAttribute("storeName", store.getName());
        model.addAttribute("store", store);
        return "add-bobaFromStore";
    }
    else{
        for(StoreBobaTeaModel sbtm : listStoreBobaTea){
            storeBobaTeaService.deleteStoreBobaTea(sbtm);
       }
        model.addAttribute("storeName", store.getName());
        model.addAttribute("store", store);
        return "add-bobaFromStore";
    }
    }
}