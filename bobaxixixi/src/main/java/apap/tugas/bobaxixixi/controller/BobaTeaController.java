package apap.tugas.bobaxixixi.controller;

import apap.tugas.bobaxixixi.model.*;
import apap.tugas.bobaxixixi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;

import java.util.List;
import java.util.ArrayList;

@Controller
public class BobaTeaController {
    @Qualifier("bobaTeaServiceImpl")
    @Autowired
    private BobaTeaService bobaTeaService;
    @Qualifier("toppingServiceImpl")
    @Autowired
    private ToppingService toppingService;
    @Qualifier("storeServiceImpl")
    @Autowired
    private StoreService storeService; 
    @Qualifier("storeBobaTeaServiceImpl")
    @Autowired
    private StoreBobaTeaService storeBobaTeaService;   

    @GetMapping("/boba")
    public String listBoba(Model model) {
        List<BobaTeaModel> listBobaTea = bobaTeaService.getListBobaTea();
        model.addAttribute("listBoba", listBobaTea);
        return "viewAll-boba";
    }

    @GetMapping("/boba/add")
    public String addBoba(Model model) {
        List<ToppingModel> listToppingExist = toppingService.getListTopping();
        model.addAttribute("bobaTea", new BobaTeaModel());
        model.addAttribute("listToppingExist", listToppingExist);
        return "form-add-boba";
    }

    @PostMapping(value = "/boba/add", params = "save")
    public String addBobaSubmitPage(
        @ModelAttribute BobaTeaModel bobaTea,
        Model model
    ){
        bobaTeaService.addBobaTea(bobaTea);
        model.addAttribute("bobaTeaName", bobaTea.getName());
        return "add-boba-tea";
    }

    @GetMapping("/boba/update/{id}")
    public String updateBobaFormPage(
        @PathVariable Long id,
        Model model
    ){
        BobaTeaModel boba = bobaTeaService.getBobaTeaById(id);
        model.addAttribute("bobaTea", boba);
        LocalTime sekarang = LocalTime.now();
        if(boba != null){
            List <StoreBobaTeaModel> sbt = boba.getStoreBobaTeaModel();
            if(sbt.size()!= 0){
                for(StoreBobaTeaModel s: sbt){
                    StoreModel store = s.getStore();
                    LocalTime waktuTutup = store.getCloseHour();
                    LocalTime waktuBuka = store.getOpenHour();
                    if(sekarang.isAfter(waktuTutup) || sekarang.isBefore(waktuBuka)){
                        List<ToppingModel> listToppingExist = toppingService.getListTopping();
                        model.addAttribute("listToppingExist", listToppingExist);
                        return "form-update-boba-tea";
                    }
                }
            }
            else if(sbt.size()==0){
                List<ToppingModel> listToppingExist = toppingService.getListTopping();
                model.addAttribute("listToppingExist", listToppingExist);
                return "form-update-boba-tea";
            }
            
            return "update-gagal";
            
        }

        //jangan lupa ubah
        return "error400";
        
    }

    @PostMapping("/boba/update/{id}")
    public String updateStoreSubmitPage(
        @PathVariable String id,
        @ModelAttribute BobaTeaModel bobaTea,
        Model model
    ){
        
        BobaTeaModel updatedBobaTea = bobaTeaService.updateBobaTea(bobaTea);
        model.addAttribute("bobaTeaName", updatedBobaTea.getName());

        return "update-boba-tea";
    }

    @PostMapping("/boba/delete/{id}")
    public String deleteBobaSubmitPage(
        @PathVariable Long id,
        Model model
    ){
        BobaTeaModel bobaTea = bobaTeaService.getBobaTeaById(id);
        int banyakStore = bobaTea.getStoreBobaTeaModel().size();
        String bobaTeaName = bobaTea.getName();
        if((bobaTea != null) && (banyakStore == 0)){
            bobaTeaService.deleteBobaTea(bobaTea);
            model.addAttribute("bobaTeaName", bobaTeaName);
            return "delete-boba-tea";
        }
        model.addAttribute("bobaTeaName", bobaTeaName);
        return "delete-boba-tea-failed";
    }

    @GetMapping("/boba/{id}/assign-boba")
    public String addBobaPage(
        @PathVariable Long id,
        Model model
    ){
        BobaTeaModel bobaTea = bobaTeaService.getBobaTeaById(id);
        List <StoreBobaTeaModel> listStoreBobaTea = bobaTea.getStoreBobaTeaModel();
        List <StoreModel> listStore = storeService.getListStore();
        List <StoreModel> listStoreAdded = new ArrayList<>();
        List <StoreModel> listStoreNotAdded = new ArrayList<>();
        for(StoreBobaTeaModel sbt : listStoreBobaTea){
            listStoreAdded.add(sbt.getStore());
        }
        for(StoreModel s : listStore){
            if(listStoreAdded.contains(s) != true){
                listStoreNotAdded.add(s);
            }
        }
        model.addAttribute("listStoreNotAdded",  listStoreNotAdded);
        model.addAttribute("listStoreAdded", listStoreAdded);
        model.addAttribute("bobaTea", bobaTea);
        return "form-add-bobaFromBoba";  
    }

    @PostMapping("/boba/{id}/assign-boba")
    public String submitAddStorePage(
        @PathVariable Long id,
        @RequestParam(value = "checklist" , required = false) List<Long> checklist,
        Model model
    ){  
        BobaTeaModel bobaTea = bobaTeaService.getBobaTeaById(id);
        List <StoreBobaTeaModel> listStoreBobaTea =bobaTea.getStoreBobaTeaModel();
        List <StoreModel> listStoreFinal =new ArrayList<>();
        if(checklist != null){
        for(StoreBobaTeaModel sbt : listStoreBobaTea){
             if(checklist.contains(sbt.getStore().getId())){
                 checklist.remove(sbt.getStore().getId());
                 listStoreFinal.add(sbt.getStore());
             }
             else{
                 storeBobaTeaService.deleteStoreBobaTea(sbt);
             }
        }

        for(int x =0;x<checklist.size();x++){
            Long storeId = checklist.get(x);
            StoreBobaTeaModel storeBobaTea = new StoreBobaTeaModel();
            StoreModel store = storeService.getStoreById(storeId);
            storeBobaTea.setBobaTea(bobaTea);
            storeBobaTea.setStore(store);
            storeBobaTeaService.addStoreBobaTea(storeBobaTea);
            listStoreFinal.add(store);
        }
        
        model.addAttribute("listStore", listStoreFinal);
        model.addAttribute("bobaName", bobaTea.getName());
        model.addAttribute("bobaTea", bobaTea);
        return "add-bobaFromBoba";
    }
    else{
        for(StoreBobaTeaModel sbtm : listStoreBobaTea){
            storeBobaTeaService.deleteStoreBobaTea(sbtm);
       }
        model.addAttribute("bobaName", bobaTea.getName());
        model.addAttribute("bobaTea", bobaTea);
        return "add-bobaFromBoba";
    }
    }

    @RequestMapping(value="search", method = RequestMethod.GET)
    public String searchbyBobaToppingPage(Model model){
        List<ToppingModel> listToppingExist = toppingService.getListTopping();
        List<BobaTeaModel> listBobaTeaExist = bobaTeaService.getListBobaTea();
        model.addAttribute("listToppingExist", listToppingExist);
        model.addAttribute("listBobaTeaExist", listBobaTeaExist);
        return "search";
    }

    @RequestMapping(value= "search",params = {"bobaName", "toppingName"} , method = RequestMethod.GET)
    public String searchbyBobaToppingSubmit(
        @RequestParam(required = false, value = "bobaName") String bobaName,
        @RequestParam(required = false, value = "toppingName") String toppingName,
        Model model){
        List<ToppingModel> listToppingExist = toppingService.getListTopping();
        List<BobaTeaModel> listBobaTeaExist = bobaTeaService.getListBobaTea();    
        LocalTime sekarang = LocalTime.now();
        List<StoreBobaTeaModel> fixed = new ArrayList<>();
        List<StoreBobaTeaModel> liststoreBobaTea = storeBobaTeaService.getByBobaTopping(bobaName, toppingName);
        for(StoreBobaTeaModel x : liststoreBobaTea){
            StoreModel store = x.getStore();
            LocalTime waktuTutup = store.getCloseHour();
            LocalTime waktuBuka = store.getOpenHour();
            if(sekarang.isAfter(waktuBuka) && sekarang.isBefore(waktuTutup)){
                fixed.add(x);
            }
        }
        model.addAttribute("listToppingExist", listToppingExist);
        model.addAttribute("listBobaTeaExist", listBobaTeaExist);
        model.addAttribute("liststoreBobaTea", fixed);
        return "search";
    }

    @RequestMapping(value="filter", method = RequestMethod.GET)
    public String searchManagerbyBobaPage(Model model){
            List<BobaTeaModel> listBobaTeaExist = bobaTeaService.getListBobaTea();
            model.addAttribute("listBobaTeaExist", listBobaTeaExist);
            return "filter";
        }

    @RequestMapping(value="filter", params = {"bobaName"}, method = RequestMethod.GET)
    public String searchManagerbyBobaSubmit(
        @RequestParam(required = false, value = "bobaName") String bobaName,
        Model model){
            List<BobaTeaModel> listBobaTeaExist = bobaTeaService.getListBobaTea();
            List<ManagerModel> listManager = storeBobaTeaService.getManagerByBoba(bobaName);
            model.addAttribute("listManager", listManager);
            model.addAttribute("listBobaTeaExist", listBobaTeaExist);
            return "filter";
        }
}
