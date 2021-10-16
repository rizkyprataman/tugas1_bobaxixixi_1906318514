package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.ManagerModel;
import apap.tugas.bobaxixixi.repository.ManagerDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import javax.transaction.Transactional;
import java.util.List;
 
@Service
@Transactional
public class ManagerServiceImpl implements ManagerService{
    
    @Autowired
    ManagerDb managerDb;

    @Override
    public List<ManagerModel> getListManager(){
        return managerDb.findAll();
    }
}
