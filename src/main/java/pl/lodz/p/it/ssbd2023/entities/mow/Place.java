package pl.lodz.p.it.ssbd2023.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2023.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2023.entities.mok.OwnerData;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "place", uniqueConstraints = {@UniqueConstraint(columnNames = {"placeNumber", "building" })})
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(
                name = "Place.findAll",
                query = "SELECT p FROM Place p"),
        @NamedQuery(
                name = "Place.findById",
                query = "SELECT p FROM Place p WHERE p.id = :id"),
        @NamedQuery(
                name = "Place.findByPlaceNumber",
                query = "SELECT p FROM Place p WHERE p.placeNumber = :placeNumber"),
        @NamedQuery(
                name = "Place.findAllActive",
                query = "SELECT p FROM Place p WHERE p.active = true"),
        @NamedQuery(
                name = "Place.findAllInactive",
                query = "SELECT p FROM Place p WHERE p.active = false"),
        @NamedQuery(
                name = "Place.findByResidentsNumber",
                query = "SELECT p FROM Place p WHERE p.residentsNumber = :residentsNumber"),
        @NamedQuery(
                name = "Place.findByResidentsNumberAndActive",
                query = "SELECT p FROM Place p WHERE p.residentsNumber = :residentsNumber AND p.active = true"),
        @NamedQuery(
                name = "Place.findByResidentsNumberAndInactive",
                query = "SELECT p FROM Place p WHERE p.residentsNumber = :residentsNumber AND p.active = false"),
        @NamedQuery(
                name = "Place.findBySquareFootage",
                query = "SELECT p FROM Place p WHERE p.squareFootage = :squareFootage"),
        @NamedQuery(
                name = "Place.findBySquareFootageAndActive",
                query = "SELECT p FROM Place p WHERE p.squareFootage = :squareFootage AND p.active = true"),
        @NamedQuery(
                name = "Place.findBySquareFootageAndInactive",
                query = "SELECT p FROM Place p WHERE p.squareFootage = :squareFootage AND p.active = false"),
        @NamedQuery(
                name = "Place.findByAddress",
                query = "SELECT p FROM Place p WHERE p.building.address = :address"),
        @NamedQuery(
                name = "Place.findByAddressAndActive",
                query = "SELECT p FROM Place p WHERE p.building.address = :address AND p.active = true"),
        @NamedQuery(
                name = "Place.findByAddressAndInactive",
                query = "SELECT p FROM Place p WHERE p.building.address = :address AND p.active = false")
})
public class Place extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Basic(optional = false)
    @Column(name = "place_number", nullable = false)
    @Getter
    @Setter
    private Integer placeNumber;

    @NotNull
    @Basic(optional = false)
    @Column(name = "square_footage", nullable = false)
    @Getter
    @Setter
    private BigDecimal squareFootage;

    @NotNull
    @Basic(optional = false)
    @Column(name = "residents_number", nullable = false)
    @Getter
    @Setter
    private Integer residentsNumber;

    @NotNull
    @Basic(optional = false)
    @Column(name = "active", nullable = false)
    @Getter
    @Setter
    private boolean active;

    @NotNull
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "building_id", referencedColumnName = "id", updatable = false, nullable = false)
    @Getter
    @Setter
    private Building building;

    @NotNull
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "place_owner",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id"))
    @Getter
    @Setter
    private Set<OwnerData> owners = new HashSet<>();

    @NotNull
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "place_rate",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "rate_id"))
    @Getter
    @Setter
    private Set<Rate> currentRates = new HashSet<>();

    @NotNull
    @OneToMany(mappedBy = "place", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @Getter
    @Setter
    private Set<Meter> meters = new HashSet<>();

    public Place(Integer placeNumber, BigDecimal squareFootage, Integer residentsNumber, boolean active,
                 Building building) {
        this.placeNumber = placeNumber;
        this.squareFootage = squareFootage;
        this.residentsNumber = residentsNumber;
        this.active = active;
        this.building = building;
    }
}
