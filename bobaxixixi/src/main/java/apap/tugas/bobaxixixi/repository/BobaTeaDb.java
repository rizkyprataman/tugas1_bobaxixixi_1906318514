package apap.tugas.bobaxixixi.repository;

import apap.tugas.bobaxixixi.model.BobaTeaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface BobaTeaDb extends JpaRepository<BobaTeaModel, Long> {
  Optional<BobaTeaModel> findById(String id);
  List<BobaTeaModel> findByName(String name);
}