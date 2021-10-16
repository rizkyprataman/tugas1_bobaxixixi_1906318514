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
@Table(name = "boba_tea")
public class BobaTeaModel implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max=255)
    @Column(name="name", nullable = false)
    private String name;

    @NotNull
    @Column(name="price", nullable = false)
    private Integer price;

    @NotNull
    @Column(name="size", nullable = false)
    private Integer size;

    @NotNull
    @Column(name="ice_level", nullable = false)
    private Integer iceLevel;

    @NotNull
    @Column(name="sugar_level", nullable = false)
    private Integer sugarLevel;

    //Relasi dengan Topping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_topping", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ToppingModel topping;

    //Relasi dengan StoreBobaTea
    @OneToMany(
        mappedBy = "bobaTea",
        cascade = CascadeType.REMOVE,
        fetch = FetchType.LAZY)
    private List<StoreBobaTeaModel> storeBobaTeaModel;
 

}
