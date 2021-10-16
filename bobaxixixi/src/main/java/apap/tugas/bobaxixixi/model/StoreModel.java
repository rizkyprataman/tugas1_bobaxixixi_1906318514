package apap.tugas.bobaxixixi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "store")
public class StoreModel implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max=255)
    @Column(name="name", nullable = false)
    private String name;

    @NotNull
    @Size(max=255)
    @Column(name="address", nullable = false)
    private String address;

    @NotNull
    @Size(max=10)
    @Column(name="store_code", nullable = false, unique = true)
    private String storeCode;

    @NotNull
    @Column(name="open_hour", nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime openHour;

    @NotNull
    @Column(name="close_hour", nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime closeHour;

    //Relasi dengan ManagerModel
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_manager", referencedColumnName = "id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ManagerModel manager;
    
    //Relasi dengan StoreBobaTea
    @OneToMany(
        mappedBy = "store",
        cascade = CascadeType.REMOVE,
        fetch = FetchType.LAZY)
    private List<StoreBobaTeaModel> storeBobaTeaModel;
 
    
}
