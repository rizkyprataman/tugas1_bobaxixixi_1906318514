package apap.tugas.bobaxixixi.repository;

import apap.tugas.bobaxixixi.model.ToppingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import java.util.Optional;

@Repository
public interface ToppingDb extends JpaRepository<ToppingModel, Long> {
  Optional<ToppingModel> findByName(String name);
}